package com.dev.model.dao;

import com.dev.common.dao.BaseDao;
import com.dev.common.utils.Constants;
import com.dev.common.utils.UtilityClass;
import com.dev.model.entity.CalculatedQuota;
import com.dev.model.entity.Vehicle;
import com.dev.model.entity.VehicleBuilder;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
This class is responsible for calculating the corresponding bulk for the cardId if it has remains
(2 types of remains, functional & public, and the priority is for functional)
putting the generated bulk into quota_info
updating the vehicle profile
 */
public class QuotaBulkProducerDao {

    private static Logger log = Logger.getLogger(QuotaBulkProducerDao.class);
    private UtilityClass utilityClass;
    private Connection connection;
    private BaseDao baseDao;

    public QuotaBulkProducerDao() {
        baseDao = new BaseDao();
        connection = baseDao.getConnection();
        utilityClass = new UtilityClass();
    }


    public int sendDailyBulk(String cardId) throws SQLException {


        Vehicle vehInfoMain = null; //the profile object
        int countDone = 0;
        try {
            vehInfoMain = getVehicleInformation(cardId);

            /*method calculateQuota*/
            CalculatedQuota cq = calculateQuota(vehInfoMain);
            if (cq.getCalculatedQuota() > 0) {

                /*
                    ADD THE SEQUENCE
                    INSERT BULK INTO quota_info
               */
                vehInfoMain.addSeq(1);

                insertToQuota(vehInfoMain, cq);

                updateVehicle(vehInfoMain);
                connection.commit();

            } else {
                log.info("all remains are zero! no bulk remained for the card_id= " + cardId);
            }

            countDone++;
        } catch (SQLException e) {
            try {
                connection.rollback();

                e.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return countDone;
    }

    /*
    join the vehicle profile record with the Rule_Quota table( a table that keeps the rules for bulk_size , ...)
    and fill the Vehicle object
     */
    private Vehicle getVehicleInformation(String cardId) throws SQLException {

        PreparedStatement statement = null;

        String sql = utilityClass.concat("SELECT v.*,r.BULK_SIZE from vehicle v , rule_quota r ",
                "where v.card_id=?  ",
                "and v.clazz= r.clazz and v.rule_id = r.rule_id and  v.fuel_code=r.fuel_code");


        statement = connection.prepareStatement(sql);
        statement.setString(1, cardId);

        ResultSet vehInfoRes = statement.executeQuery();

        Vehicle vehInfoMain = new VehicleBuilder().build();


        if (vehInfoRes.next()) {

            vehInfoMain = new VehicleBuilder().addCard(vehInfoRes.getString("CARD_ID"), vehInfoRes.getString("VEHICLE_ID"))
                    .addRules(vehInfoRes.getString("RULE_ID"), vehInfoRes.getString("CLAZZ"),
                            vehInfoRes.getString("FUEL_CODE"))
                    .withVehicleStatus(vehInfoRes.getString("CARD_STATUS"))
                    .withRemain(vehInfoRes.getInt("FUNCTIONAL_REMAIN"), vehInfoRes.getShort("LAST_SEQ")
                            , vehInfoRes.getInt("PUBLIC_REMAIN"))
                    .withBulk(vehInfoRes.getInt("BULK_SIZE"))
                    .build(); //build the object using builder design pattern

        }
        vehInfoRes.close();
        statement.close();

        return vehInfoMain;
    }

    /*method calculateQuota: CALCULATE BULK OF QUOTA FOR SELECTED VEHICLE RECORD*/
    private CalculatedQuota calculateQuota(Vehicle vehicle) {

        CalculatedQuota calcOut = new CalculatedQuota();

        int fRemain = vehicle.getFunctionalRemain();
        int pRemain = vehicle.getPublicRemain();
        int bSize = vehicle.getBulkSize();

        if (fRemain > 0) { // F >(0), OK get the bulk

            if (fRemain <= bSize) { //0 < F <= B , functional remain is less than bulk_size

                calcOut.setCalculatedQuota(fRemain);
                fRemain = 0;
                vehicle.setFunctionalRemain(fRemain);

            } else { //  F > B , subtract the bulk from the profile

                fRemain = fRemain - bSize;
                vehicle.setFunctionalRemain(fRemain);
                calcOut.setCalculatedQuota(bSize);

            }

            calcOut.setCalculatedType(Constants.QUOTA_TYPE_FUNCTIONAL);

        } // END OF F >(0)
        else { // F=0

            if (pRemain == 0) { //no bulk exist!

                calcOut.setCalculatedQuota(0);
                fRemain = 0;
                vehicle.setFunctionalRemain(fRemain);
                vehicle.setPublicRemain(fRemain);

                calcOut.setCalculatedType(Constants.QUOTA_TYPE_FUNCTIONAL);

            } else { //pRemain>0

                if (pRemain > bSize) { //P>B

                    calcOut.setCalculatedQuota(bSize);
                    pRemain = pRemain - bSize;

                } else {    //P<=B

                    calcOut.setCalculatedQuota(pRemain);
                    pRemain = 0;
                }
                vehicle.setPublicRemain(pRemain);
                vehicle.setFunctionalRemain(fRemain);

                calcOut.setCalculatedType(Constants.QUOTA_TYPE_PUBLIC);

            }

        }

        return calcOut;
    }


    /*
    insert the generated bulk into the quota_table
     */
    private int insertToQuota(Vehicle vehInfoMain, CalculatedQuota cq) throws SQLException {
        PreparedStatement chk = null;

        String st = utilityClass.concat("INSERT INTO quota_info ",
                "VALUES (?,?,?,?,?,?,?)");

        chk = connection.prepareStatement(st);

        chk.setString(1, vehInfoMain.getCardId());
        chk.setString(2, vehInfoMain.getVehicleId());
        chk.setShort(3, vehInfoMain.getLastSeq());
        chk.setInt(4, vehInfoMain.getFunctionalRemain());
        chk.setInt(5, vehInfoMain.getPublicRemain());
        chk.setString(6, cq.getCalculatedType());
        chk.setInt(7, cq.getCalculatedQuota());

        int inserted = chk.executeUpdate();
        chk.close();
        return inserted;
    }


    /*
    update the vehicle table with new remains, and increased seq, put the is_processed=true
     */
    private void updateVehicle(Vehicle vehInfoMain) throws SQLException {

        PreparedStatement chk = null;

        String st = utilityClass.concat(" UPDATE VEHICLE ",
                "SET FUNCTIONAL_REMAIN =? ,",
                "LAST_SEQ =? ,",
                "PUBLIC_REMAIN =? ,",
                "IS_PROCESSED =? ",
                " WHERE CARD_ID=?");

        chk = connection.prepareStatement(st);

        chk.setInt(1, vehInfoMain.getFunctionalRemain());
        chk.setShort(2, vehInfoMain.getLastSeq());
        chk.setInt(3, vehInfoMain.getPublicRemain());
        chk.setString(4, Constants.QUOTA_PROCESSED);
        chk.setString(5, vehInfoMain.getCardId());

        chk.executeUpdate();
        chk.close();

    }

}
