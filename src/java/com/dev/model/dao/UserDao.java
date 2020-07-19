package com.dev.model.dao;

import com.dev.common.dao.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDao {

    private BaseDao dao;
    private Connection connection;

    public UserDao() {
        dao = new BaseDao();
        connection = dao.getConnection();
    }

    public int getUserFromDB(String userName, String pass, String level) throws SQLException {

        int cnt = 0;
        PreparedStatement chk = null;
        String sql;

        sql = "select count(*) from user_info where user_name=? and password=? and user_level=?";

        chk = connection.prepareStatement(sql);

        chk.setString(1, userName);
        chk.setString(2, pass);

        chk.setString(3, level);


        ResultSet resultSet = chk.executeQuery();

        if (resultSet.next()) {
            cnt = resultSet.getInt(1);
        }
        chk.close();
        if (connection != null)
            connection.close();
        return cnt;
    }

}
