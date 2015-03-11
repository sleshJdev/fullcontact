package by.slesh.itechart.fullcontact.util;

import java.text.ParseException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public final class HttpProcessUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpProcessUtil.class);

    public static Long[] checkForDeletingPhones(HttpServletRequest request) {
	return checkDeletingIdFor(request, "deleting-phone-ids");
    }

    public static Long[] checkForDeletingAtachments(HttpServletRequest request) {
	return checkDeletingIdFor(request, "deleting-atachmet-ids");
    }

    private static Long[] checkDeletingIdFor(HttpServletRequest request, String parameterName) {
	LOGGER.info("BEGIN");
	LOGGER.info("check deleting for {}", parameterName);
	
	String parameterId = request.getParameter(parameterName);

	LOGGER.info("origin parameter id: |{}|", parameterId);
	
	if (StringUtils.isEmptyOrWhitespaceOnly(parameterId)) {
	    LOGGER.info("{} null or empty", parameterId);
	    return null;
	}
	String token = parameterId.replaceAll("null", "");
	if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
	    LOGGER.info("{} null or empty", parameterName);
	    return null;
	}
	String[] tokensId = token.trim().split("\\s+");

	LOGGER.info("parameter id: |{}|", Arrays.toString(tokensId));

	Long[] ids = new Long[tokensId.length];

	try {
	    for (int i = 0; i < ids.length; i++) {
		ids[i] = "null".equals(tokensId[i]) ? null : Long.parseLong(tokensId[i]);
	    }
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	}

	return ids;
    }

    public static ContactEntity readContactFrom(HttpServletRequest request) throws ParseException {
	LOGGER.info("BEGIN");

	ContactEntity contact = new ContactEntity();

	String idParameter = request.getParameter("contact-id");

	Long contactId = idParameter == null ? null : Long.parseLong(idParameter);

	contact.setId(contactId);
	contact.setFirstName(request.getParameter("first-name").trim());
	contact.setMiddleName(request.getParameter("middle-name").trim());
	contact.setLastName(request.getParameter("last-name").trim());
	contact.setDateOfBirth(DateUtil.getSqlDate(request.getParameter("date-of-birth")));
	contact.setSex(request.getParameter("sex").trim());
	contact.setNationality(request.getParameter("nationality").trim());
	contact.setFamilyStatus(request.getParameter("family-status").trim());
	contact.setWebSite(request.getParameter("web-site").trim());
	contact.setEmailAddress(request.getParameter("email-address").trim());

	LOGGER.info("obtain {} phones", fetchPhones(request, contact));
	LOGGER.info("obtain {} atachments", fetchAtachments(request, contact));

	contact.setCurrentEmployment(request.getParameter("current-employment").trim());
	contact.setCountry(request.getParameter("country").trim());
	contact.setCity(request.getParameter("city").trim());
	contact.setStreet(request.getParameter("street").trim());
	contact.setHouse(request.getParameter("house").trim());
	contact.setBlock(request.getParameter("block").trim());
	contact.setApartment(request.getParameter("apartment").trim());
	contact.setCityIndex(request.getParameter("city-index").trim());

	LOGGER.info("obtained contact: {}", contact);
	LOGGER.info("END");

	return contact;
    }

    private static long fetchPhones(HttpServletRequest request, ContactEntity targetContact) {
	String[] phonesId = request.getParameterValues("phone-id");
	String[] phonesValue = request.getParameterValues("phone-value");
	String[] phonesType = request.getParameterValues("phone-type");
	String[] phonesComment = request.getParameterValues("phone-comment");
	String[] phonesCountryCode = request.getParameterValues("phone-country-code");
	String[] phonesOperatorCode = request.getParameterValues("phone-operator-code");

	if (phonesId == null || phonesValue == null || phonesType == null || phonesComment == null
		|| phonesCountryCode == null || phonesOperatorCode == null) {
	    LOGGER.info("RETURN: all phones attributes is null");
	    return 0;
	}

	LOGGER.info("phonesId({}): {}", phonesId.length, Arrays.toString(phonesId));
	LOGGER.info("phonesValue({}): {}", phonesValue.length, Arrays.toString(phonesValue));
	LOGGER.info("phonesType({}): {}", phonesType.length, Arrays.toString(phonesType));
	LOGGER.info("phonesComment({}): {}", phonesComment.length, Arrays.toString(phonesComment));
	LOGGER.info("phonesCountryCode({}): {}",phonesCountryCode.length,  Arrays.toString(phonesCountryCode));
	LOGGER.info("phonesOperatorCode({}): {}",phonesOperatorCode.length,  Arrays.toString(phonesOperatorCode));
	
	long quantity = phonesId.length;

	for (int i = 0; i < quantity; ++i) {
	    String id = phonesId[i];
	    if (StringUtils.isEmptyOrWhitespaceOnly(id)) {
		continue;
	    }
	    PhoneEntity phone = new PhoneEntity();
	    phone.setId("null".equals(id) ? null : Long.parseLong(id));
	    phone.setContactId(targetContact.getId());
	    phone.setValue(phonesValue[i].trim());
	    phone.setType(phonesType[i].trim());
	    phone.setComment(phonesComment[i].trim());
	    phone.setCountryCode(phonesCountryCode[i].trim());
	    phone.setOperatorCode(phonesOperatorCode[i].trim());

	    targetContact.addPhone(phone);
	}

	return quantity;
    }
    
    public static void main(String[] args) {
	String id = null;
	LOGGER.info("VALUE: {}    IS NULL {}", id, StringUtils.isEmptyOrWhitespaceOnly(id));
	LOGGER.info("VALUE: {}    IS NULL {}", id, StringUtils.isNullOrEmpty(id));
	LOGGER.info("VALUE: {}    IS NULL {}", id, id == null);
    }
    
    private static long fetchAtachments(HttpServletRequest request, ContactEntity targetContact) {
	String[] atachmentsId = request.getParameterValues("atachment-id");
	String[] atachmentsName = request.getParameterValues("atachment-name");
	String[] atachmentsChangedName = request.getParameterValues("changed-atachment-name");
	String[] atachmentsSaltName = request.getParameterValues("atachment-name-salt");
	String[] atachmentsComment = request.getParameterValues("atachment-comment");

	
	if (atachmentsId == null || atachmentsName == null || atachmentsChangedName == null
		|| atachmentsSaltName == null || atachmentsComment == null) {
	    LOGGER.info("RETURN: all atachments attributes is null");
	    return 0;
	}
	
	LOGGER.info("atachmentsId({}): {}", atachmentsId.length, Arrays.toString(atachmentsId));
	LOGGER.info("atachmentsName({}): {}",atachmentsName.length,  Arrays.toString(atachmentsName));
	LOGGER.info("atachmentsChangeName({}): {}",atachmentsChangedName.length,  Arrays.toString(atachmentsChangedName));
	LOGGER.info("atachmentsSaltName({}): {}",atachmentsSaltName.length,  Arrays.toString(atachmentsSaltName));
	LOGGER.info("atachmentsComment({}): {}",atachmentsComment.length,  Arrays.toString(atachmentsComment));
	
	long quantity = atachmentsId.length;

	for (int i = 0; i < quantity; ++i) {
	    String id = atachmentsId[i];
	    if (StringUtils.isEmptyOrWhitespaceOnly(id)) {
		continue;
	    }
	    
	    AttachmentEntity atachment = new AttachmentEntity();
	    atachment.setId("null".equals(id) ? null : Long.parseLong(id));
	    atachment.setContactId(targetContact.getId());
	    atachment.setName(atachmentsName[i].trim());
	    atachment.setChangedName(atachmentsChangedName[i]);
	    atachment.setSalt(atachmentsSaltName[i]);
	    atachment.setUploadDate(new java.sql.Date(new java.util.Date().getTime()));
	    atachment.setComment(atachmentsComment[i].trim());
	    targetContact.addAtachment(atachment);
	}

	return quantity;
    }
}
