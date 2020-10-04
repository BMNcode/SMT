package org.bmn.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection getMyConnection() throws SQLException
    {

        return PostgresConnUtils.getPostgresConnection();
    }
}
