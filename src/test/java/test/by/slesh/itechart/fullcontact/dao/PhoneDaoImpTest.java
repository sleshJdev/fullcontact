package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.PhoneDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;

public class PhoneDaoImpTest {
    @Test
    public void testGetById() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<PhoneEntity> phoneDao = DaoFactory.getPhoneDao(true, true);
	PhoneEntity phone = phoneDao.get(1);
	assertNotNull(phone);
    }

    @Test
    public void testAdd() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(new Long(8));

	PhoneEntity phone1 = new PhoneEntity();
	phone1.setId(null);
	phone1.setValue("12-21-12-21");
	phone1.setType("Mobile");
	phone1.setComment("add comment 1");
	phone1.setCountryCode("cc1");
	phone1.setOperatorCode("oc1");

	PhoneEntity phone2 = new PhoneEntity();
	phone2.setId(null);
	phone2.setValue("34-43-34-43");
	phone2.setType("Home");
	phone2.setComment("add comment 2");
	phone2.setCountryCode("cc2");
	phone2.setOperatorCode("oc2");

	List<PhoneEntity> phones = new ArrayList<PhoneEntity>();
	phones.add(phone1);
	phones.add(phone2);

	contact.setPhones(phones);

	long expectedQuantityInsert = phones.size();

	PhoneDao phoneDao = (PhoneDao) DaoFactory.getPhoneDao(true, true);
	long actualQuantityInsert = phoneDao.add(contact);

	assertEquals("Not equals quantity INSERT rows and actual entity", expectedQuantityInsert, actualQuantityInsert);
    }

    @Test
    public void testDelete() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<PhoneEntity> phoneDao = DaoFactory.getPhoneDao(true, true);
	final long[] ids = new long[] { 17, 18, 19 };
	final long expectedQuantityUpdate = ids.length;
	long actualQuantityDelete = phoneDao.deleteRange(1, ids);
	assertEquals("Not equals quantity DELETE rows and actual entity", expectedQuantityUpdate, actualQuantityDelete);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(new Long(1));

	PhoneEntity phone1 = new PhoneEntity();
	phone1.setId(new Long(1));
	phone1.setContactId(contact.getId());
	phone1.setValue("12-21-12-21");
	phone1.setType("Mobile");
	phone1.setComment("update comment 1");
	phone1.setCountryCode("cc1");
	phone1.setOperatorCode("oc1");

	PhoneEntity phone2 = new PhoneEntity();
	phone2.setId(new Long(2));
	phone2.setContactId(contact.getId());
	phone2.setValue("34-43-34-43");
	phone2.setType("Home");
	phone2.setComment("update comment 2");
	phone2.setCountryCode("cc2");
	phone2.setOperatorCode("oc2");

	List<PhoneEntity> phones = new ArrayList<PhoneEntity>();
	phones.add(phone1);
	phones.add(phone2);

	contact.setPhones(phones);

	long expectedQuantityUpdate = phones.size();

	PhoneDao phoneDao = (PhoneDao) DaoFactory.getPhoneDao(true, true);
	long actualQuantityUpdate = phoneDao.update(contact);

	assertEquals("Not equals quantity UPDATE rows and actual entity", expectedQuantityUpdate, actualQuantityUpdate);
    }
}
