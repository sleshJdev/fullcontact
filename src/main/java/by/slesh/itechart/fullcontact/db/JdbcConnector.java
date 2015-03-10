package by.slesh.itechart.fullcontact.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 *
 */
public final class JdbcConnector {
    private final static Logger LOGGER = LoggerFactory.getLogger(JdbcConnector.class);
    private static final String DEFAULT_PROPERTY_FILE_NAME = "jdbc.properties";
    private static Connection connection;

    private JdbcConnector() {
    }

    private static File getDefaultPropertyFile() {
	ClassLoader classLoader = JdbcConnector.class.getClassLoader();
	String propertyPath = classLoader.getResource(DEFAULT_PROPERTY_FILE_NAME).getFile();
	return new File(propertyPath);
    }

    public static Connection openConnection() throws ClassNotFoundException, IOException, SQLException {

	return openConnection(new FileInputStream(getDefaultPropertyFile()));
    }

    public static Connection openConnection(InputStream stream) throws IOException, ClassNotFoundException,
	    SQLException {
	Properties properties = new Properties();
	properties.load(stream);
	return openConnection(properties);
    }

    public static Connection openConnection(Properties properties) throws IOException, ClassNotFoundException,
	    SQLException {
	Class.forName(properties.getProperty("jdbc.driverClassName"));
	String url = properties.getProperty("jdbc.databaseurl");
	String user = properties.getProperty("jdbc.username");
	String password = properties.getProperty("jdbc.password");

	return DriverManager.getConnection(url, user, password);
    }

    public static Connection getCurrentConnection() throws ClassNotFoundException, IOException, SQLException {
	if (connection == null || connection.isClosed()) {
	    connection = openConnection();
	}
	return connection;
    }

    public static void close() throws SQLException {
	closeResource(connection);
    }

    public static void closeResource(Connection connection, Statement... statements) {
	if (connection != null) {
	    try {
		connection.close();
		connection = null;
	    } catch (SQLException e) {
		LOGGER.error("error occured during release resources: {}", e);
	    }
	}

	if (statements != null) {
	    for (Statement statement : statements) {
		if (statement != null) {
		    try {
			statement.close();
			statement = null;
		    } catch (SQLException e) {
			LOGGER.error("error occured during release resources: {}", e);
		    }
		}
	    }
	}
    }
}
