package by.slesh.itechart.fullcontact.dao.reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.EmailEntity;
import by.slesh.itechart.fullcontact.domain.Entity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public final class DaoReadersContainer {
    public static final DaoReader<Entity> EMAIL_READER = new DaoReader<Entity>() {
	@Override
	public Entity read(ResultSet resultSet) throws SQLException {
	    if (resultSet.isBeforeFirst()) {
		resultSet.next();
	    }
	    if (resultSet.isAfterLast()) {
		return null;
	    }
	    EmailEntity email = new EmailEntity();
	    Long emailId = resultSet.getLong("email_id");
	    do {
		Long id = resultSet.getLong("email_id");
		if (emailId == id) {
		    email.setId(resultSet.getLong("email_id"));
		    email.setContactIdSender(resultSet.getLong("contact_id_sender"));
		    email.setSubject(resultSet.getString("email_subject"));
		    email.setText(resultSet.getString("email_text"));
		    email.setSendDate(resultSet.getDate("email_date_send"));
		} else {
		    break;
		}
	    } while (resultSet.next());
	    return email;
	}
    };

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
	    Map<Long, AttachmentEntity> atachmentsId = new HashMap<Long, AttachmentEntity>();
	    ContactEntity contact = new ContactEntity();
	    Long contactId = resultSet.getLong("contacts.contact_id");
	    do {
		Long id = resultSet.getLong("contacts.contact_id");
		if ((contactId == id)) {
		    readMostPartTo(resultSet, contact);
		    readSmallPartTo(resultSet, contact);
		} else {
		    break;
		}
		Long phoneId = resultSet.getLong("phones.phone_id");
		if (phonesId.get(phoneId) == null && phoneId > 0) {
		    PhoneEntity phone = new PhoneEntity();
		    readTo(resultSet, phone);
		    phonesId.put(phoneId, phone);
		}

		Long atachmentId = resultSet.getLong("atachments.atachment_id");
		if (atachmentsId.get(atachmentId) == null && atachmentId > 0) {
		    AttachmentEntity atachment = new AttachmentEntity();
		    readTo(resultSet, atachment);
		    atachmentsId.put(atachmentId, atachment);
		}
	    } while (resultSet.next());

	    contact.setPhones(new ArrayList<PhoneEntity>(phonesId.values()));
	    contact.setAtachments(new ArrayList<AttachmentEntity>(atachmentsId.values()));
	    
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
	    Long contactId = resultSet.getLong("contacts.contact_id");
	    do {
		Long id = resultSet.getLong("contacts.contact_id");
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
//	    LOGGER.info("BEGIN: read entity ");
	    
	    if (resultSet.isBeforeFirst()) {
		resultSet.next();
	    }
	    if (resultSet.isAfterLast()) {
		return null;
	    }
	    Entity entity = new Entity() {
	    };
	    Long familyStatusId = resultSet.getLong(1);
	    do {
		Long id = resultSet.getLong(1);
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

    public static final DaoReader<Entity> ATACHMENTS_READER = new DaoReader<Entity>() {
	@Override
	public Entity read(ResultSet resultSet) throws SQLException {
	    if (resultSet.isBeforeFirst()) {
		resultSet.next();
	    }
	    if (resultSet.isAfterLast()) {
		return null;
	    }
	    AttachmentEntity atachment = new AttachmentEntity();
	    Long atachmentId = resultSet.getLong(1);
	    
	    do {
		Long id = resultSet.getLong(1);
		if (atachmentId == id) {
		    readTo(resultSet, atachment);
		} else {
		    break;
		}
	    } while (resultSet.next());

	    return atachment;
	}
    };
    
    public static final DaoReader<Entity> PHONES_READER = new DaoReader<Entity>() {
	@Override
	public Entity read(ResultSet resultSet) throws SQLException {
	    if (resultSet.isBeforeFirst()) {
		resultSet.next();
	    }
	    if (resultSet.isAfterLast()) {
		return null;
	    }
	    PhoneEntity phone = new PhoneEntity();
	    Long phoneId = resultSet.getLong(1);
	    do {
		Long id = resultSet.getLong(1);
		if (phoneId == id) {
		    readTo(resultSet, phone);
		} else {
		    break;
		}
	    } while (resultSet.next());

	    return phone;
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
	phone.setContactId(resultSet.getLong("contact_id"));
	phone.setValue(resultSet.getString("phones.phone_value"));
	phone.setComment(resultSet.getString("phones.phone_comment"));
	phone.setCountryCode(resultSet.getString("phones.phone_country_code"));
	phone.setOperatorCode(resultSet.getString("phones.phone_operator_code"));
	phone.setType(resultSet.getString("phones_types.phone_type_value"));
    }

    private static void readTo(ResultSet resultSet, AttachmentEntity atachment) throws SQLException {
	atachment.setId(resultSet.getLong("atachments.atachment_id"));
	atachment.setContactId(resultSet.getLong("atachments.contact_id"));
	atachment.setName(resultSet.getString("atachments.atachment_name"));
	atachment.setUploadDate(resultSet.getDate("atachments.atachment_upload_date"));
	atachment.setComment(resultSet.getString("atachments.atachment_comment"));
    }
}
