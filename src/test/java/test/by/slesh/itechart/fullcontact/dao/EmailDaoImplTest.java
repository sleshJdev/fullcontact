package test.by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

public class EmailDaoImplTest {

    @Test
    public void testAdd() throws ClassNotFoundException, IOException, SQLException {
	// Contact contact = new Contact();
	// contact.setId(1);
	//
	// Email email1 = new Email();
	// email1.setContactId(contact.getId());
	// email1.setValue("test11@mail.com");
	//
	// Email email2 = new Email();
	// email2.setContactId(contact.getId());
	// email2.setValue("test22@mail.com");
	//
	// List<Email> emails = new ArrayList<Email>();
	// emails.add(email1);
	// emails.add(email2);
	// long expectedQuantityInsert = emails.size();
	//
	// contact.setEmails(emails);
	//
	// EmailDao emailDao = new EmailDaoImpl();
	// long actualQuantityInsert = emailDao.add(contact);
	//
	// assertEquals("Not equals quantity INSERT rows and actual entity",
	// expectedQuantityInsert, actualQuantityInsert);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException {
	// Contact contact = new Contact();
	// contact.setId(1);
	//
	// Email email1 = new Email();
	// email1.setId(5);
	// email1.setContactId(contact.getId());
	// email1.setValue("test51@mail.com");
	//
	// Email email2 = new Email();
	// email2.setId(4);
	// email2.setContactId(contact.getId());
	// email2.setValue("test41@mail.com");
	//
	// List<Email> emails = new ArrayList<Email>();
	// emails.add(email1);
	// emails.add(email2);
	// long checkQuantitu = emails.size();
	//
	// contact.setEmails(emails);
	//
	// EmailDao emailDao = new EmailDaoImpl();
	// long rowsAdded = emailDao.update(contact);
	//
	// assertEquals("Not equals quantity UPDATED rows and actual entity",
	// checkQuantitu, rowsAdded);
    }
}
