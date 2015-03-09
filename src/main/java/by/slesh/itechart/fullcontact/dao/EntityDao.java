package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.reader.DaoReader;
import by.slesh.itechart.fullcontact.domain.Entity;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public abstract class EntityDao<T extends Entity> extends AbstractDao implements Getable<T>, Countable, Deletable {
    private final static Logger LOGGER = LoggerFactory.getLogger(EntityDao.class);

    private String deleteRangeQuery;
    private String deleteQuery;
    private String countQuery;
    private String getByIdQuery;
    private String getIdQuery;
    private String getAllQuery;
    private String getLimitQuery;
    private DaoReader<T> reader;

    public EntityDao() {
    }

    public EntityDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
    }

    public EntityDao(final String deleteRangeQuery, final String deleteQuery, final String countQuery, final String getByIdQuery, final String getIdQuery,
	    final String getLimitQuery, final String getAllQuery, DaoReader<T> reader, boolean isUseCurrentConnection,
	    boolean isCloseConnectionAfterWork) {
	this(isUseCurrentConnection, isCloseConnectionAfterWork);
	this.deleteRangeQuery = deleteRangeQuery;
	this.deleteQuery = deleteQuery;
	this.countQuery = countQuery;
	this.getByIdQuery = getByIdQuery;
	this.getIdQuery = getIdQuery;
	this.getLimitQuery = getLimitQuery;
	this.getAllQuery = getAllQuery;
	this.reader = reader;
    }

    public void setDeleteRangeQuery(String deleteRangeQuery) {
        this.deleteRangeQuery = deleteRangeQuery;
    }

    public void setDeleteQuery(String deleteQuery) {
	this.deleteQuery = deleteQuery;
    }

    public void setCountQuery(String countQuery) {
	this.countQuery = countQuery;
    }

    public void setGetByIdQuery(String getQuery) {
	this.getByIdQuery = getQuery;
    }

    public void setGetIdQuery(final String getIdQuery) {
	this.getIdQuery = getIdQuery;
    }

    public void setGetLimitQuery(String getLimitQuery) {
	this.getLimitQuery = getLimitQuery;
    }

    public void setGetAllQuery(final String getAllQuery) {
	this.getAllQuery = getAllQuery;
    }

    @SuppressWarnings("unchecked")
    public void setReader(DaoReader<Entity> reader) {
	this.reader = (DaoReader<T>) reader;
    }
    
    @Override
    public long deleteRange(long contactId, long[] ids) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("contactId: {}, ids: {}", contactId, Arrays.toString(ids));

	if (ids == null || ids.length == 0) {
	    LOGGER.info("RETURN: not entities for delete");
	    return 0;
	}
	
	if (StringUtils.isEmptyOrWhitespaceOnly(deleteRangeQuery)) {
	    throw new SQLException("'delete range' sql query is not found. maybe this method not supported");
	}
	
	long rowsDeleted = 0;
	try {
	    connect();
	    String inStatement = Arrays.toString(ids).replaceAll("[\\]\\[]", "");
	    String query = String.format(deleteRangeQuery, inStatement);
	    preparedStatement = getPrepareStatement(query);
	    preparedStatement.setLong(1, contactId);
	    preparedStatement.executeUpdate();
	    rowsDeleted = preparedStatement.getUpdateCount();

	    LOGGER.info("query: {}", preparedStatement);
	} finally {
	    closeResources();
	}

	LOGGER.info("delete {} entity", rowsDeleted);
	LOGGER.info("END");
	return rowsDeleted;
    }

    @Override
    public void delete(long id) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("id: {}", id);

	if (StringUtils.isEmptyOrWhitespaceOnly(countQuery)) {
	    throw new SQLException("'delete' sql query is not found. maybe this method not supported");
	}
	
	try {
	    connect();
	    preparedStatement = getPrepareStatement(deleteQuery);
	    preparedStatement.setLong(1, id);
	    preparedStatement.executeUpdate();

	    LOGGER.info("query: {}", preparedStatement);
	} finally {
	    closeResources();
	}

	LOGGER.info("END");
    }

    @Override
    public long count() throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	if (StringUtils.isEmptyOrWhitespaceOnly(countQuery)) {
	    throw new SQLException("'cout' sql query is not found. maybe this method not supported");
	}
	
	long quantity = 0;
	try {
	    connect();
	    statement = getStatement();
	    resultSet = statement.executeQuery(countQuery);
	    while (resultSet.next()) {
		quantity = resultSet.getLong(1);
	    }

	    LOGGER.info("quantity: " + quantity + " contacts");
	    LOGGER.info("query: " + statement);
	} finally {
	    closeResources();
	}

	LOGGER.info("END");

	return quantity;
    }

    @Override
    public long getId(String value) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("value: {}", value);

	if (StringUtils.isEmptyOrWhitespaceOnly(getIdQuery)) {
	    throw new SQLException("'get id' sql query is not found. maybe this method not supported");
	}

	long id = 0;
	try {
	    connect();
	    preparedStatement = getPrepareStatement(getIdQuery);
	    preparedStatement.setString(1, value);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		id = resultSet.getLong(1);
	    }

	    LOGGER.info("query: {}", preparedStatement);
	    LOGGER.info("id for {} = {}", value, id);
	} finally {
	    closeResources();
	}

	LOGGER.info("END");

	return id;
    }

    @Override
    public T get(long id) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("id: {}", id);

	if (StringUtils.isEmptyOrWhitespaceOnly(getByIdQuery) || reader == null) {
	    throw new SQLException("'get' sql query is not found. maybe this method not supported or 'reader' is null");
	}
	
	T item = null;
	try {
	    connect();
	    preparedStatement = getPrepareStatement(getByIdQuery);
	    preparedStatement.setLong(1, id);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    item = reader.read(resultSet);

	    LOGGER.info("query: {}", preparedStatement);
	    LOGGER.info("entity with id {}: {}", id, item);
	} finally {
	    closeResources();
	}

	return item;
    }

    @Override
    public List<T> getLimit(long start, long size) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("start: {}, size: {}", start, size);
	
	if (StringUtils.isEmptyOrWhitespaceOnly(getLimitQuery) || reader == null) {
	    throw new SQLException("'get limit' sql query is not found. maybe this method not supported or 'reader' is null");
	}
	List<T> list = null;
	try {
	    connect();
	    preparedStatement = getPrepareStatement(getLimitQuery);
	    preparedStatement.setLong(1, start);
	    preparedStatement.setLong(2, size);
	    list = getHelper(preparedStatement);
	} finally {
	    closeResources();
	}

	LOGGER.info("END");

	return list;
    }

    @Override
    public List<T> getAll() throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	if (StringUtils.isEmptyOrWhitespaceOnly(getAllQuery) || reader == null) {
	    throw new SQLException("'get all' sql query is not found. maybe this method not supported or 'reader' is null");
	}
	List<T> list = null;
	try {
	    connect();
	    preparedStatement = getPrepareStatement(getAllQuery);
	    list = getHelper(preparedStatement);
	} finally {
	    closeResources();
	}

	LOGGER.info("END");
	return list;
    }

    private final List<T> getHelper(PreparedStatement statement) throws ClassNotFoundException, IOException,
	    SQLException {
	List<T> list = new ArrayList<T>();
	T item = null;
	try {
	    connect();
	    resultSet = statement.executeQuery();
	    while ((item = reader.read(resultSet)) != null) {
		list.add(item);
	    }

	    LOGGER.info("query: {}", statement);
	} finally {
	    closeResources();
	}

	LOGGER.info("fetch {} entities", list.size());

	return list;
    }
}
