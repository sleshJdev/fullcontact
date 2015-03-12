package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import by.slesh.itechart.fullcontact.dao.EmailDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.EmailEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class EmailDaoImpl extends EntityDao<EmailEntity> implements EmailDao {
    private final static Logger LOGGER = Logger.getLogger(EmailDaoImpl.class);

    private static final String DELETE_QUERY = 
	    "\n\t DELETE FROM emails "
	  + "\n\t WHERE email_id IN ( %s ) AND contact_id_sender = ?";
    
    private static final String GET_QUERY = 
	    "\n\t SELECT emails.email_id, emails.contact_id_sender, emails.email_subject, emails.email_text, emails.email_date_send "
          + "\n\t FROM emails ";

    private static final String GET_BY_ID_QUERY = 
	       GET_QUERY 
	    + "\n\t WHERE email_id = ?";
    
    private static final String DELETE_RANGE_EMAILS_QUERY = 
	      "\n\t DELETE FROM emails " 
	    + "\n\t WHERE email_id IN ( %s ) AND contact_id_sender = ?";
    
    public EmailDaoImpl() {
	this(true, true);
    }
    
    public EmailDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setDeleteRangeQuery(DELETE_RANGE_EMAILS_QUERY);
	setDeleteQuery(DELETE_QUERY);
	setGetByIdQuery(GET_BY_ID_QUERY);
	setGetAllQuery(GET_QUERY);
	setReader(DaoReadersContainer.EMAIL_READER);
    }
    
    private static final String GET_EMAILS_OF_CONTACT = 
	    GET_QUERY
	 + "\n\t WHERE emails.contact_id_sender = %s";
    
    @Override
    public List<EmailEntity> getEmailsOfContact(Long contactId) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	
	if(contactId == null){
	    return null;
	}
	
	setGetAllQuery(String.format(GET_EMAILS_OF_CONTACT, contactId));
	List<EmailEntity> emails = super.getAll();
	setGetAllQuery(GET_QUERY);
	
	LOGGER.info("END");
	return emails;
    }

    private static final String ADD_EMAIL_QUERY = 
	    "\n INSERT INTO emails "
	  + "\n\t (contact_id_sender, email_subject, email_text, email_date_send) "
	  + "\n VALUES (?, ?, ?, ?)";
    
    @Override
    public long add(EmailEntity email) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	long id = 0;
	try {
	    connect();
	    preparedStatement = getPrepareStatement(ADD_EMAIL_QUERY, Statement.RETURN_GENERATED_KEYS);
	    preparedStatement.setLong(1, email.getContactIdSender());
	    preparedStatement.setString(2, email.getSubject());
	    preparedStatement.setString(3, email.getText());
	    preparedStatement.setDate(4, email.getSendDate());
	    preparedStatement.execute();
	    resultSet = preparedStatement.getGeneratedKeys();
	    while (resultSet.next()) {
		id = resultSet.getLong(1);
	    }
	} catch(SQLException e){
	    rollback();
	} finally {
	    closeResources();
	}

	LOGGER.info("END");

	return id;
    }

    @Override
    public long update(EmailEntity email) throws ClassNotFoundException, IOException, SQLException {
	// TODO
	return 0;
    }
}
