package com.my.util;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DataSourceUtil {

    private static DataSource dataSource;

    private DataSourceUtil(){

    }

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            try {
                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                dataSource = (javax.sql.DataSource) envContext.lookup("jdbc/TestDB");
            } catch (NamingException ex) {
                throw new IllegalStateException("Cannot init DBManager", ex);
            }
        }
        return dataSource;
    }
}
