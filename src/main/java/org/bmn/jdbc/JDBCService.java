package org.bmn.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCService {


    public int executeUpdate(String query) throws SQLException {
        Connection connection = ConnectionUtils.getMyConnection();
        Statement statement = connection.createStatement();
        // Для Insert, Update, Delete
        int result = statement.executeUpdate(query);
        return result;
    }

    public void insertShapeTable() throws SQLException {
        String customerEntryQuery = "INSERT INTO Shape " +
                "VALUES (default, 'Rectangle', 50, 25, 'Color.BLACK', 'Color.WHITE')";
        executeUpdate(customerEntryQuery);
    }

}
