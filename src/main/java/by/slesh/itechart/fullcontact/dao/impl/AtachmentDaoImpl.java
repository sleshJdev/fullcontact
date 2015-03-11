package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.AttachmentDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.DateUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class AtachmentDaoImpl extends EntityDao<AttachmentEntity> implements AttachmentDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(AtachmentDaoImpl.class);

    public static final String GET_QUERY_BODY =
	    "\n\t SELECT atachments.atachment_id, atachments.contact_id, atachments.atachment_name, "
          + "\n\t\t atachments.atachment_upload_date, atachments.atachment_comment ";
    
    public static final String GET_QUERY =
	    GET_QUERY_BODY
	  + "\n\t FROM atachments ";
	    
    public static final String GET_BY_ID_QUERY = 
	    GET_QUERY
          + "\n\t WHERE atachments.atachment_id = ? ";
    
    public static final String DELETE_RANGE_ATACHMENT_QUERY_TEMPLATE = 
	      "\n\t DELETE FROM atachments " 
	    + "\n\t WHERE atachments.atachment_id IN ( %s ) AND atachments.contact_id = ? ";
    
    public static final String DELETE_ATACHMENT_QUERY = 
	      "\n\t DELETE FROM atachments " 
	    + "\n\t WHERE atachments.atachment_id = ? ";
    
    
    public AtachmentDaoImpl() {
    }

    public AtachmentDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setDeleteRangeQuery(DELETE_RANGE_ATACHMENT_QUERY_TEMPLATE);
	setDeleteQuery(DELETE_ATACHMENT_QUERY);
	setGetByIdQuery(GET_BY_ID_QUERY);
	setGetAllQuery(GET_QUERY);
	setReader(DaoReadersContainer.ATACHMENTS_READER);
    }
    
    
    public static final String GET_ATTACHMENTS_OF_CONTACT = 
	    GET_QUERY
	 + "\n\t LEFT JOIN contacts ON atachments.contact_id = contacts.contact_id "
	 + "\n\t WHERE contacts.contact_id = ?";
    
    
    @Override
    public List<AttachmentEntity> getAttachmentsOfContact(Long contactId) throws SQLException, ClassNotFoundException,
	    IOException {
	LOGGER.info("BEGIN. contacId: {}", contactId);

	List<AttachmentEntity> atachments = null;
	try {
	    connect();
	    preparedStatement = getPrepareStatement(GET_ATTACHMENTS_OF_CONTACT);
	    preparedStatement.setLong(1, contactId);
	    resultSet = preparedStatement.executeQuery();

	    LOGGER.info("query: {}", preparedStatement);
	    
	    if (!resultSet.next()) {
		LOGGER.info("RETURN: result set is empty");
		return null;
	    }

	    if (atachments == null) {
		atachments = new ArrayList<AttachmentEntity>();
	    }
	    
	    AttachmentEntity attachmentEntity = null;
	    while ((attachmentEntity = (AttachmentEntity) DaoReadersContainer.ATACHMENTS_READER.read(resultSet)) != null) {
		atachments.add(attachmentEntity);
	    }
	} finally {
	    closeResources();
	}

	LOGGER.info("END. fetch {} atachmetns", atachments == null ? 0 : atachments.size());
	return atachments;
    }
    
    
    public static final String ADD_ATACHMENTS_TEMPLATE = 
	      "\n\t INSERT INTO atachments "
	    + "\n\t\t (atachments.contact_id, atachments.atachment_name, "
	    + "\n\t\t atachments.atachment_upload_date, atachments.atachment_comment) "
	    + "\n\t VALUES (?, ?, ?, ?)";
    
    @Override
    public Long add(AttachmentEntity atachment) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	if (atachment == null) {
	    LOGGER.info("RETURN: atachment is null");
	    return null;
	}
	try {
	    connect();
	    addHelper(atachment);
	} finally {
	    closeResources();
	}

	return atachment.getId();
    }

    @Override
    public Long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<AttachmentEntity> atachments = contact.getAtachments();
	if (atachments == null || atachments.size() == 0) {
	    LOGGER.info("RETURN: no atachments for add");
	    return null;
	}
	long rowsAddeds = 0;
	try {
	    connect();
	    Iterator<AttachmentEntity> iterator = atachments.iterator();
	    long id = contact.getId();
	    while (iterator.hasNext()) {
		AttachmentEntity atachment = iterator.next();
		atachment.setContactId(id);
		addHelper(atachment);
		++rowsAddeds;
	    }
	} finally {
	    closeResources();
	}

	LOGGER.info("add {} atachments", rowsAddeds);
	LOGGER.info("END");

	return rowsAddeds;
    }
    
    private void addHelper(AttachmentEntity atachment) throws SQLException {
	try {
	    if (atachment.getId() == null) {
		preparedStatement = getPrepareStatement(ADD_ATACHMENTS_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setLong(1, atachment.getContactId());
		preparedStatement.setString(2, atachment.getName());
		preparedStatement.setDate(3, DateUtil.getSqlDate());
		preparedStatement.setString(4, atachment.getComment());
		preparedStatement.executeUpdate();
		resultSet = preparedStatement.getGeneratedKeys();
		while (resultSet.next()) {
		    atachment.setId(resultSet.getLong(1));
		}

		LOGGER.info("query: {}", preparedStatement);
	    }
	} catch (ParseException e) {
	    throw new SQLException(e);
	} finally {
	    closeResource(null, preparedStatement);

	}
    }

    public static final String UPDATE_ATACHMENT_QUERY_TEMPLATE = 
	      "\n\t UPDATE atachments "
	    + "\n\t SET "
	    + "\n\t\t atachment_name = ?, "
	    + "\n\t\t atachment_upload_date = ?, "  
	    + "\n\t\t atachment_comment = ? "  
	    + "\n\t WHERE atachment_id = ?";

    @Override
    public Long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<AttachmentEntity> atachments = contact.getAtachments();
	if (atachments == null || atachments.size() == 0) {
	    LOGGER.info("RETURN: no atachments for update");
	    return null;
	}
	long rowsUpdated = 0;
	try {
	    connect();
	    Iterator<AttachmentEntity> iterator = atachments.iterator();
	    while (iterator.hasNext()) {
		AttachmentEntity atachment = iterator.next();
		Long id = atachment.getId();
		if (id != null) {
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
