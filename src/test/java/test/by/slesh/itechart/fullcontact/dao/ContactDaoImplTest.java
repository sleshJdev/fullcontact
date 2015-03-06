package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.Getable;
import by.slesh.itechart.fullcontact.dao.impl.ContactDaoImp;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;

public class ContactDaoImplTest {
    @Test
    public void testGetAll() throws ClassNotFoundException, IOException, SQLException {
	Getable<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
	List<ContactEntity> contacts = contactDao.getAll();
	assertNotNull("Contacts list extract failed", contacts);
    }

    @Test
    public void testGetLimit() throws ClassNotFoundException, IOException, SQLException {
	Getable<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
	List<ContactEntity> contacts = contactDao.getLimit(0, 2);
	assertNotNull("Contacts limit list extract failed", contacts);
    }

    @Test
    public void testCount() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
	long quantity = contactDao.count();
	assertNotNull("Incorrent quantity rows", quantity);
    }

    @Test
    public void testDelete() throws ClassNotFoundException, IOException, SQLException {
	EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
	long quantityBefore = contactDao.count();
	contactDao.delete(1);
	long quantityAfter = contactDao.count();
	assertEquals("Incorrent quantity rows", 1, (quantityBefore - quantityAfter));
    }

    @Test
    public void testAdd() throws ClassNotFoundException, IOException, SQLException {
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

	AtachmentEntity atachment1 = new AtachmentEntity();
	atachment1.setId(-1);
	atachment1.setName("addname1.txt");
	atachment1.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment1.setComment("add comment 1");

	AtachmentEntity atachment2 = new AtachmentEntity();
	atachment2.setId(-1);
	atachment2.setName("addname2.txt");
	atachment2.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment2.setComment("add comment 2");

	List<AtachmentEntity> atachments = new ArrayList<AtachmentEntity>();
	atachments.add(atachment1);
	atachments.add(atachment2);

	ContactEntity contact = new ContactEntity();
	contact.setFirstName("Test1First");
	contact.setLastName("Test1Last");
	contact.setMiddleName("Test1Middle");
	contact.setDateOfBirth(new Date(new java.util.Date().getTime()));
	contact.setSex("Male");
	contact.setNationality("Belarus");
	contact.setFamilyStatus("Divorced");
	contact.setCurrentEmployment("Test1CureentEmployment");
	contact.setWebSite("test1@mail.ru");
	contact.setEmailAddress("test1@mail.ru");
	contact.setPhones(phones);
	contact.setAtachments(atachments);
	contact.setCountry("Test1Country");
	contact.setCity("Test1City");
	contact.setStreet("Street1");
	contact.setHouse("13Houes");
	contact.setBlock("Block1");
	contact.setApartment("Apa12321");
	contact.setCityIndex("123490");

	ContactDao contactDao = (ContactDao) DaoFactory.getContactDao(true, true);
	contactDao.add(contact);
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException {
	PhoneEntity phone1 = new PhoneEntity();
	phone1.setId(-1);
	phone1.setValue("12-21-12-21");
	phone1.setType("Mobile");
	phone1.setComment("add comment 1");
	phone1.setCountryCode("update cc 1");
	phone1.setOperatorCode("update oc 1");

	PhoneEntity phone2 = new PhoneEntity();
	phone2.setId(-1);
	phone2.setValue("34-43-34-43");
	phone2.setType("Home");
	phone2.setComment("update comment 2");
	phone2.setCountryCode("update cc 2");
	phone2.setOperatorCode("update oc 2");

	List<PhoneEntity> phones = new ArrayList<PhoneEntity>();
	phones.add(phone1);
	phones.add(phone2);

	AtachmentEntity atachment1 = new AtachmentEntity();
	atachment1.setId(-1);
	atachment1.setName("updatename1.txt");
	atachment1.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment1.setComment("update comment 1");

	AtachmentEntity atachment2 = new AtachmentEntity();
	atachment2.setId(-1);
	atachment2.setName("updatename2.txt");
	atachment2.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment2.setComment("update comment 2");

	List<AtachmentEntity> atachments = new ArrayList<AtachmentEntity>();
	atachments.add(atachment1);
	atachments.add(atachment2);
	
	ContactEntity contact = new ContactEntity();
	contact.setId(3);
	contact.setFirstName("UpdateFirst");
	contact.setLastName("UpdateLast");
	contact.setMiddleName("UpdateMiddle");
	contact.setDateOfBirth(new Date(new java.util.Date().getTime()));
	contact.setSex("Male");
	contact.setNationality("Belarus");
	contact.setFamilyStatus("Divorced");
	contact.setCurrentEmployment("UpdateCureentEmployment");
	contact.setWebSite("Update@mail.ru");
	contact.setEmailAddress("Update@mail.ru");
	contact.setPhones(phones);
	contact.setAtachments(atachments);
	contact.setCountry("UpdateCountry");
	contact.setCity("UpdateCity");
	contact.setStreet("Street1");
	contact.setHouse("13Houes");
	contact.setBlock("Block1");
	contact.setApartment("Apa12321");
	contact.setCityIndex("123490");

	ContactDao contactDao = new ContactDaoImp();
	contactDao.update(contact);
    }
}
