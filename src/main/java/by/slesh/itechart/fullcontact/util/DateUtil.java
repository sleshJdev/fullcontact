package by.slesh.itechart.fullcontact.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public final class DateUtil {
    public static final String SQL_DATE_FORMATE = "yyyy-MM-dd";
    
    public static final java.sql.Date getSqlDate(String dateString) throws ParseException {
	java.util.Date utilDate = new SimpleDateFormat(SQL_DATE_FORMATE).parse(dateString);
	java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

	return sqlDate;
    }

    public static final java.sql.Date getSqlDate() throws ParseException {
	return getSqlDate(new SimpleDateFormat(SQL_DATE_FORMATE).format(new java.util.Date()));
    }
}
