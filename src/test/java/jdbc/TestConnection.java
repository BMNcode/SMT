package jdbc;

import org.bmn.jdbc.ConnectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    @Test
    public void testShouldGetJdbcConnection() throws SQLException {
        try(Connection connection = ConnectionUtils.getMyConnection()) {
            Assert.assertTrue(connection.isValid(1));
            Assert.assertFalse(connection.isClosed());
        }
    }

}