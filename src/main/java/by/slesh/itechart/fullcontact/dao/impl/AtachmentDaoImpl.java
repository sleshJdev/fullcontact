package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.AtachmentDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;

public class AtachmentDaoImpl extends EntityDao<AtachmentEntity> implements AtachmentDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(AtachmentDaoImpl.class);

    public AtachmentDaoImpl() {
    }

    public AtachmentDaoImpl(boolean isUseCurrentConnection,
	    boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
    }

    private static final String ADD_ATACHMENTS_TEMPLATE = 
	      "\n INSERT INTO atachments "
	    + "\n\t (atachments.contact_id, atachments.atachment_name, "
	    + "\n\t atachments.atachment_upload_date, atachments.atachment_comment) "
	    + "\n VALUES (?, ?, ?, ?)";

    @Override
    public long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<AtachmentEntity> atachments = contact.getAtachments();
	if (atachments == null || atachments.size() == 0) {
	    LOGGER.info("return: no atachments for add");
	    return 0;
	}
	long rowsAddeds = 0;
	try {
	    connect();
	    Iterator<AtachmentEntity> iterator = atachments.iterator();
	    long id = contact.getId();
	    while (iterator.hasNext()) {
		AtachmentEntity atachment = iterator.next();
		if (atachment.getId() == -1) {
		    preparedStatement = getPrepareStatement(ADD_ATACHMENTS_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
		    preparedStatement.setLong(1, id);
		    preparedStatement.setString(2, atachment.getName());
		    preparedStatement.setDate(3, atachment.getUploadDate());
		    preparedStatement.setString(4, atachment.getComment());
		    preparedStatement.executeUpdate();
		    resultSet = preparedStatement.getGeneratedKeys();
		    while (resultSet.next()) {
			atachment.setId(resultSet.getLong(1));
		    }
		    ++rowsAddeds;

		    LOGGER.info("{} query: {}", rowsAddeds, preparedStatement);

		    closeResource(null, preparedStatement);
		}
	    }
	} finally {
	    closeResources();
	}

	LOGGER.info("add {} atachments", rowsAddeds);
	LOGGER.info("END");

	return rowsAddeds;
    }
    
    private static final String DELETE_ATACHMENT_QUERY_TEMPLATE = 
	      "\n DELETE FROM atachments " 
	    + "\n WHERE atachments.atachment_id NOT IN ( ? ) AND contact_id = ?";
    
    @Override
    public long deleteRange(long contactId, long[] ids) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("ids: {}", Arrays.toString(ids));

	if (ids == null || ids.length == 0) {
	    LOGGER.info("return: no atachments for delete");
	    return 0;
	}
	long rowsDeleted = 0;
	try {
	    String inStatement = Arrays.toString(ids).replaceAll("[\\]\\[]", "");
	    connect();
	    preparedStatement = getPrepareStatement(DELETE_ATACHMENT_QUERY_TEMPLATE);
	    preparedStatement.setString(1, inStatement);
	    preparedStatement.setLong(2, contactId);
	    rowsDeleted = preparedStatement.getUpdateCount();
	    
	    LOGGER.info("query: {}", preparedStatement);
	} finally {
	    closeResources();
	}

	LOGGER.info("delete {} atachments", rowsDeleted);
	LOGGER.info("END");
	
	return rowsDeleted;
    }

    private static final String UPDATE_ATACHMENT_QUERY_TEMPLATE = 
	      "\n UPDATE atachments "
	    + "\n SET "
	    + "\n\t atachment_name = ?, "
	    + "\n\t atachment_upload_date = ?, "  
	    + "\n\t atachment_comment = ? "  
	    + "\n WHERE atachment_id = ?";

    @Override
    public long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<AtachmentEntity> atachments = contact.getAtachments();
	if (atachments == null || atachments.size() == 0) {
	    LOGGER.info("return: no atachments for update");
	    return 0;
	}
	long rowsUpdated = 0;
	try {
	    connect();
	    Iterator<AtachmentEntity> iterator = atachments.iterator();
	    while (iterator.hasNext()) {
		AtachmentEntity atachment = iterator.next();
		long id = atachment.getId();
		if (id != -1) {
		    preparedStatement = getPrepareStatement(UPDATE_ATACHMENT_QUERY_TEMPLATE);
		    preparedStatement.setString(1, atachment.getName());
		    preparedStatement.setDate(2, atachment.getUploadDate());
		    preparedStatement.setString(3, atachment.getComment());
		    preparedStatement.setLong(4, id);
		    rowsUpdated = preparedStatement.executeUpdate();
		    ++rowsUpdated;

		    LOGGER.info("{} query: {}", rowsUpdated, preparedStatement);

		    closeResource(null, preparedStatement);
		}
	    }
	} finally {
	    closeResources();
	}

	LOGGER.info("update " + rowsUpdated + " rows");
	LOGGER.info("END");

	return rowsUpdated;
    }
}
