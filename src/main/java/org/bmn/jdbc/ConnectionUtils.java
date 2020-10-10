package org.bmn.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection getMyConnection()
    {
        try {
            return PostgresConnUtils.getPostgresConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
