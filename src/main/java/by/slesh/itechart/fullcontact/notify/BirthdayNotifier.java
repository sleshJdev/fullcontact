package by.slesh.itechart.fullcontact.notify;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.db.JdbcConnector;
import by.slesh.itechart.fullcontact.db.local.Database;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.DateUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 8, 2015
 *
 */
public class BirthdayNotifier extends TimerTask implements Notifier {
    private final static Logger LOGGER = LoggerFactory.getLogger(BirthdayNotifier.class);
    private Timer timer;
    private long period;

    public BirthdayNotifier() {
	this(3, TimeUnit.SECONDS);
    }

    public BirthdayNotifier(long period, TimeUnit unit) {
	if (period <= 0) {
	    throw new IllegalAccessError("period cannot be negative");
	}
	this.period = unit.toMillis(period);
    }

    public long getPeriod() {
	return period;
    }

    public void setPeriod(long period) {
	this.period = period;
    }

    @Override
    public void run() {
	LOGGER.info("BEGIN");

	Connection connection = null;
	PreparedStatement preparedStatement = null;
	try {
	    connection = JdbcConnector.openConnection();
	    final String query = 
		    "\n\t SELECT contact_id, first_name, middle_name, last_name, email_address "
		  + "\n\t FROM contacts "
	          + "\n\t WHERE date_of_birth = ? ";
	    preparedStatement = connection.prepareStatement(query);
	    preparedStatement.setDate(1, DateUtil.getSqlDate());
	    ResultSet resultSet = preparedStatement.executeQuery();
	    List<ContactEntity> birthdayMans = new ArrayList<ContactEntity>();
	    while (resultSet.next()) {
		ContactEntity contact = new ContactEntity();
		contact.setId(resultSet.getLong("contact_id"));
		contact.setFirstName(resultSet.getString("first_name"));
		contact.setMiddleName(resultSet.getString("middle_name"));
		contact.setLastName(resultSet.getString("last_name"));
		contact.setEmailAddress(resultSet.getString("email_address"));
		contact.setDateOfBirth(DateUtil.getSqlDate());
		birthdayMans.add(contact);
	    }
	    if (birthdayMans.size() > 0) {
		Database.setBirthdayMans(birthdayMans);
		
		LOGGER.info("quantity birthday mans: {}", birthdayMans.size());
	    }
	} catch (ClassNotFoundException | IOException | SQLException | ParseException e) {
	    LOGGER.error("error occured during notify: {}", e.getMessage());
	} finally {
	    JdbcConnector.closeResource(connection, preparedStatement);
	}

	LOGGER.info("END");
    }
    
    public void startNotify() {
	if (timer == null) {
	    timer = new Timer(true);
	    timer.schedule(this, 0, period);
	}
    }

    public void stopNotify() {
	if (timer != null) {
	    timer.cancel();
	    timer.purge();
	    timer = null;
	}
    }
}
