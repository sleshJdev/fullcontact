package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.AtachmentDao;
import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.PhoneDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReader;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.Entity;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Feb 13, 2015
 *
 *         Contact data access object interface implementation
 *         Table contact. Database fullcontact.
 */
public class ContactDaoImp extends EntityDao<ContactEntity> implements ContactDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContactDaoImp.class);

    public static final String ORDER_BY = 
	    "\n\t ORDER BY contacts.contact_id ASC ";
    
    public static final String GET_QUERY = 
	      "\n\t SELECT "
	    + "\n\t\t contacts.contact_id, first_name, last_name, middle_name, avatar_path, date_of_birth,web_site, email_address, current_employment, country, city, street, house, block, apartment, city_index, "
	    + "\n\t\t sexes.sex_id, sexes.sex_value, "
	    + "\n\t\t nationalities.nationality_id, nationalities.nationality_value, "
	    + "\n\t\t family_status.family_status_id, family_status.family_status_value, "
	    + "\n\t\t phones.phone_id, phones.phone_value, phones.phone_comment, phones.phone_country_code, phones.phone_operator_code, phones.phone_type_id, phones_types.phone_type_value, "
	    + "\n\t\t atachments.atachment_id, atachments.contact_id,  atachments.atachment_name, atachments.atachment_upload_date, atachments.atachment_comment "
	    + "\n\t FROM contacts "
	    + "\n\t LEFT JOIN nationalities 	ON contacts.nationality_id	= nationalities.nationality_id "
	    + "\n\t LEFT JOIN sexes 	  	ON contacts.sex_id		= sexes.sex_id "
	    + "\n\t LEFT JOIN family_status 	ON contacts.family_status_id	= family_status.family_status_id "
            + "\n\t LEFT JOIN phones 	  	ON contacts.contact_id		= phones.contact_id "
            + "\n\t LEFT JOIN phones_types  	ON phones.phone_type_id 	= phones_types.phone_type_id "
            + "\n\t LEFT JOIN atachments 	ON contacts.contact_id		= atachments.contact_id ";
  
    
    public static final String GET_LIMIT_QUERY_BODY = 
	    "\n\t SELECT contacts.contact_id, first_name, last_name, middle_name, avatar_path, date_of_birth, "
            + "\n\t\t current_employment, country, city, street, house, block, apartment, city_index "
	    + "\n\t FROM contacts ";
    
    public static final String GET_CONTACT_BY_ID = 
	    GET_LIMIT_QUERY_BODY
	 + "\n\t WHERE contacts.contact_id = ?"; 
    
    public static final String GET_LIMIT_QUERY = 
	      GET_LIMIT_QUERY_BODY
	    + ORDER_BY
	    + "\n\t LIMIT ?, ? ";

    public static final String GET_BY_ID_QUERY = 
	    GET_QUERY
          + " \n\t WHERE contacts.contact_id = ? \n " 
          + ORDER_BY;
    
    public static final String COUNT_QUERY = 
	    "\t SELECT COUNT(*) FROM contacts";
    
    public static final String DELETE_QUERY = 
	      "\n\t DELETE FROM contacts "
	    + "\n\t WHERE contact_id = ?";
    
    public static final String GET_ID_QUERY =
	    "\n\t SELECT contacts.contact_id "
	  + "\n\t FROM contacts "
	  + "\n\t WHERE contacts.email_address = ? ";  
    
    public ContactDaoImp() {
	this(true, true);
    }
    
    public ContactDaoImp(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setDeleteQuery(DELETE_QUERY);
	setCountQuery(COUNT_QUERY);
	setGetLimitQuery(GET_LIMIT_QUERY);
	setGetAllQuery(GET_QUERY);
	setGetByIdQuery(GET_BY_ID_QUERY);
	setGetIdQuery(GET_ID_QUERY);
	setReader(DaoReadersContainer.LIMIT_CONTACT_READER);
    }

    @Override
    public long deleteRange(Long contactId, Long[] ids) throws ClassNotFoundException, IOException, SQLException {
	throw new SQLException("not supported this operation!");
    }

    @Override
    public ContactEntity getContact(long id) throws ClassNotFoundException, IOException, SQLException {
	setReader(DaoReadersContainer.LIMIT_CONTACT_READER);
	setGetByIdQuery(GET_CONTACT_BY_ID);
	return super.get(id);
    }

    @Override
    public ContactEntity get(Long id) throws ClassNotFoundException, IOException, SQLException {
	setReader(DaoReadersContainer.FULL_CONTACT_READER);
	setGetByIdQuery(GET_BY_ID_QUERY);
	return super.get(id);
    }

    @Override
    public List<ContactEntity> getLimit(long start, long size) throws ClassNotFoundException, IOException, SQLException {
	setReader(DaoReadersContainer.LIMIT_CONTACT_READER);
	setGetLimitQuery(GET_LIMIT_QUERY);
	return super.getLimit(start, size);
    }

    @Override
    public List<ContactEntity> getAll() throws ClassNotFoundException, IOException, SQLException {
	setReader(DaoReadersContainer.FULL_CONTACT_READER);
	setGetAllQuery(GET_QUERY);
	return super.getAll();
    }

    private static final String SET_AVATAR_PATH_QUERY = 
	      "\n\t UPDATE contacts "
	    + "\n\t SET avatar_path = ? "
	    + "\n\t WHERE contact_id = ? ";
    
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
	      "\n\t SELECT avatar_path "
	    + "\n\t FROM contacts "
	    + "\n\t WHERE contact_id = ?";
            
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
    
    public static final String GET_NAME_BY_EMAIL = 
	    "\n\t SELECT contacts.first_name "
	  + "\n\t FROM contacts "
          + "\n\t WHERE contacts.email_address = ? ";
    
    @Override
    public String getName(String email) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("email  = {}", email);

	String name = null;
	try {
	    connect();
	    preparedStatement = getPrepareStatement(GET_NAME_BY_EMAIL);
	    preparedStatement.setString(1, email);
	    resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
		name = resultSet.getString(1);
	    }

	    LOGGER.info("query: {}", preparedStatement );
	} finally {
	    closeResources();
	}

	LOGGER.info("name: {}", name);
	LOGGER.info("END");
	return name;
    }

    private static final String GET_EMAIL_QUERY = 
	     "\n\t SELECT email_address "
	   + "\n\t FROM contacts " 
	   + "\n\t WHERE contact_id = ?";

    @Override
    public String getEmail(long contactId) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("contact id = {}", contactId);

	String emailAdress = null;
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
	      "\n\t INSERT INTO contacts"
	    + "\n\t\t (first_name, last_name, middle_name, date_of_birth, "
	    + "\n\t\t sex_id, nationality_id, family_status_id, "
	    + "\n\t\t web_site, email_address, current_employment, country, city, street, house, block, apartment, city_index) "
	    + "\n\t VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
    
    private static final String SEARCH_QUERY_TEMPLATE = 
	            GET_QUERY
	       + " \n\t WHERE \n\t ( ";
	    		
    private static final String SEARCH_LIKE_STRING_TEMPLATE = 
	    	"\n\t\t %s LIKE ? AND ";

    private static final String SEARCH_COMPARE_DATE_TEMPLATE = 
	    	"\n\t\t %s %s ?  OR ";
    
    private static final String END = 
	    	" \n\t )";

    private interface Setter {
	public void set(PreparedStatement preparedStatement) throws SQLException;
    }

    private final class SetterFactory {
	private int counter = 0;

	public final Setter getSetterForString(final String value) {

	    return new Setter() {
		@Override
		public void set(PreparedStatement preparedStatement) throws SQLException {
		    preparedStatement.setString(++counter, String.format("%%%s%%", value));
		}
	    };
	}

	public final Setter getSetterForDate(final java.sql.Date date) {

	    return new Setter() {
		@Override
		public void set(PreparedStatement preparedStatement) throws SQLException {
		    preparedStatement.setDate(++counter, date);
		}
	    };
	}
    }

    @Override
    public List<ContactEntity> search(ContactEntity contact, boolean isLess) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	SetterFactory setterFactory = new SetterFactory();
	List<ContactDaoImp.Setter> setters = new ArrayList<ContactDaoImp.Setter>();
	String query = SEARCH_QUERY_TEMPLATE;// when statement
	
	if (!StringUtils.isEmptyOrWhitespaceOnly(contact.getFirstName())) {
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.first_name"));
	    setters.add(setterFactory.getSetterForString(contact.getFirstName()));
	}
	if (!StringUtils.isEmptyOrWhitespaceOnly(contact.getMiddleName())) {
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.middle_name"));
	    setters.add(setterFactory.getSetterForString(contact.getMiddleName()));
	}
	if (!StringUtils.isEmptyOrWhitespaceOnly(contact.getLastName())) {
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.last_name"));
	    setters.add(setterFactory.getSetterForString(contact.getLastName()));
	}
	if (contact.getDateOfBirth() != null) {
	    String sign = isLess ? "<" : ">";
	    query = query.concat(String.format(SEARCH_COMPARE_DATE_TEMPLATE, "contacts.date_of_birth", sign));
	    setters.add(setterFactory.getSetterForDate(contact.getDateOfBirth()));
	}
	if (!StringUtils.isEmptyOrWhitespaceOnly(contact.getWebSite())) {
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.web_site"));
	    setters.add(setterFactory.getSetterForString(contact.getWebSite()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getEmailAddress())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.email_address"));
	    setters.add(setterFactory.getSetterForString(contact.getEmailAddress()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getCurrentEmployment())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.current_employment"));
	    setters.add(setterFactory.getSetterForString(contact.getCurrentEmployment()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getCountry())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.country"));
	    setters.add(setterFactory.getSetterForString(contact.getCountry()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getCity())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.city"));
	    setters.add(setterFactory.getSetterForString(contact.getCity()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getStreet())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.street"));
	    setters.add(setterFactory.getSetterForString(contact.getStreet()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getHouse())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.house"));
	    setters.add(setterFactory.getSetterForString(contact.getHouse()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getBlock())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.block"));
	    setters.add(setterFactory.getSetterForString(contact.getBlock()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getApartment())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.apartment"));
	    setters.add(setterFactory.getSetterForString(contact.getApartment()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getCityIndex())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "contacts.city_index"));
	    setters.add(setterFactory.getSetterForString(contact.getCityIndex()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getSex())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "sexes.sex_value"));
	    setters.add(setterFactory.getSetterForString(contact.getSex()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getNationality())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "nationalities.nationality_value"));
	    setters.add(setterFactory.getSetterForString(contact.getNationality()));
	}
	if(!StringUtils.isEmptyOrWhitespaceOnly(contact.getFamilyStatus())){
	    query = query.concat(String.format(SEARCH_LIKE_STRING_TEMPLATE, "family_status.family_status_value"));
	    setters.add(setterFactory.getSetterForString(contact.getFamilyStatus()));
	}
	 
	query = query.substring(0, query.lastIndexOf("AND")).concat(END);
	
	List<ContactEntity> contacts = null;
	try{
	    connect();
	    preparedStatement = getPrepareStatement(query);
	    for (Setter setter : setters) {
		setter.set(preparedStatement);
	    }
	    resultSet = preparedStatement.executeQuery();
	    
	    LOGGER.info("query: {}", preparedStatement);
	    
	    DaoReader<Entity> reader = DaoReadersContainer.FULL_CONTACT_READER;
	    contacts = new ArrayList<ContactEntity>();
	    ContactEntity contactEntity = null;
	    while ((contactEntity = (ContactEntity) reader.read(resultSet)) != null) {
		contacts.add(contactEntity);
	    }
	    
	    LOGGER.info("found {} contacts", contacts.size());
	}finally{
	    closeResources();
	}

	LOGGER.info("END");
	return contacts;
    }

    private static final String UPDATE_QUERY = 
	    "\n\t UPDATE contacts "
	  + "\n\t SET "
	  + "\n\t\t first_name = ?, last_name = ?, middle_name = ?, date_of_birth = ?, "
	  + "\n\t\t sex_id = ?, nationality_id = ?, family_status_id = ?, "
	  + "\n\t\t web_site = ?, email_address = ?, current_employment = ?, country = ?, city = ?, "
	  + "\n\t\t street = ?, house = ?, block = ?, apartment = ?, city_index = ? "
	  + "\n\t WHERE contact_id = ?";

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
