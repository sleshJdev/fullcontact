package test.by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.impl.ManyToManyDao;
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;

public class ManyToManyDaoTest {
    @Test
    public void testDoLinkEmailAtachment() throws ClassNotFoundException, IOException, SQLException {
	final long emailId = 1;
	final long atachmentId = 1;
	ManyToManyDao.getInstance(true, true).doLinkEmailAtachment(emailId, atachmentId);
    }

    @Test
    public void testDoLinkEmailContact() throws ClassNotFoundException, IOException, SQLException {
	final long emailId = 1;
	final long contactId = 1;
	ManyToManyDao.getInstance(true, true).doLinkEmailContact(emailId, contactId);
    }

    @Test
    public void testGetAtachmentsOfEmail() throws ClassNotFoundException, SQLException, IOException {
	final long emailId = 1;
	List<AttachmentEntity> atachments = ManyToManyDao.getInstance(true, true).getAtachmentsOfEmail(emailId);
	assertNotNull(atachments);
    }

    @Test
    public void testGetReceiversOfEmail() throws ClassNotFoundException, SQLException, IOException {
	final long emailId = 1;
	List<ContactEntity> receivers = ManyToManyDao.getInstance(true, true).getReceiversOfEmail(emailId);
	assertNotNull(receivers);
    }
}
