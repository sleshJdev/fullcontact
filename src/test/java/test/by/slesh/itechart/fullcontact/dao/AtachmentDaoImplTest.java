package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.AttachmentDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.AtachmentDaoImpl;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class AtachmentDaoImplTest {
    @Test
    public void testGetAll() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<AttachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	List<AttachmentEntity> atachment = atachmentDao.getAll();
	assertNotNull(atachment);
    }

    @Test
    public void testGetById() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<AttachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	AttachmentEntity atachment = atachmentDao.get(new Long(1));
	assertNotNull(atachment);
    }

    @Test
    public void testAdd() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(new Long(8));
	
	AttachmentEntity atachment1 = new AttachmentEntity();
	atachment1.setId(null);
	atachment1.setContactId(contact.getId());
	atachment1.setName("addname1.txt");
	atachment1.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment1.setComment("add comment 1");

	AttachmentEntity atachment2 = new AttachmentEntity();
	atachment2.setId(null);
	atachment2.setContactId(contact.getId());
	atachment2.setName("addname2.txt");
	atachment2.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment2.setComment("add comment 2");

	List<AttachmentEntity> atachments = new ArrayList<AttachmentEntity>();
	atachments.add(atachment1);
	atachments.add(atachment2);
	long expectedQuantityInsert = atachments.size();

	contact.setAtachments(atachments);

	EntityDao<AttachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	long actualQuantityInsert = ((AttachmentDao) atachmentDao).add(contact);

	assertEquals("Not equals quantity INSERT rows and actual entity", expectedQuantityInsert, actualQuantityInsert);
    }

    @Test
    public void testDelete() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<AttachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	final Long contactId = new Long(15);
	final Long[] ids = new Long[] { new Long(22), new Long(23), new Long(24), new Long(25), new Long(26) };
	final long expectedQuantityUpdate = ids.length;
	long actualQuantityDelete = atachmentDao.deleteRange(contactId, ids);

	assertEquals("Not equals quantity UPDATE rows and actual entity", expectedQuantityUpdate, actualQuantityDelete);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(new Long(1));

	AttachmentEntity atachment1 = new AttachmentEntity();
	atachment1.setContactId(contact.getId());
	atachment1.setId(new Long(1));
	atachment1.setName("updatename1.txt");
	atachment1.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment1.setComment("update comment 1");

	AttachmentEntity atachment2 = new AttachmentEntity();
	atachment2.setId(new Long(2));
	atachment2.setContactId(contact.getId());
	atachment2.setName("updatename2.txt");
	atachment2.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment2.setComment("update comment 2");

	List<AttachmentEntity> atachments = new ArrayList<AttachmentEntity>();
	atachments.add(atachment1);
	atachments.add(atachment2);
	long expectedQuantityUpdate = atachments.size();

	contact.setAtachments(atachments);

	AttachmentDao atachmentDao = new AtachmentDaoImpl();
	long actualQuantityUpdate = atachmentDao.update(contact);

	assertEquals("Not equals quantity UPDATE rows and actual entity", expectedQuantityUpdate, actualQuantityUpdate);

    }

}
