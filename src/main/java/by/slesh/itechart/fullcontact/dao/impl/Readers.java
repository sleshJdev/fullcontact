package by.slesh.itechart.fullcontact.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.slesh.itechart.fullcontact.dao.DaoReader;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.Entity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;

public final class Readers {
    public static final DaoReader<Entity> FULL_CONTACT_READER = new DaoReader<Entity>() {
	@Override
	public ContactEntity read(ResultSet resultSet) throws SQLException {
	    if (resultSet.isBeforeFirst()) {
		resultSet.next();
	    }
	    if (resultSet.isAfterLast()) {
		return null;
	    }
	    Map<Long, PhoneEntity> phonesId = new HashMap<Long, PhoneEntity>();
	    Map<Long, AtachmentEntity> atachmentsId = new HashMap<Long, AtachmentEntity>();
	    ContactEntity contact = new ContactEntity();
	    long contactId = resultSet.getLong("contacts.contact_id");
	    do {
		long id = resultSet.getLong("contacts.contact_id");
		if ((contactId == id)) {
		    readMostPartTo(resultSet, contact);
		    readSmallPartTo(resultSet, contact);
		} else {
		    break;
		}
		long phoneId = resultSet.getLong("phone_id");
		if (phonesId.get(phoneId) == null) {
		    PhoneEntity phone = new PhoneEntity();
		    readTo(resultSet, phone);
		    phonesId.put(phoneId, phone);
		}

		long atachmentid = resultSet.getLong("atachment_id");
		if (atachmentsId.get(atachmentid) == null) {
		    AtachmentEntity atachment = new AtachmentEntity();
		    readTo(resultSet, atachment);
		    atachmentsId.put(atachmentid, atachment);
		}
	    } while (resultSet.next());

	    contact.setPhones(new ArrayList<PhoneEntity>(phonesId.values()));
	    contact.setAtachments(new ArrayList<AtachmentEntity>(atachmentsId.values()));

	    return contact;
	}
    };

    public static final DaoReader<Entity> LIMIT_CONTACT_READER = new DaoReader<Entity>() {
	@Override
	public ContactEntity read(ResultSet resultSet) throws SQLException {
	    if (resultSet.isBeforeFirst()) {
		resultSet.next();
	    }
	    if (resultSet.isAfterLast()) {
		return null;
	    }
	    ContactEntity contact = new ContactEntity();
	    long contactId = resultSet.getLong("contacts.contact_id");
	    do {
		long id = resultSet.getLong("contacts.contact_id");
		if (contactId == id) {
		    readMostPartTo(resultSet, contact);
		} else {
		    break;
		}
	    } while (resultSet.next());

	    return contact;
	}
    };

    public static final DaoReader<Entity> ENTITY_READER = new DaoReader<Entity>() {
	@Override
	public Entity read(ResultSet resultSet) throws SQLException {
	    if (resultSet.isBeforeFirst()) {
		resultSet.next();
	    }
	    if (resultSet.isAfterLast()) {
		return null;
	    }
	    Entity entity = new Entity();
	    long familyStatusId = resultSet.getLong(1);
	    do {
		long id = resultSet.getLong(1);
		if (familyStatusId == id) {
		    entity.setId(resultSet.getLong(1));
		    entity.setValue(resultSet.getString(2));
		} else {
		    break;
		}
	    } while (resultSet.next());

	    return entity;
	}
    };

    private static void readSmallPartTo(ResultSet resultSet, ContactEntity contact) throws SQLException {
	contact.setSex(resultSet.getString("sex_value"));
	contact.setNationality(resultSet.getString("nationality_value"));
	contact.setFamilyStatus(resultSet.getString("family_status_value"));
	contact.setWebSite(resultSet.getString("web_site"));
	contact.setWebSite(resultSet.getString("web_site"));
	contact.setEmailAddress(resultSet.getString("email_address"));
	contact.setAvatarPath(resultSet.getString("avatar_path"));
    }

    private static void readMostPartTo(ResultSet resultSet, ContactEntity contact) throws SQLException {
	contact.setId(resultSet.getLong("contacts.contact_id"));
	contact.setFirstName(resultSet.getString("contacts.first_name"));
	contact.setLastName(resultSet.getString("contacts.last_name"));
	contact.setMiddleName(resultSet.getString("contacts.middle_name"));
	contact.setAvatarPath(resultSet.getString("contacts.avatar_path"));
	contact.setDateOfBirth(resultSet.getDate("contacts.date_of_birth"));
	contact.setCurrentEmployment(resultSet.getString("contacts.current_employment"));
	contact.setCountry(resultSet.getString("contacts.country"));
	contact.setCity(resultSet.getString("contacts.city"));
	contact.setStreet(resultSet.getString("contacts.street"));
	contact.setHouse(resultSet.getString("contacts.house"));
	contact.setBlock(resultSet.getString("contacts.block"));
	contact.setApartment(resultSet.getString("contacts.apartment"));
	contact.setCityIndex(resultSet.getString("contacts.city_index"));
    }

    private static void readTo(ResultSet resultSet, PhoneEntity phone) throws SQLException {
	phone.setId(resultSet.getLong("phones.phone_id"));
	phone.setContactId(resultSet.getLong("contacts.contact_id"));
	phone.setValue(resultSet.getString("phones.phone_value"));
	phone.setComment(resultSet.getString("phones.phone_comment"));
	phone.setCountryCode(resultSet.getString("phones.phone_country_code"));
	phone.setOperatorCode(resultSet.getString("phones.phone_operator_code"));
	phone.setType(resultSet.getString("phones_types.phone_type_value"));
    }

    private static void readTo(ResultSet resultSet, AtachmentEntity atachment) throws SQLException {
	atachment.setId(resultSet.getLong("atachments.atachment_id"));
	atachment.setContactId(resultSet.getLong("contacts.contact_id"));
	atachment.setName(resultSet.getString("atachments.atachment_name"));
	atachment.setUploadDate(resultSet.getDate("atachments.atachment_upload_date"));
	atachment.setComment(resultSet.getString("atachments.atachment_comment"));
    }
}
