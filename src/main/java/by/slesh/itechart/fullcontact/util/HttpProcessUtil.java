package by.slesh.itechart.fullcontact.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public class HttpProcessUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpProcessUtil.class);

    public static long[] checkForDeletingPhones(HttpServletRequest request) {
	return checkDeletingIdFor(request, "phone-idz");
    }

    public static long[] checkForDeletingAtachments(HttpServletRequest request) {
	return checkDeletingIdFor(request, "atachment-id");
    }

    private static long[] checkDeletingIdFor(HttpServletRequest request, String parameterName) {
	LOGGER.info("BEGIN");

	String parameterId = request.getParameter(parameterName);

	LOGGER.info("origin parameter id: |{}|", parameterId);

	if (StringUtils.isEmptyOrWhitespaceOnly(parameterId)) {
	    return new long[] { -1 };
	}
	String token = parameterId.replaceAll("-1", "");
	if (StringUtils.isEmptyOrWhitespaceOnly(token)) {
	    return new long[] { -1 };
	}
	String[] tokensId = token.trim().split("\\s+");

	LOGGER.info("parameter id: |{}|", Arrays.toString(tokensId));

	long[] ids = new long[tokensId.length];

	try {
	    for (int i = 0; i < ids.length; i++) {
		ids[i] = Long.parseLong(tokensId[i]);
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

	long contactId = idParameter == null ? -1 : Long.parseLong(idParameter);

	contact.setId(contactId);
	contact.setFirstName(request.getParameter("first-name"));
	contact.setMiddleName(request.getParameter("middle-name"));
	contact.setLastName(request.getParameter("last-name"));

	String dateString = request.getParameter("date-of-birth");
	java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
	Date sqlDate = new java.sql.Date(utilDate.getTime());
	contact.setDateOfBirth(sqlDate);

	contact.setSex(request.getParameter("sex"));
	contact.setNationality(request.getParameter("nationality"));
	contact.setFamilyStatus(request.getParameter("family-status"));
	contact.setWebSite(request.getParameter("web-site"));
	contact.setEmailAddress(request.getParameter("email-address"));

	LOGGER.info("obtain {} phones", fetchPhones(request, contact));
	LOGGER.info("obtain {} atachments", fetchAtachments(request, contact));

	contact.setCurrentEmployment(request.getParameter("current-employment"));
	contact.setCountry(request.getParameter("country"));
	contact.setCity(request.getParameter("city"));
	contact.setStreet(request.getParameter("street"));
	contact.setHouse(request.getParameter("house"));
	contact.setBlock(request.getParameter("block"));
	contact.setApartment(request.getParameter("apartment"));
	contact.setCityIndex(request.getParameter("city-index"));

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

	LOGGER.info("array is null? - {}", (phonesId == null));

	LOGGER.info("phonesId: {}", Arrays.toString(phonesId));
	LOGGER.info("phonesValue: {}", Arrays.toString(phonesValue));
	LOGGER.info("phonesType: {}", Arrays.toString(phonesType));
	LOGGER.info("phonesComment: {}", Arrays.toString(phonesComment));
	LOGGER.info("phonesCountryCode: {}", Arrays.toString(phonesCountryCode));
	LOGGER.info("phonesOperatorCode: {}", Arrays.toString(phonesOperatorCode));

	long quantity = phonesId.length;

	for (int i = 0; i < quantity; ++i) {
	    String id = phonesId[i];
	    if (StringUtils.isEmptyOrWhitespaceOnly(id)) {
		continue;
	    }
	    PhoneEntity phone = new PhoneEntity();
	    phone.setId(Long.parseLong(phonesId[i]));
	    phone.setContactId(targetContact.getId());
	    phone.setValue(phonesValue[i]);
	    phone.setType(phonesType[i]);
	    phone.setComment(phonesComment[i]);
	    phone.setCountryCode(phonesCountryCode[i]);
	    phone.setOperatorCode(phonesOperatorCode[i]);

	    targetContact.addPhone(phone);
	}

	return quantity;
    }

    private static long fetchAtachments(HttpServletRequest request, ContactEntity targetContact) {
	String[] atachmentsId = request.getParameterValues("atachment-id");
	String[] atachmentsName = request.getParameterValues("atachment-name");
	String[] atachmentsComment = request.getParameterValues("atachment-comment");

	LOGGER.info("atachmentsId: {}", Arrays.toString(atachmentsId));
	LOGGER.info("atachmentsName: {}", Arrays.toString(atachmentsName));
	LOGGER.info("atachmentsComment: {}", Arrays.toString(atachmentsComment));

	long quantity = atachmentsId.length;

	for (int i = 0; i < quantity; ++i) {
	    String id = atachmentsId[i];
	    if (StringUtils.isEmptyOrWhitespaceOnly(id)) {
		continue;
	    }
	    AtachmentEntity atachment = new AtachmentEntity();
	    atachment.setId(Long.parseLong(atachmentsId[i]));
	    atachment.setContactId(targetContact.getId());
	    atachment.setName(atachmentsName[i]);
	    atachment.setUploadDate(new java.sql.Date(new java.util.Date().getTime()));
	    atachment.setComment(atachmentsComment[i]);
	    targetContact.addAtachment(atachment);
	}

	return quantity;
    }
}
