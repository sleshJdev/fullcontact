package by.slesh.itechart.fullcontact.util;

import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public final class DateUtil {
    public static final java.util.Locale LOCALE = java.util.Locale.US;
    public static final String SQL_DATE_FORMATE = "yyyy-MM-dd";
    public static final DateTimeZone TIME_ZONE = DateTimeZone.forID("Pacific/Honolulu");
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(SQL_DATE_FORMATE).withZone(TIME_ZONE)
	    .withLocale(LOCALE);

    public static final java.sql.Date getSqlDate(String dateString) throws ParseException {
	DateTime dateTime = FORMATTER.parseDateTime(dateString);
	java.sql.Date sqlDate = new java.sql.Date(dateTime.toDate().getTime());

	return sqlDate;
    }

    public static void main(String[] args) throws ParseException {
	System.out.println(getSqlDate("1999-02-12"));
    }

    public static final java.sql.Date getSqlDate() throws ParseException {
	java.util.Date date = DateTime.now().toDate();
	return new java.sql.Date(date.getTime());
    }
}
