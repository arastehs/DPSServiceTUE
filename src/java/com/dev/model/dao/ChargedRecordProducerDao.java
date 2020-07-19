package com.dev.model.dao;

import com.dev.common.dao.BaseDao;
import com.dev.common.exceptions.CardNotFoundException;
import com.dev.common.utils.UtilityClass;
import com.dev.model.entity.ChargedRecordBuilder;
import com.dev.model.entity.ChargedRecord;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
This class is responsible for getting the send_charged record from db,
inserting it into the acknowledged table,
and deleting it from the temp_charged records
 */
public class ChargedRecordProducerDao {

    private static Logger log = Logger.getLogger(ChargedRecordProducerDao.class);
    private UtilityClass utilityClass;
    private ChargedRecord chargedRecord;
    private Connection connection;
    private BaseDao baseDao;

    public ChargedRecordProducerDao() {
        baseDao = new BaseDao();
        connection = baseDao.getConnection();
        utilityClass = new UtilityClass();
    }

    public int chargedAcknowledged(String id) throws CardNotFoundException, SQLException {

        //split the id tho get the cardId and seq
        String cardId = id.substring(0, 10);
        Short seqNo = Short.valueOf(id.substring(10));
        int cntDone = 0;

        try {
            getVehicleInformation(cardId, seqNo);

            if (cardId != null) {

                insertIntoChargedTable();
                deleteFromTempTable();
                connection.commit();

                cntDone++;

            } else {
                //card not found exception
                connection.rollback();
                throw new CardNotFoundException(cardId, seqNo);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return cntDone;

    }

    /*
    get the corresponding record of send_charge table
     */
    private void getVehicleInformation(String cardId, Short seqId) throws SQLException {

        PreparedStatement statement = null;
        ResultSet sentRes = null;

        String st = utilityClass.concat("SELECT a.*",
                " FROM SENT_CHARGE a",
                " WHERE CARD_ID=? AND SEQ_NO=?  ");

        statement = connection.prepareStatement(st);
        statement.setString(1, cardId);
        statement.setShort(2, seqId);

        sentRes = statement.executeQuery();
        chargedRecord = new ChargedRecordBuilder().build();

        if (sentRes.next()) {

            chargedRecord = new ChargedRecordBuilder().setIds(sentRes.getString("VEHICLE_ID"), cardId).
                    setQuota(sentRes.getInt("QUOTA"), sentRes.getString("QUOTA_TYPE")).
                    setSeqNo(sentRes.getShort("SEQ_NO")).
                    setRemains(sentRes.getInt("FUNCTIONAL_REMAIN"), sentRes.getInt("PUBLIC_REMAIN")).
                    build(); //build the corresponding charged record

        }
        sentRes.close();
        statement.close();

    }

    private int insertIntoChargedTable() throws SQLException {

        PreparedStatement statement = null;
        int inserted = 0;

        String st = "INSERT INTO CHARGED_RECORD VALUES(?,?,?,?,?,?,?)";

        statement = connection.prepareStatement(st);

        statement.setString(1, chargedRecord.getCardId());
        statement.setInt(2, chargedRecord.getQuota());
        statement.setShort(3, chargedRecord.getSeqNo());
        statement.setString(4, chargedRecord.getVehicleId());
        statement.setInt(5, chargedRecord.getFunctionalRem());
        statement.setInt(6, chargedRecord.getPublicRem());
        statement.setString(7, chargedRecord.getQuotaType());

        inserted = statement.executeUpdate();
        statement.close();

        return inserted;
    }

    private int deleteFromTempTable() throws SQLException {

        PreparedStatement statement = null;
        int deleted = 0;

        String st = "DELETE FROM TEMP_CHARGED_RECORDS WHERE  CARD_ID=? AND SEQ_NO=?";

        statement = connection.prepareStatement(st);

        statement.setString(1, chargedRecord.getCardId());
        statement.setShort(2, chargedRecord.getSeqNo());

        deleted = statement.executeUpdate();

        statement.close();
        return deleted;
    }

}
