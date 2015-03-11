package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.Getable;
import by.slesh.itechart.fullcontact.dao.PhoneDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;
import by.slesh.itechart.fullcontact.domain.PhoneTypeEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class PhoneDaoImpl extends EntityDao<PhoneEntity> implements PhoneDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(PhoneDaoImpl.class);

    private static final String GET_BY_ID_QUERY = 
	    "\n\t SELECT  phones.phone_id, contact_id, phone_value, phones_types.phone_type_value, "
	  + "\n\t\t  phone_comment, phone_country_code, phone_operator_code "
	  + "\n\t FROM phones "
          + "\n\t LEFT JOIN phones_types ON phones.phone_type_id = phones_types.phone_type_id "
    	  + "\n\t WHERE phones.phone_id = ? ";
    
    private static final String DELETE_RANGE_PHONE_QUERY_TEMPLATE = 
	      "\n\t DELETE FROM phones " 
	    + "\n\t WHERE phone_id IN ( %s ) AND contact_id = ?";
    
    public PhoneDaoImpl() {
	this(true, true);
    }

    public PhoneDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setDeleteRangeQuery(DELETE_RANGE_PHONE_QUERY_TEMPLATE);
	setGetByIdQuery(GET_BY_ID_QUERY);
	setReader(DaoReadersContainer.PHONES_READER);
    }
    
    private static final String ADD_PHONE_TEMPLATE = 
	      "\n\t INSERT INTO phones "
	    + "\n\t\t (phones.contact_id, phones.phone_value, phones.phone_type_id, phones.phone_comment, "
	    + "\n\t\t phones.phone_country_code, phones.phone_operator_code) "
	    + "\n\t VALUES (?, ?, ?, ?, ?, ?)";
    
    @Override
    public Long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<PhoneEntity> phones = contact.getPhones();
	if (phones == null || phones.size() == 0) {
	    LOGGER.info("RETURN: no phones for add");
	    return null;
	}
	long rowsAddeds = 0;
	try {
	    connect();
	    EntityDao<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, false);
	    Iterator<PhoneEntity> iterator = phones.iterator();
	    long id = contact.getId();
	    while (iterator.hasNext()) {
		PhoneEntity phone = iterator.next();
		if (phone.getId() == null) {// if -1 then need add. Is new entity
		    preparedStatement = getPrepareStatement(ADD_PHONE_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
		    preparedStatement.setLong(1, id);
		    preparedStatement.setString(2, phone.getValue());
		    preparedStatement.setLong(3, phoneTypeDao.getId(phone.getType()));
		    preparedStatement.setString(4, phone.getComment());
		    preparedStatement.setString(5, phone.getCountryCode());
		    preparedStatement.setString(6, phone.getOperatorCode());
		    preparedStatement.executeUpdate();
		    resultSet = preparedStatement.getGeneratedKeys();
		    while (resultSet.next()) {
			phone.setId(resultSet.getLong(1));
		    }
		    ++rowsAddeds;
		    
		    LOGGER.info("{} query: {}", rowsAddeds, preparedStatement);

		    closeResource(null, preparedStatement);
		}
	    }
	} finally {
	    closeResources();
	}

	LOGGER.info("add {} phones", rowsAddeds);
	LOGGER.info("END");

	return rowsAddeds;
    }

    private static final String UPDATE_PHONE_QUERY = 
	      "\n\t UPDATE phones "
	    + "\n\t SET " 
	    + "\n\t\t phone_value = ?, " 
	    + "\n\t\t phone_type_id = ?, "
	    + "\n\t\t phone_comment = ?, "
	    + "\n\t\t phone_country_code = ?, "
	    + "\n\t\t phone_operator_code = ? "
	    + "\n\t WHERE phone_id = ?";

    @Override
    public Long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<PhoneEntity> phones = contact.getPhones();
	if (phones == null || phones.size() == 0) {
	    LOGGER.info("RETURN: not phones for update");
	    return null;
	}
	long rowsUpdated = 0;
	try {
	    connect();
	    Getable<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, false);
	    Iterator<PhoneEntity> iterator = phones.iterator();
	    while (iterator.hasNext()) {
		PhoneEntity phone = iterator.next();
		Long id = phone.getId();
		if (id != null) {
		    preparedStatement = getPrepareStatement(UPDATE_PHONE_QUERY);
		    preparedStatement.setString(1, phone.getValue());
		    preparedStatement.setLong(2, phoneTypeDao.getId(phone.getType()));
		    preparedStatement.setString(3, phone.getComment());
		    preparedStatement.setString(4, phone.getCountryCode());
		    preparedStatement.setString(5, phone.getOperatorCode());
		    preparedStatement.setLong(6, id);
		    preparedStatement.executeUpdate();
		    ++rowsUpdated;

		    LOGGER.info("{} query: {}", rowsUpdated, preparedStatement);

		    closeResource(null, preparedStatement);
		}
	    }
	} finally {
	    closeResources();
	}

	LOGGER.info("update {} phones", rowsUpdated);
	LOGGER.info("END");

	return rowsUpdated;
    }
}
