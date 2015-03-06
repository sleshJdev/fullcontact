package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.Getable;
import by.slesh.itechart.fullcontact.dao.PhoneDao;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;
import by.slesh.itechart.fullcontact.domain.PhoneTypeEntity;

public class PhoneDaoImpl extends EntityDao<PhoneEntity> implements PhoneDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(PhoneDaoImpl.class);

    public PhoneDaoImpl() {
    }

    public PhoneDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
    }
    
    private static final String ADD_PHONE_TEMPLATE = 
	      "\n INSERT INTO phones "
	    + "\n\t (phones.contact_id, phones.phone_value, phones.phone_type_id, phones.phone_comment, "
	    + "\n\t  phones.phone_country_code, phones.phone_operator_code) "
	    + "\n VALUES (?, ?, ?, ?, ?, ?)";
    
    @Override
    public long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<PhoneEntity> phones = contact.getPhones();
	if (phones == null || phones.size() == 0) {
	    LOGGER.info("return: no phones for add");
	    return 0;
	}
	long rowsAddeds = 0;
	try {
	    connect();
	    Getable<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, false);
	    Iterator<PhoneEntity> iterator = phones.iterator();
	    long id = contact.getId();
	    while (iterator.hasNext()) {
		PhoneEntity phone = iterator.next();
		if (phone.getId() == -1) {// if -1 then need add. Is new entity
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
		    closeResource(null, preparedStatement);
		    
		    LOGGER.info("{} query: {}", rowsAddeds, preparedStatement);
		}
	    }
	} finally {
	    closeResources();
	}

	LOGGER.info("add {} phones", rowsAddeds);
	LOGGER.info("END");

	return rowsAddeds;
    }
    
    private static final String DELETE_PHONE_QUERY_TEMPLATE = 
	      "\n DELETE FROM phones " 
	    + "\n WHERE phone_id NOT IN ( ? ) AND contact_id = ?";
    
    @Override
    public long deleteRange(long contactId, long[] ids) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("ids: {}", Arrays.toString(ids));

	if (ids == null || ids.length == 0) {
	    LOGGER.info("return: not phones for delete");
	    return 0;
	}
	long rowsDeleted = 0;
	try {
	    connect();
	    String inStatement = Arrays.toString(ids).replaceAll("[\\]\\[]", "");
	    preparedStatement = getPrepareStatement(DELETE_PHONE_QUERY_TEMPLATE);
	    preparedStatement.setString(1, inStatement);
	    preparedStatement.setLong(2, contactId);
	    preparedStatement.executeUpdate();
	    rowsDeleted = preparedStatement.getUpdateCount();

	    LOGGER.info("query: {}", preparedStatement);
	} finally {
	    closeResources();
	}

	LOGGER.info("delete {} phones", rowsDeleted);

	return rowsDeleted;
    }

    private static final String UPDATE_PHONE_QUERY = 
	      "\n UPDATE phones "
	    + "\n SET " 
	    + "\n\t phone_value = ?, " 
	    + "\n\t phone_type_id = ?, "
	    + "\n\t phone_comment = ?, "
	    + "\n\t phone_country_code = ?, "
	    + "\n\t phone_operator_code = ? "
	    + "\n WHERE phone_id = ?";

    @Override
    public long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException {
	LOGGER.info("BEGIN");

	List<PhoneEntity> phones = contact.getPhones();
	if (phones == null || phones.size() == 0) {
	    LOGGER.info("return update()");
	    return 0;
	}
	long rowsUpdated = 0;
	try {
	    connect();
	    Getable<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, false);
	    Iterator<PhoneEntity> iterator = phones.iterator();
	    while (iterator.hasNext()) {
		PhoneEntity phone = iterator.next();
		long id = phone.getId();
		if (id != -1) {
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
