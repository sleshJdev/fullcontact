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
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;
import by.slesh.itechart.fullcontact.settings.G;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class ContactDaoImplTest {
    @Test
    public void testGetNameByEmail() throws ClassNotFoundException, IOException, SQLException{
	final String name = "PankajFirst";
	final String email = "shorin-roman@yandex.by";
	ContactDao contactDao = (ContactDao) DaoFactory.getContactDao(true, true);
	String result = contactDao.getName(email);
	assertEquals(name, result);
    }
    
    @Test
    public void testGetContact() throws ClassNotFoundException, IOException, SQLException{
	final long id = G.MY_ID;
	ContactDao contactDao = (ContactDao) DaoFactory.getContactDao(true, true);
	ContactEntity contact = contactDao.getContact(id);
	assertNotNull("Contact is null", contact);
    }
    
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
	contactDao.delete(new Long(16));
	long quantityAfter = contactDao.count();
	assertEquals("Incorrent quantity rows", 1, (quantityBefore - quantityAfter));
    }

    @Test
    public void testAdd(){
	PhoneEntity phone1 = new PhoneEntity();
	phone1.setId(null);
	phone1.setValue("12-21null2-21");
	phone1.setType("Mobile");
	phone1.setComment("add comment 1");
	phone1.setCountryCode("acc1");
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

	AttachmentEntity atachment1 = new AttachmentEntity();
	atachment1.setId(null);
	atachment1.setName("addname1.txt");
	atachment1.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment1.setComment("add comment 1");

	AttachmentEntity atachment2 = new AttachmentEntity();
	atachment2.setId(null);
	atachment2.setName("addname2.txt");
	atachment2.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment2.setComment("add comment 2");

	List<AttachmentEntity> atachments = new ArrayList<AttachmentEntity>();
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
	try {
	    contactDao.add(contact);
	} catch (ClassNotFoundException | IOException | SQLException e) {
	    // TODO
	    e.printStackTrace();
	}
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, IOException, SQLException {
	PhoneEntity phone1 = new PhoneEntity();
	phone1.setId(null);
	phone1.setValue("12-21null2-21");
	phone1.setType("Mobile");
	phone1.setComment("add comment 1");
	phone1.setCountryCode("cc1");
	phone1.setOperatorCode("oc1");

	PhoneEntity phone2 = new PhoneEntity();
	phone2.setId(null);
	phone2.setValue("34-43-34-43");
	phone2.setType("Home");
	phone2.setComment("update comment 2");
	phone2.setCountryCode("cc2");
	phone2.setOperatorCode("oc2");

	List<PhoneEntity> phones = new ArrayList<PhoneEntity>();
	phones.add(phone1);
	phones.add(phone2);

	AttachmentEntity atachment1 = new AttachmentEntity();
	atachment1.setId(null);
	atachment1.setName("updatename1.txt");
	atachment1.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment1.setComment("update comment 1");

	AttachmentEntity atachment2 = new AttachmentEntity();
	atachment2.setId(null);
	atachment2.setName("updatename2.txt");
	atachment2.setUploadDate(new Date(new java.util.Date().getTime()));
	atachment2.setComment("update comment 2");

	List<AttachmentEntity> atachments = new ArrayList<AttachmentEntity>();
	atachments.add(atachment1);
	atachments.add(atachment2);
	
	ContactEntity contact = new ContactEntity();
	contact.setId(new Long(17));
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
	contact.setCountry("UpdateCC");
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
