package test.by.slesh.itechart.fullcontact.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import by.slesh.itechart.fullcontact.db.JdbcConnector;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 *
 */
public class JdbcConnectTest {
    @Test
    public void testDefaultConnect() throws ClassNotFoundException, IOException, SQLException {
	Connection connection = JdbcConnector.getCurrentConnection();
	Assert.assertNotNull("Connection instance null", connection);
	JdbcConnector.close(connection);
    }
}
