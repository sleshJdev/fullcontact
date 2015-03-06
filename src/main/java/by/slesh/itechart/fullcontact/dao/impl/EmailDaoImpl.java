package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import by.slesh.itechart.fullcontact.dao.EmailDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.EmailEntity;

public class EmailDaoImpl extends EntityDao<EmailEntity> implements EmailDao {
    private final static Logger LOGGER = Logger.getLogger(EmailDaoImpl.class);

    public EmailDaoImpl() {
    }

    public EmailDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
    }
    
    @Override
    public long deleteRange(long contactId, long[] ids) throws ClassNotFoundException, IOException, SQLException {
	throw new SQLException("not supported this operation!");
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
	} finally {
	    closeResources();
	}

	LOGGER.info("END");

	return id;
    }

    @Override
    public long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	// TODO
	return 0;
    }

    public List<EmailEntity> gets(long contactId) throws ClassNotFoundException, IOException, SQLException {
	// TODO
	return null;
    }
}
