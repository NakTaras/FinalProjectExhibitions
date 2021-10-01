package com.my.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AutoCloseableClose {

    private static final Logger logger = LogManager.getLogger(AutoCloseableClose.class);

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ex) {
                logger.error("Cannot close connection!", ex);
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                logger.error("Cannot close statement!", ex);
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception ex) {
                logger.error("Cannot close result set!", ex);
            }
        }
    }
}
