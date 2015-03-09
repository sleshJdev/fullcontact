package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.db.JdbcConnector;

public abstract class AbstractDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);

    private boolean isUseCurrentConnection = true;
    private boolean isCloseConnectionAfterWork = true;

    private Connection connection;
    protected Statement statement;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    public AbstractDao() {
    }

    public AbstractDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	this.isUseCurrentConnection = isUseCurrentConnection;
	this.isCloseConnectionAfterWork = isCloseConnectionAfterWork;
    }

    public boolean isUseCurrentConnection() {
	return isUseCurrentConnection;
    }

    public void setUseCurrentConnection(boolean isUseCurrentConnection) {
	this.isUseCurrentConnection = isUseCurrentConnection;
    }

    public boolean isCloseConnectionAfterWork() {
	return isCloseConnectionAfterWork;
    }

    public void setCloseConnectionAfterWork(boolean isCloseConnectionAfterWork) {
	this.isCloseConnectionAfterWork = isCloseConnectionAfterWork;
    }

    protected void connect() throws ClassNotFoundException, IOException, SQLException {
	connection = isUseCurrentConnection ? JdbcConnector.getCurrentConnection() : JdbcConnector.openConnection();
    }

    private void checkConnection() throws SQLException {
	if (connection == null || connection.isClosed()) {
	    throw new SQLException("connection is null or closed");
	}
    }

    protected Statement getStatement() throws SQLException {
	checkConnection();

	return connection.createStatement();
    }

    protected PreparedStatement getPrepareStatement(String sql) throws SQLException {
	checkConnection();

	return connection.prepareStatement(sql);
    }

    protected PreparedStatement getPrepareStatement(String sql, int autoGenerateKeys) throws SQLException {
	checkConnection();

	return connection.prepareStatement(sql, autoGenerateKeys);
    }

    protected void closeResources() {
	closeResource(connection, statement, preparedStatement);
    }

    protected void closeResource(Connection connection, Statement... statements) {
	try {
	    if (isCloseConnectionAfterWork) {
		if (connection != null) {
		    connection.close();
		    connection = null;
		}
	    }
	} catch (SQLException e) {
	    LOGGER.error("error occured release resources: {}", e.getMessage());
	}
	for (Statement statement : statements) {
	    try {
		if (statement != null) {
		    statement.close();
		    statement = null;
		}
	    } catch (SQLException e) {
		LOGGER.error("error occured release resources: {}", e.getMessage());
	    }
	}
    }
}
