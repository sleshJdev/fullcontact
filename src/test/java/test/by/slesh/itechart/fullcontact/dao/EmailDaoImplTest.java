package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.EmailDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.EmailEntity;
import by.slesh.itechart.fullcontact.settings.G;
import by.slesh.itechart.fullcontact.util.DateUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class EmailDaoImplTest {
    @Test
    public void testGetEmailOfContact() throws ClassNotFoundException, IOException, SQLException {
	final long contactId = 1;
	List<EmailEntity> emails = ((EmailDao) DaoFactory.getEmailDao(true, true)).getEmailsOfContact(contactId);
	assertNotNull(emails);
    }

    @Test
    public void testGetAll() throws ClassNotFoundException, IOException, SQLException {
	List<EmailEntity> emails = null;
	emails = DaoFactory.getEmailDao(true, true).getAll();
	assertNotNull(emails);
    }

    @Test
    public void testGetById() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<EmailEntity> emailDao = DaoFactory.getEmailDao(true, true);
	EmailEntity email = emailDao.get(new Long(2));
	assertNotNull(email);
    }

    @Test
    public void testAdd() throws ClassNotFoundException, IOException, SQLException, ParseException {
	EmailEntity email = new EmailEntity();
	email.setContactIdSender(G.MY_ID);
	email.setSubject("Test Subject 1");
	email.setText("Test Block Text 1");
	email.setSendDate(DateUtil.getSqlDate());

	EntityDao<EmailEntity> emailDao = DaoFactory.getEmailDao(true, true);
	long id = ((EmailDao) emailDao).add(email);

	assertNotNull(id);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException, ParseException {
	EmailEntity email = new EmailEntity();
	email.setId(new Long(1));
	email.setSubject("Update Subject 1");
	email.setText("Update Block Text 1");
	email.setSendDate(DateUtil.getSqlDate());

	EntityDao<EmailEntity> emailDao = DaoFactory.getEmailDao(true, true);
	long id = ((EmailDao) emailDao).update(email);
	assertNotNull(id);
    }
}
