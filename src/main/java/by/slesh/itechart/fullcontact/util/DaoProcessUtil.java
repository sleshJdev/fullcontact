package by.slesh.itechart.fullcontact.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 *
 */
public final class DaoProcessUtil{
    private final static Logger LOGGER = LoggerFactory.getLogger(DaoProcessUtil.class);

    public static String processToken(String targetQuery, String value, String field, String template) {
	LOGGER.info("BEGIN");
	LOGGER.info("value: {}", value);

	if (!StringUtils.isEmptyOrWhitespaceOnly(value)) {
	    String[] tokens = value.split("\\s+");
	    for (String token : tokens) {
		token = token.concat(String.format("%s %s %s", "%", token, "%"));
		targetQuery += String.format(template, field, token);
	    }

	    LOGGER.info("END return new query: ");

	    return targetQuery;
	}

	LOGGER.info("for {} return empty string", field);

	return targetQuery;
    }
}
