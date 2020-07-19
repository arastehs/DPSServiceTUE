/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.common.dao;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;

public class BaseDao {

    private static Logger log = Logger.getLogger(BaseDao.class);

    private DataSource ds;

    public BaseDao() {
        ds = SingleDataSource.getDataSource();
    }

    public Connection getConnection() {
        try {

            Connection conn = ds.getConnection();
            conn.setAutoCommit(false);
            return conn;

        } catch (Exception ex) {
            log.error(ex.toString());
            return null;
        }

    }

}
