package jdbc;

import org.bmn.jdbc.ConnectionUtils;
import org.bmn.jdbc.JDBCService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestConnection {

    private static Connection connection;

    @Before
    public void init() throws SQLException {
        connection = ConnectionUtils.getMyConnection();
    }

    @Test
    public void testShouldGetJdbcConnection() throws SQLException {
        try(Connection connection = ConnectionUtils.getMyConnection()) {
            Assert.assertTrue(connection.isValid(1));
            Assert.assertFalse(connection.isClosed());
        }
    }

    @Test
    public void shouldInsertShapeTable() throws SQLException {
        JDBCService jdbcService = new JDBCService();
        jdbcService.insertShapeTable();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Shape WHERE shape_name = ?");
        statement.setString(1, "Rectangle");
        boolean hasResult = statement.execute();
        assertTrue(hasResult);
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        int width = resultSet.getInt("width");
        assertEquals(50, width);
    }

    @After
    public void close() throws SQLException{
        connection.close();
    }

}