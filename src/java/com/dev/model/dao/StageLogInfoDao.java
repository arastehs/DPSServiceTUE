package com.dev.model.dao;

import com.dev.common.dao.BaseDao;
import com.dev.model.entity.StatusResponse;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StageLogInfoDao implements LogInfo {
    private static Logger log = Logger.getLogger(StageLogInfoDao.class);
    private Connection connection;
    private BaseDao dao;


    public StageLogInfoDao() {

    }

    @Override
    public StatusResponse reportStageLogInfo(String jobName) throws SQLException {

        dao = new BaseDao();
        connection = dao.getConnection();

        PreparedStatement chk = null;
        StatusResponse status = null;

        String st = "SELECT * FROM STAGE_LOG a WHERE job_name=? and " +
                " rowid in(SELECT max(rowid) FROM STAGE_LOG a WHERE job_name=?)";

        chk = connection.prepareStatement(st);
        chk.setString(1, jobName.toLowerCase());
        chk.setString(2, jobName.toLowerCase());

        ResultSet resultSet = chk.executeQuery();
        if (resultSet.next()) {
            String jName = resultSet.getString(1);//JOB NAME
            String jStatus = resultSet.getString(2);//JOB STATUS
            String dmDate = resultSet.getTimestamp(3).toString();//DM DATE
            status = new StatusResponse(jName, jStatus, dmDate);
        }

        resultSet.close();
        chk.close();
        if (connection != null)
            connection.close();

        return status;

    }

    @Override
    public void insertLogInfo(String jobName, String status) throws SQLException {

        dao = new BaseDao();
        connection = dao.getConnection();

        PreparedStatement chk = null;

        String st = "INSERT INTO STAGE_LOG VALUES(?,?,SYSDATE,SEQ_STAGE.NEXTVAL)";

        try {
            chk = connection.prepareStatement(st);

            chk.setString(1, jobName.toLowerCase());
            chk.setString(2, status);
            chk.executeUpdate();

            chk.close();
            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
                throw e;
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
    }
}
