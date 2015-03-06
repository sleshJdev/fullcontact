package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.AtachmentDao;
import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.PhoneDao;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.DaoProcessUtil;

/**
 * @author Eugene Putsykovich(slesh) Feb 13, 2015
 *
 *         Contact data access object interface implementation
 *         Table contact. Database fullcontact.
 */
public class ContactDaoImp extends EntityDao<ContactEntity> implements ContactDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContactDaoImp.class);

    private static final String ORDER_BY = 
	    "ORDER BY contacts.contact_id ASC ";
    
    private static final String GET_QUERY = 
	      "\n SELECT contacts.contact_id, first_name, last_name, middle_name, avatar_path, date_of_birth, avatar_path, "
	    + "\n\t sexes.sex_id, sexes.sex_value, "
	    + "\n\t nationalities.nationality_id, nationalities.nationality_value, "
	    + "\n\t family_status.family_status_id, family_status_value, "
	    + "\n\t web_site, email_address, current_employment, country, city, street, house, block, apartment, city_index, "
	    + "\n\t phones.phone_id, phones.phone_value, phones.phone_comment, phones.phone_country_code, phones.phone_operator_code, "
	    + "\n\t phones.phone_type_id, phones_types.phone_type_value, "
	    + "\n\t atachments.atachment_id, atachments.atachment_name, atachments.atachment_upload_date, atachments.atachment_comment "
	    + "\n FROM contacts "
	    + "\n LEFT JOIN nationalities ON contacts.nationality_id	= nationalities.nationality_id "
	    + "\n LEFT JOIN sexes 	  ON contacts.sex_id		= sexes.sex_id "
	    + "\n LEFT JOIN family_status ON contacts.family_status_id	= family_status.family_status_id "
            + "\n LEFT JOIN phones 	  ON contacts.contact_id	= phones.contact_id "
            + "\n LEFT JOIN phones_types  ON phones.phone_type_id 	= phones_types.phone_type_id "
            + "\n LEFT JOIN atachments 	  ON contacts.contact_id	= atachments.contact_id ";
  
  private static final String GET_LIMIT_QUERY = 
	      "\n SELECT contacts.contact_id, first_name, last_name, middle_name, avatar_path, date_of_birth, "
	    + "\n\t current_employment, country, city, street, house, block, apartment, city_index "
	    + "\n FROM contacts " 
	    + "\n " + ORDER_BY
	    + "\n LIMIT ?, ? ";

    private static final String GET_BY_ID_QUERY = 
	    GET_QUERY
          + " \n WHERE contacts.contact_id = ? \n " 
          + ORDER_BY;
    
    private static final String COUNT_QUERY = 
	    "SELECT COUNT(*) FROM contacts";
    
    private static final String DELETE_QUERY = 
	      "\n DELETE FROM contacts "
	    + "\n WHERE contact_id = ?";
    
    public ContactDaoImp() {
	this(true, true);
    }
    
    public ContactDaoImp(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setDeleteQuery(DELETE_QUERY);
	setCountQuery(COUNT_QUERY);
	setGetLimitQuery(GET_LIMIT_QUERY);
	setGetAllQuery(GET_QUERY);
	setGetQuery(GET_BY_ID_QUERY);
	setReader(Readers.LIMIT_CONTACT_READER);
    }
    
    @Override
    public long deleteRange(long contactId, long[] ids) throws ClassNotFoundException, IOException, SQLException {
	throw new SQLException("not supported this operation!");
    }
    
    @Override
    public ContactEntity get(long id) throws ClassNotFoundException, IOException, SQLException {
	setReader(Readers.FULL_CONTACT_READER);
	return super.get(id);
    }

    @Override
    public List<ContactEntity> getLimit(long start, long size) throws ClassNotFoundException, IOException, SQLException {
	setReader(Readers.LIMIT_CONTACT_READER);
	return super.getLimit(start, size);
    }

    @Override
    public List<ContactEntity> getAll() throws ClassNotFoundException, IOException, SQLException {
	setReader(Readers.FULL_CONTACT_READER);
	return super.getAll();
    }

    private static final String SET_AVATAR_PATH_QUERY = 
	      "\n UPDATE contacts "
	    + "\n SET avatar_path = ? "
	    + "\n WHERE contact_id = ? ";
    
    @Override
    public void setAvatar(long contactId, String path) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("contact id = {}, path = ", contactId, path);
	
	try{
	    connect();
	    preparedStatement = getPrepareStatement(SET_AVATAR_PATH_QUERY);
	    preparedStatement.setString(1, path);
	    preparedStatement.setLong(2, contactId);
	    preparedStatement.executeUpdate();
	}finally{
	    closeResources();
	}
	
	LOGGER.info("END");
    }

    private static final String GET_AVATAR_PATH_QUERY = 
	      "\n SELECT avatar_path "
	    + "\n FROM contacts "
	    + "\n WHERE contact_id = ?";
            
    @Override
    public String getAvatar(long contactId) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("contact id: {}", contactId);
	
	String path = null;
	try{
	    connect();
	    preparedStatement = getPrepareStatement(GET_AVATAR_PATH_QUERY);
	    preparedStatement.setLong(1, contactId);
	    resultSet = preparedStatement.executeQuery();
	    while(resultSet.next()){
		path = resultSet.getString(1);
	    }
	}finally{
	    closeResources();
	}
	
	LOGGER.info("path: {}", path);
	LOGGER.info("END");
	
	return path;
    }

    private static final String GET_EMAIL_QUERY = 
	     "\n SELECT email_address "
	   + "\n FROM contacts " 
	   + "\n WHERE contact_id = ?";

    @Override
    public String getEmail(long contactId) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("contact id = {}", contactId);

	String emailAdress = "";
	try {
	    connect();
	    preparedStatement = getPrepareStatement(GET_EMAIL_QUERY);
	    preparedStatement.setLong(1, contactId);
	    resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		emailAdress = resultSet.getString(1);
	    }

	    LOGGER.info("query: {}", preparedStatement );
	} finally {
	    closeResources();
	}

	LOGGER.info("email address: {}", emailAdress);
	LOGGER.info("END");

	return emailAdress;
    }

    private static final String ADD_CONTACT_QUERY = 
	      "\n INSERT INTO contacts"
	    + "\n\t (first_name, last_name, middle_name, date_of_birth, "
	    + "\n\t sex_id, nationality_id, family_status_id, "
	    + "\n\t web_site, email_address, current_employment, country, city, street, house, block, apartment, city_index) "
	    + "\n VALUES (?, ?, ?, ?,\n ?, ?, ?,\n ?, ?, ?, ?,\n ?, ?, ?, ?, ?, ?)";

    @Override
    public void add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	try {
	    connect();
	    long sexId = DaoFactory.getSexDao(true, false).getId(contact.getSex());
	    long nationalityId = DaoFactory.getNationalityDao(true, false).getId(contact.getNationality());
	    long familyStatusId = DaoFactory.getFamilyStatusDao(true, false).getId(contact.getFamilyStatus());
	    preparedStatement = getPrepareStatement(ADD_CONTACT_QUERY, Statement.RETURN_GENERATED_KEYS);
	    preparedStatement.setString(1, contact.getFirstName());
	    preparedStatement.setString(2, contact.getLastName());
	    preparedStatement.setString(3, contact.getMiddleName());
	    preparedStatement.setDate(4, contact.getDateOfBirth());
	    preparedStatement.setLong(5, sexId);
	    preparedStatement.setLong(6, nationalityId);
	    preparedStatement.setLong(7, familyStatusId);
	    preparedStatement.setString(8, contact.getWebSite());
	    preparedStatement.setString(9, contact.getEmailAddress());
	    preparedStatement.setString(10, contact.getCurrentEmployment());
	    preparedStatement.setString(11, contact.getCountry());
	    preparedStatement.setString(12, contact.getCity());
	    preparedStatement.setString(13, contact.getStreet());
	    preparedStatement.setString(14, contact.getHouse());
	    preparedStatement.setString(15, contact.getBlock());
	    preparedStatement.setString(16, contact.getApartment());
	    preparedStatement.setString(17, contact.getCityIndex());

	    LOGGER.info("query: {}", preparedStatement);

	    preparedStatement.executeUpdate();
	    resultSet = preparedStatement.getGeneratedKeys();
	    while (resultSet.next()) {
		contact.setId(resultSet.getLong(1));
	    }
	    ((PhoneDao) DaoFactory.getPhoneDao(true, false)).add(contact);
	    ((AtachmentDao) DaoFactory.getAtachmentDao(true, false)).add(contact);

	    LOGGER.info("add follow contact {}", contact);
	} finally {
	    closeResources();
	}

	LOGGER.info("END");
    }

    private static final String SEARCH_WHEN_TEMPLATE = 
	    "\n\t %s  LIKE '%s' OR";

    private static final String SEARCH_QUERY_TEMPLATE = GET_QUERY
	    + "\nWHERE\n( %s \n)";

    @Override
    public List<ContactEntity> search(ContactEntity contact) 
	    throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	String q = "";// when statement
	String f = "";// field name

	f = contact.getFirstName();
	q = DaoProcessUtil.processToken(q, f, "contacts.first_name",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getMiddleName();
	q = DaoProcessUtil.processToken(q, f, "contacts.middle_name",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getLastName();
	q = DaoProcessUtil.processToken(q, f, "contacts.last_name",
		SEARCH_WHEN_TEMPLATE);

//	f = contact.getDateOfBirth();
//	q = DataProcessHelper.processToken(q, f, "contacts.date_of_birth",
//		SEARCH_WHEN_TEMPLATE);

	f = contact.getSex();
	q = DaoProcessUtil.processToken(q, f, "contacts.sex",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getFamilyStatus();
	q = DaoProcessUtil.processToken(q, f, "contacts.family_status",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getWebSite();
	q = DaoProcessUtil.processToken(q, f, "contacts.web_site",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getEmailAddress();
	q = DaoProcessUtil.processToken(q, f, "contacts.email_address",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getCurrentEmployment();
	q = DaoProcessUtil.processToken(q, f, "contacts.current_employment",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getCountry();
	q = DaoProcessUtil.processToken(q, f, "contacts.country",
		SEARCH_WHEN_TEMPLATE);

	f = contact.getCity();
	q = DaoProcessUtil.processToken(q, f, "contacts.city",
		SEARCH_WHEN_TEMPLATE);

//	f = contact.getAddress();
//	q = DataProcessHelper.processToken(q, f, "contacts.address",
//		SEARCH_WHEN_TEMPLATE);

	f = contact.getCityIndex();
	q = DaoProcessUtil.processToken(q, f, "contacts.city_index",
		SEARCH_WHEN_TEMPLATE);

	q = q.substring(0, q.lastIndexOf("OR"));

	String query = String.format(SEARCH_QUERY_TEMPLATE, q);
	
	LOGGER.info("query: " + query);

	setReader(Readers.FULL_CONTACT_READER);
	List<ContactEntity> contacts = super.getAll();

	LOGGER.info("found " + contacts.size() + " contacts");
	LOGGER.info("END");

	return contacts;
    }

    private static final String UPDATE_QUERY = 
	    "\n UPDATE contacts "
	  + "\n SET "
	  + "\n\t first_name = ?, last_name = ?, middle_name = ?, date_of_birth = ?, "
	  + "\n\t sex_id = ?, nationality_id = ?, family_status_id = ?, "
	  + "\n\t web_site = ?, email_address = ?, current_employment = ?, country = ?, city = ?, "
	  + "\n\t street = ?, house = ?, block = ?, apartment = ?, city_index = ? "
	  + "\n WHERE contact_id = ?";

    @Override
    public void update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	try {
	    connect();
	    long sexId = DaoFactory.getSexDao(true, false).getId(contact.getSex());
	    long nationalityId = DaoFactory.getNationalityDao(true, false).getId(contact.getNationality());
	    long familyStatusId = DaoFactory.getFamilyStatusDao(true, false).getId(contact.getFamilyStatus());
	    preparedStatement = getPrepareStatement(UPDATE_QUERY);
	    preparedStatement.setString(1, contact.getFirstName());
	    preparedStatement.setString(2, contact.getLastName());
	    preparedStatement.setString(3, contact.getMiddleName());
	    preparedStatement.setDate(4, contact.getDateOfBirth());
	    preparedStatement.setLong(5, sexId);
	    preparedStatement.setLong(6, nationalityId);
	    preparedStatement.setLong(7, familyStatusId);
	    preparedStatement.setString(8, contact.getWebSite());
	    preparedStatement.setString(9, contact.getEmailAddress());
	    preparedStatement.setString(10, contact.getCurrentEmployment());
	    preparedStatement.setString(11, contact.getCountry());
	    preparedStatement.setString(12, contact.getCity());
	    preparedStatement.setString(13, contact.getStreet());
	    preparedStatement.setString(14, contact.getHouse());
	    preparedStatement.setString(15, contact.getBlock());
	    preparedStatement.setString(16, contact.getApartment());
	    preparedStatement.setString(17, contact.getCityIndex());
	    preparedStatement.setLong(18, contact.getId());
	    preparedStatement.executeUpdate();

	    LOGGER.info("query: {}", preparedStatement);

	    PhoneDao phoneDao = (PhoneDao) DaoFactory.getPhoneDao(true, false);
	    phoneDao.update(contact);
	    phoneDao.add(contact);

	    AtachmentDao atachmentDao = (AtachmentDao) DaoFactory.getAtachmentDao(true, false);
	    atachmentDao.update(contact);
	    atachmentDao.add(contact);
	} finally {
	    closeResources();
	}

	LOGGER.info("END");
    }
}
