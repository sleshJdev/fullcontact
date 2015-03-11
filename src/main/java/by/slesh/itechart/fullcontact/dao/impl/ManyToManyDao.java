package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

import by.slesh.itechart.fullcontact.dao.AbstractDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReader;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.Entity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public final class ManyToManyDao extends AbstractDao{
    private final static Logger LOGGER = LoggerFactory.getLogger(ManyToManyDao.class);
    
    private static ManyToManyDao instance;
    
    private ManyToManyDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork){
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
    }
    
    public static ManyToManyDao getInstance() {
	return getInstance(true, true);
    }

    public static ManyToManyDao getInstance(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	if(instance == null){
	    instance = new ManyToManyDao(isUseCurrentConnection,isCloseConnectionAfterWork);
	}
	instance.setUseCurrentConnection(isUseCurrentConnection);
	instance.setCloseConnectionAfterWork(isCloseConnectionAfterWork);
	
	return instance;
    }
    
    private final String LINK_EMAIL_CONTACT_QUERY = 
	      "\n\t INSERT INTO emails_receivers "
	    + "\n\t\t (email_id, contact_id) "
	    + "\n\t VALUES (?, ?)";
    
    public void doLinkEmailContact(long emailId, long contactId) throws ClassNotFoundException, IOException, SQLException{
	LOGGER.info("BEGIN");
	
	link(LINK_EMAIL_CONTACT_QUERY, emailId, contactId);
    
	LOGGER.info("END");
    }
    
    private final String LINK_EMAIL_ATACHMENT_QUERY = 
	      "\n\t INSERT INTO emails_atachments "
	    + "\n\t\t (email_id, atachment_id) "
	    + "\n\t VALUES (?, ?)";
    
    public void doLinkEmailAtachment(Long emailId, Long atachmentId) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	
	link(LINK_EMAIL_ATACHMENT_QUERY, emailId, atachmentId);
    
	LOGGER.info("END");
    }

    private void link(String query, Long key1, Long key2) throws SQLException, ClassNotFoundException, IOException {
	LOGGER.info("key1: {}, key2: {}", key1, key2);
	
	try {
	    connect();
	    preparedStatement = getPrepareStatement(query);
	    preparedStatement.setLong(1, key1);
	    preparedStatement.setLong(2, key2);
	    preparedStatement.executeUpdate();

	    LOGGER.info("query: {}", query);
	} finally {
	    closeResources();
	}
    }
    
    private static final String GET_ATACHMENTS_OF_EMAIL_QUERY = 
	    AtachmentDaoImpl.GET_QUERY_BODY
	  + "FROM emails_atachments "
	  + "LEFT JOIN atachments ON emails_atachments.atachment_id = atachments.atachment_id "
	  + "WHERE email_id = ?";
    
    public List<AttachmentEntity> getAtachmentsOfEmail(Long emailId) throws ClassNotFoundException, SQLException, IOException {
	LOGGER.info("BEGIN");
	return new SimpleReader<AttachmentEntity>().get(GET_ATACHMENTS_OF_EMAIL_QUERY, DaoReadersContainer.ATACHMENTS_READER, emailId);
    }
    
    private static final String GET_RECEIVERS_OF_EMAIL_QUERY = 
	    ContactDaoImp.GET_LIMIT_QUERY_BODY
	  + "\n\t LEFT JOIN emails_receivers ON emails_receivers.contact_id = contacts.contact_id "
	  + "\n\t WHERE emails_receivers.email_id = ?";
    
    public List<ContactEntity> getReceiversOfEmail(Long emailId) throws SQLException, ClassNotFoundException, IOException {
	LOGGER.info("BEGIN");
	return new SimpleReader<ContactEntity>().get(GET_RECEIVERS_OF_EMAIL_QUERY, DaoReadersContainer.LIMIT_CONTACT_READER, emailId);
    }

    class SimpleReader<T extends Entity> {
	@SuppressWarnings("unchecked")
	private List<T> get(String query, DaoReader<Entity> limitContactReader, Long id) throws SQLException, ClassNotFoundException, IOException {
	    LOGGER.info("id: {}", id);

	    if (StringUtils.isEmptyOrWhitespaceOnly(query) || limitContactReader == null) {
		throw new SQLException(
			"'get' sql query is not found. maybe this method not supported or 'reader' is null");
	    }

	    List<T> entities = null;
	    try {
		connect();
		preparedStatement = getPrepareStatement(query);
		preparedStatement.setLong(1, id);

		LOGGER.info("query: {}", preparedStatement);

		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.next()) {
		    LOGGER.info("RETURN: result set is empty");
		    return null;
		}
		
		T item = null;
		if (entities == null) {
		    entities = new ArrayList<T>();
		}
		while ((item = (T) limitContactReader.read(resultSet)) != null) {
		    entities.add(item);
		}

		LOGGER.info("fetch {} entity", entities.size());
	    } finally {
		closeResources();
	    }

	    return entities;
	}
    }
}
