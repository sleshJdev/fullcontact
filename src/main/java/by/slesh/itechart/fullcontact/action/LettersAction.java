package by.slesh.itechart.fullcontact.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EmailDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.ContactDaoImp;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.dao.impl.ManyToManyDao;
import by.slesh.itechart.fullcontact.db.JdbcConnector;
import by.slesh.itechart.fullcontact.db.local.Database;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.EmailEntity;
import by.slesh.itechart.fullcontact.settings.G;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class LettersAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContactDaoImp.class);
    private final String SUCCESS = "Your Letters. Today %s";
    private final String PROBLEM = "Some Problem Occured During Fetch Your Emails !";

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	try {
	    EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, false);
	    ContactEntity sender = ((ContactDao) contactDao).getContact(G.MY_ID);

	    EntityDao<EmailEntity> emailDao = DaoFactory.getEmailDao(true, false);
	    List<EmailEntity> emails = ((EmailDao) emailDao).getEmailsOfContact(G.MY_ID);

	    if (emails != null) {
		for (EmailEntity email : emails) {
		    // no close connection after work!
		    List<ContactEntity> receivers = ManyToManyDao.getInstance(true, false).getReceiversOfEmail( email.getId());
		    // no close connection after work!
		    List<AtachmentEntity> atachments = ManyToManyDao.getInstance(true, false).getAtachmentsOfEmail( email.getId());
		    email.setContactIdSender(sender.getId());
		    email.setSender(sender);
		    if (receivers != null) {
			email.setReceivers(receivers);
		    }
		    if (atachments != null) {
			email.setAtachments(atachments);
		    }
		}
	    }
	    getRequest().setAttribute("emails", emails);
	    getRequest().setAttribute("status", String.format(SUCCESS, emails == null ? 0 : emails.size()));
	} catch (ClassNotFoundException | SQLException e) {
	    getRequest().setAttribute("status", PROBLEM);
	    throw new ServletException(e);
	} finally {
	    try {
		JdbcConnector.close();// close connection
	    } catch (SQLException e) {
		getRequest().setAttribute("status", PROBLEM);
		throw new ServletException(e);
	    }
	}
	getRequest().setAttribute("contactsBirthdayMans", Database.getBirthdayMans());
	getRequest().setAttribute("content", "letters-content.jsp");
	getRequest().setAttribute("title", "Notifications Page");
	getDispatcher().forward(getRequest(), getResponse());

	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	super.init(request, response);
    }
}
