package com.dev.model.dao;

import com.dev.common.dao.BaseDao;
import com.dev.common.utils.Constants;
import com.dev.common.utils.UtilityClass;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class QuotaVehicleListProvider implements VehicleListProvider {

    private static Logger log = Logger.getLogger(QuotaVehicleListProvider.class);
    private BaseDao dao;
    private UtilityClass utilityClass;
    private Connection connection;

    public QuotaVehicleListProvider() {
        dao = new BaseDao();
        connection = dao.getConnection();
        utilityClass = new UtilityClass();
    }

    public Vector<String> VehicleProvider() throws SQLException {

        fillTempVehicle();
        int recordForProcess = getVehListCount();
        Vector<String> vehicleList = new Vector<>();
        /*
        means that there is s.th to process
         */
        if (recordForProcess > 0) {
            vehicleList = fillVectorList();
            log.info("START OF DAILY VEHICLE FOR  " + recordForProcess + " RECORDS ");
        }
        if (connection != null)
            connection.close();
        return vehicleList;
    }

    @Override
    public int getVehListCount() throws SQLException {

        int recordCnt = 0;
        PreparedStatement chk = null;
        String sql = "SELECT COUNT(*) FROM TEMP_VEHICLE";
        chk = connection.prepareStatement(sql);
        ResultSet cntRes = chk.executeQuery();

        if (cntRes.next()) {
            recordCnt = cntRes.getInt(1);
        }
        log.info("TEMP_VEHICLE TABLE INITIALIZED,RECORDS IS: " + recordCnt);
        cntRes.close();
        chk.close();
        return recordCnt;
    }

    private void fillTempVehicle() throws SQLException {
        /* THIS METHOD FETCH ALL CANDIDATE VEHICLES FOR GETTING QUOTA
        WE SHOULD PUT 1 BULK FOR EACH ACTIVE VEHICLE FOR EVERY QUOTA RUN, BUT THERE MAY BE
        SOME VEHICLES THAT HAVE NOT CONSUMED THEIR PREVIOUS BULK YET. SO WE CAN ACCESS ALL THE CANDIDATES
        FOR THIS PROCESS BY DOING A MINUS OF QUOTA_INFO CARDS FROM ACTIVE VEHICLE CARDS
        TEMP_VEHICLE IS AN INTERFACE TABLE IN DB SCHEMA AND WILL BE TRUNCATED WITH EACH RUN */

        PreparedStatement chk = null;

        log.info("INITIALING TEMP_VEHICLE TABLE...");
        String st = "TRUNCATE TABLE TEMP_VEHICLE";

        chk = connection.prepareStatement(st);

        chk.executeUpdate();
        log.info("TEMP_VEHICLE TRUNCATED");

        //ADD WITHOUT BULK VEHICLES TO FETCH LIST
        String s = utilityClass.concat("INSERT INTO TEMP_VEHICLE ",
                "SELECT DISTINCT(CARD_ID) FROM VEHICLE ",
                "WHERE  IS_PROCESSED=? ",
                "  AND CARD_STATUS='0' ",
                "MINUS ",
                "SELECT DISTINCT(CARD_ID) FROM quota_info");
        chk = connection.prepareStatement(s);
        chk.setString(1, Constants.NOT_PROCESSED);

        int t = chk.executeUpdate();
        log.info("TEMP_VEHICLE NO:" + t);

        chk.close();

        connection.commit();

    }

    @Override
    public Vector<String> fillVectorList() throws SQLException {
        Vector<String> vector = new Vector<>();
        PreparedStatement chk = null;
        String sql = "SELECT card_id FROM TEMP_VEHICLE";
        chk = connection.prepareStatement(sql);
        ResultSet resultSet = chk.executeQuery();

        while (resultSet.next()) {
            vector.add(resultSet.getString(1));
        }

        chk.close();
        return vector;
    }

}
