package com.dev.model.dao;

import com.dev.common.dao.BaseDao;
import com.dev.common.utils.UtilityClass;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ChargedVehicleListProvider implements VehicleListProvider {

    private static Logger log = Logger.getLogger(ChargedVehicleListProvider.class);
    private UtilityClass utilityClass;
    private BaseDao dao;
    private Connection connection;

    public ChargedVehicleListProvider() {
        dao = new BaseDao();
        connection = dao.getConnection();
        utilityClass = new UtilityClass();
    }

    public Vector<String> VehicleProvider() throws SQLException {

        int recordForProcess = 0;
        Vector<String> ChargedAckList;

        //GET COUNT OF CANDIDATES
        recordForProcess = getVehListCount();

        ChargedAckList = new Vector<>();

        if (recordForProcess > 0) {
            //fill the vector of cards
            ChargedAckList = fillVectorList();
            log.info("START OF INSERTING INTO CHARGED_RECORD FOR " + recordForProcess + " RECORDS");
        }

        if (connection != null)
            connection.close();
        return ChargedAckList;
    }

    /*
    COUNT ALL UNIQUE PAIRS OF CARD_ID AND SEQ_NOs IN TEMP_CHARGED_RECORDS
     */
    @Override
    public int getVehListCount() throws SQLException {

        int recordCnt = 0;
        PreparedStatement chk = null;

        String sql = utilityClass.concat("select count(*) from(select  card_id,seq_no from ",
                "TEMP_CHARGED_RECORDS a",
                " where a.rowid in (select max(rowid) from TEMP_CHARGED_RECORDS ",
                "where  card_id=a.card_id  and seq_no = a.seq_no))");

        chk = connection.prepareStatement(sql);
        ResultSet cntRes = chk.executeQuery();

        if (cntRes.next()) {
            recordCnt = cntRes.getInt(1);
        }
        log.info("TEMP_CHARGED_INITIALIZED,RECORDS IS: " + recordCnt);

        cntRes.close();
        chk.close();

        return recordCnt;
    }

    /*
    FILL A VECTOR FROM ALL UNIQUE PAIRS OF CARD_ID AND SEQ_NOs IN TEMP_CHARGED_RECORDS
     */
    @Override
    public Vector<String> fillVectorList() throws SQLException {

        Vector<String> vector = new Vector<>();
        PreparedStatement chk = null;
        String sql = utilityClass.concat("select  card_id||seq_no from TEMP_CHARGED_RECORDS a",
                " where a.rowid in (select max(rowid) from TEMP_CHARGED_RECORDS",
                " where  card_id=a.card_id  and seq_no = a.seq_no)");
        chk = connection.prepareStatement(sql);
        ResultSet resultSet = chk.executeQuery();

        while (resultSet.next()) {
            vector.add(resultSet.getString(1));
        }

        resultSet.close();
        chk.close();
        return vector;
    }

}
