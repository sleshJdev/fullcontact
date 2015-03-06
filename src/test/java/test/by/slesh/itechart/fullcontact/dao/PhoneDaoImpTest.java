package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.assertEquals;

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
    public void testAdd() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(8);

	PhoneEntity phone1 = new PhoneEntity();
	phone1.setId(-1);
	phone1.setValue("12-21-12-21");
	phone1.setType("Mobile");
	phone1.setComment("add comment 1");
	phone1.setCountryCode("add cc 1");
	phone1.setOperatorCode("add oc 1");

	PhoneEntity phone2 = new PhoneEntity();
	phone2.setId(-1);
	phone2.setValue("34-43-34-43");
	phone2.setType("Home");
	phone2.setComment("add comment 2");
	phone2.setCountryCode("add cc 2");
	phone2.setOperatorCode("add oc 2");

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
	EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
	final long contactId = 2;
	contactDao.delete(contactId);
	final long[] ids = new long[] { -1 };
	final long expectedQuantityUpdate = -1;
	long actualQuantityDelete = phoneDao.deleteRange(2, ids);
	
	assertEquals("Not equals quantity UPDATE rows and actual entity", expectedQuantityUpdate, actualQuantityDelete);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = new ContactEntity();
	contact.setId(1);

	PhoneEntity phone1 = new PhoneEntity();
	phone1.setId(1);
	phone1.setContactId(contact.getId());
	phone1.setValue("12-21-12-21");
	phone1.setType("Mobile");
	phone1.setComment("update comment 1");
	phone1.setCountryCode("up cc 1");
	phone1.setOperatorCode("up oc 1");

	PhoneEntity phone2 = new PhoneEntity();
	phone2.setId(2);
	phone2.setContactId(contact.getId());
	phone2.setValue("34-43-34-43");
	phone2.setType("Home");
	phone2.setComment("update comment 2");
	phone2.setCountryCode("up cc 2");
	phone2.setOperatorCode("up oc 2");

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
