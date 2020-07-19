package com.dev.common.dao;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SingleDataSource {

    private static DataSource dataSource;

    private static Logger log = Logger.getLogger(SingleDataSource.class);


    private SingleDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            try {

                InitialContext ic = new InitialContext();

                Context envCtx = (Context) ic.lookup("java:comp/env");
                dataSource = (DataSource) envCtx.lookup("jdbc/dpsServiceRest");

            } catch (NamingException ex) {
                log.error(ex.toString());
            }

        }
        return dataSource;
    }
}
