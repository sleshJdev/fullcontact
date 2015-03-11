package by.slesh.itechart.fullcontact.servlet.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.db.local.Database;
import by.slesh.itechart.fullcontact.notify.BirthdayNotifier;
import by.slesh.itechart.fullcontact.notify.Notifier;
import by.slesh.itechart.fullcontact.settings.G;
import by.slesh.itechart.fullcontact.util.PathUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 * 
 *         Main listener. Make set up application path and other settings
 *
 */
public class ApplicationListener implements ServletContextListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationListener.class);
    private Notifier notifier;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
	LOGGER.info("BEGIN");

	try {
	    ServletContext context = servletContextEvent.getServletContext();
	    fileLocationSetup(context);

	    if (notifier == null) {
		notifier = new BirthdayNotifier(1, TimeUnit.MINUTES);
		notifier.startNotify();
	    }

	    Database.setNationalities(DaoFactory.getNationalityDao(true, true).getAll());
	    Database.setFamilyStatuses(DaoFactory.getFamilyStatusDao(true, true).getAll());
	    Database.setSexes(DaoFactory.getSexDao(true, true).getAll());
	    Database.setPhoneTypes(DaoFactory.getPhoneTypeDao(true, true).getAll());
	} catch (ClassNotFoundException | IOException | SQLException e) {
	    LOGGER.error("error occured during initialize local database: {}", e);
	}

	LOGGER.info("END. servlet context initilize successful!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
	LOGGER.info("BEGIN");

	if (notifier != null) {
	    notifier.stopNotify();
	}

	if (G.CACHE.exists()) {
	    try {
		FileUtils.deleteDirectory(G.CACHE);
	    } catch (IOException e) {
		LOGGER.error("cannot delete cache directory: {}", G.CACHE);
	    }
	}

	LOGGER.info("END");
    }

    private void fileLocationSetup(ServletContext context) throws FileNotFoundException, IOException {
	Properties properties = new Properties();
	properties.load(new FileInputStream(PathUtil.getResourceFile("upload.properties")));

	File attachmentsFile = new File(properties.getProperty("attachments_directory"));
	if (!attachmentsFile.exists()) {
	    attachmentsFile.mkdirs();
	}

	File avatarsFile = new File(properties.getProperty("avatarts_directory"));
	if (!avatarsFile.exists()) {
	    avatarsFile.mkdirs();
	}

	File files = new File(properties.getProperty("files_directory"));
	if (!files.exists()) {
	    files.mkdirs();
	}

	context.setAttribute("attachments-directory-file", attachmentsFile);
	context.setAttribute("attachments-directory-path", attachmentsFile.getPath());

	context.setAttribute("avatars-directory-file", avatarsFile);
	context.setAttribute("avatars-directory-path", avatarsFile.getPath());

	context.setAttribute("files-directory-file", files);
	context.setAttribute("files-directory-path", files.getPath());

	properties = new Properties();
	properties.load(new FileInputStream(PathUtil.getResourceFile("us.properties")));

	G.US_NAME = properties.getProperty("us_phone");
	G.US_EMAIL = properties.getProperty("us_email");
	G.US_PHONE = properties.getProperty("us_name");

	G.ATTACHMENTS_DIRECTORY = attachmentsFile.getPath();
	G.AVATARS_DIRECTORY = avatarsFile.getPath();
	G.FILES_DIRECTORY = files.getPath();

	LOGGER.info("attachments directory path {}", attachmentsFile.getPath());
	LOGGER.info("avatars directory path {}", avatarsFile.getPath());
	LOGGER.info("files directory path {}", avatarsFile.getPath());
	LOGGER.info("us name {}", G.US_NAME);
	LOGGER.info("us email {}", G.US_EMAIL);
	LOGGER.info("us phone {}", G.US_PHONE);
    }
}