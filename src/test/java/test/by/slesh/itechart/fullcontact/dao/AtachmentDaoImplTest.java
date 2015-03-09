package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.AtachmentDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.AtachmentDaoImpl;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class AtachmentDaoImplTest {
    @Test
    public void testGetAll() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<AtachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	List<AtachmentEntity> atachment = atachmentDao.getAll();
	assertNotNull(atachment);
    }

    @Test
    public void testGetById() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<AtachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	AtachmentEntity atachment = atachmentDao.get(1);
	assertNotNull(atachment);
    }

    @Test
    public void testAdd() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(new Long(8));
	
	AtachmentEntity atachment1 = new AtachmentEntity();
	atachment1.setId(null);
	atachment1.setContactId(contact.getId());
	atachment1.setName("addname1.txt");
	atachment1.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment1.setComment("add comment 1");

	AtachmentEntity atachment2 = new AtachmentEntity();
	atachment2.setId(null);
	atachment2.setContactId(contact.getId());
	atachment2.setName("addname2.txt");
	atachment2.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment2.setComment("add comment 2");

	List<AtachmentEntity> atachments = new ArrayList<AtachmentEntity>();
	atachments.add(atachment1);
	atachments.add(atachment2);
	long expectedQuantityInsert = atachments.size();

	contact.setAtachments(atachments);

	EntityDao<AtachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	long actualQuantityInsert = ((AtachmentDao) atachmentDao).add(contact);

	assertEquals("Not equals quantity INSERT rows and actual entity", expectedQuantityInsert, actualQuantityInsert);
    }

    @Test
    public void testDelete() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<AtachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, true);
	final long contactId = 15;
	final long[] ids = new long[] { 22, 23, 24, 25, 26 };
	final long expectedQuantityUpdate = ids.length;
	long actualQuantityDelete = atachmentDao.deleteRange(contactId, ids);

	assertEquals("Not equals quantity UPDATE rows and actual entity", expectedQuantityUpdate, actualQuantityDelete);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(new Long(1));

	AtachmentEntity atachment1 = new AtachmentEntity();
	atachment1.setContactId(contact.getId());
	atachment1.setId(new Long(1));
	atachment1.setName("updatename1.txt");
	atachment1.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment1.setComment("update comment 1");

	AtachmentEntity atachment2 = new AtachmentEntity();
	atachment2.setId(new Long(2));
	atachment2.setContactId(contact.getId());
	atachment2.setName("updatename2.txt");
	atachment2.setUploadDate((new Date(new java.util.Date().getTime())));
	atachment2.setComment("update comment 2");

	List<AtachmentEntity> atachments = new ArrayList<AtachmentEntity>();
	atachments.add(atachment1);
	atachments.add(atachment2);
	long expectedQuantityUpdate = atachments.size();

	contact.setAtachments(atachments);

	AtachmentDao atachmentDao = new AtachmentDaoImpl();
	long actualQuantityUpdate = atachmentDao.update(contact);

	assertEquals("Not equals quantity UPDATE rows and actual entity", expectedQuantityUpdate, actualQuantityUpdate);

    }

}
