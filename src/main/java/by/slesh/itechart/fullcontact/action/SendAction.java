package by.slesh.itechart.fullcontact.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.AttachmentDao;
import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EmailDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.ContactDaoImp;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.dao.impl.ManyToManyDao;
import by.slesh.itechart.fullcontact.db.JdbcConnector;
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.EmailEntity;
import by.slesh.itechart.fullcontact.settings.G;
import by.slesh.itechart.fullcontact.util.DateUtil;
import by.slesh.itechart.fullcontact.web.Email;
import by.slesh.itechart.fullcontact.web.Sender;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 * 
 *         Send mails to contacts
 */
public class SendAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(SendAction.class);

    private String action;
    private String filesDirectory;
    
    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	try {
	    if (!StringUtils.isEmptyOrWhitespaceOnly(action) && action.equals("send")) {
		sendAction();
		getRequest().setAttribute("status", "Your Email Send Successful!");
	    } else {
		defaultAction();
	    }
	    getRequest().setAttribute("content", "send-email-form-content.jsp");
	    getRequest().setAttribute("title", "Send Mail Page");
	    getDispatcher().forward(getRequest(), getResponse());
	} catch (ClassNotFoundException | SQLException | ParseException e) {
	    throw new ServletException(e);
	}

	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	LOGGER.info("BEGIN");

	super.init(request, response);
	action = request.getParameter("x");
	request.setAttribute("status", "");
	filesDirectory = getRequest().getServletContext().getAttribute("files-directory-path").toString();
	
	LOGGER.info("END. x: {}", action);
    }

    private void sendAction() throws IOException, ServletException, ClassNotFoundException, ParseException,
	    SQLException {
	String[] to = getRequest().getParameter("email-address").trim().replaceAll("\\s+", "").split(";");
	String subject = getRequest().getParameter("email-subject");
	String message = getRequest().getParameter("email-message");

	Collection<Part> parts = getRequest().getParts();
	List<File> files = new ArrayList<File>();

	if (parts.size() > 0) {
	    files = processParts(parts, filesDirectory);
	}

	performeMailing(to, subject, message, files);
    }

    private void performeMailing(String[] to, String subject, String message, List<File> files) throws ParseException,
	    ClassNotFoundException, IOException, SQLException, ServletException {
	try {
	    List<Long> atachmentsId = new ArrayList<Long>();
	    // not close connection after work
	    AttachmentDao atachmentDao = (AttachmentDao) DaoFactory.getAtachmentDao(true, false);
	    for (File file : files) {
		AttachmentEntity atachmentEntity = new AttachmentEntity(null, G.MY_ID, file.getName(), null, DateUtil.getSqlDate(), null);
		long atachmentId = atachmentDao.add(atachmentEntity);
		atachmentsId.add(atachmentId);
	    }
	    Sender sender = Sender.createSender(Sender.SSL);
	    // not close connection after work
	    EntityDao<EmailEntity> emailDao = DaoFactory.getEmailDao(true, false);
	    // not close connection after work
	    EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, false);
	    for (String t : to) {
		StringTemplate template = new StringTemplate(message, DefaultTemplateLexer.class);
		String name = ((ContactDao) contactDao).getName(t);
		template.setAttribute("NAME", name);
		template.setAttribute("US_FULL_NAME", G.US_NAME);
		template.setAttribute("US_PHONE", G.US_PHONE);
		template.setAttribute("US_EMAIL", G.US_EMAIL);
		String body = template.toString();

		Email email = sender.createEmail();
		email.setTo(new String[] { t });
		email.setSubject(subject);
		email.setBody(body);
		if (files != null && files.size() > 0) {
		    for (File file : files) {
			email.addAttachment(file);
		    }
		}
		sender.send(email);

		EmailEntity emailEntity = new EmailEntity(null, G.MY_ID, subject, body, DateUtil.getSqlDate());
		long emailId = ((EmailDao) emailDao).add(emailEntity);
		emailEntity.setId(emailId);

		for (int i = 0; i < to.length; ++i) {
		    long contactId = contactDao.getId(to[i]);
		    // not close connection after work
		    ManyToManyDao.getInstance(true, false).doLinkEmailContact(emailId, contactId);
		}
		for (Long atachmentId : atachmentsId) {
		    // not close connection after work
		    ManyToManyDao.getInstance(true, false).doLinkEmailAtachment(emailId, atachmentId);
		}
	    }
	} finally {
	    JdbcConnector.close();// close current opened connection
	}
    }

    private void defaultAction() throws IOException, ClassNotFoundException, SQLException {
	String[] contactsId = getRequest().getParameterValues("id");
	if(contactsId == null || (contactsId.length == 1 && contactsId.equals(G.MY_ID))){
	    return;
	}
	List<String> emails = new ArrayList<String>(contactsId.length);
	ContactDao contactDao = new ContactDaoImp();
	for (int i = 0; i < contactsId.length; i++) {
	    long id = Long.parseLong(contactsId[i]);
	    String email = contactDao.getEmail(id);
	    emails.add(email);
	}
	getRequest().setAttribute("emails", emails);
    }

    private static List<File> processParts(Collection<Part> parts, final String destination) throws IOException {
	List<File> files = new ArrayList<File>();
	for (Part part : parts) {
	    if (part != null) {
		File file = processPart(part, destination);
		if (file != null) {
		    files.add(file);
		}
	    }
	}

	return files;
    }

    private static final File processPart(Part part, final String destination) throws IOException {
	LOGGER.info("BEGIN");

	String fileName = getFileName(part);
	if (StringUtils.isEmptyOrWhitespaceOnly(fileName)) {
	    LOGGER.info("RETURN: file name is null or empty.");
	    return null;
	}
	fileName = String.format("%s_%s", UUID.randomUUID().toString(), fileName);
	OutputStream out = null;
	InputStream fileContent = null;
	File file = null;
	try {
	    file = new File(destination + File.separator + fileName);

	    LOGGER.info("file name: {}", fileName);

	    out = new FileOutputStream(file);
	    fileContent = part.getInputStream();

	    int read = 0;
	    final byte[] buffer = new byte[2048];

	    while ((read = fileContent.read(buffer)) != -1) {
		out.write(buffer, 0, read);
	    }
	} finally {
	    if (out != null) {
		out.close();
	    }
	    if (fileContent != null) {
		fileContent.close();
	    }
	}

	LOGGER.info("END. file {} uploaded successful!", fileName);
	return file;
    }

    private static final String getFileName(Part part) {
	LOGGER.info("BEGIN");

	for (String content : part.getHeader("content-disposition").split(";")) {
	    if (content.trim().startsWith("filename")) {
		String name = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
		return name;
	    }
	}

	LOGGER.info("END return null");
	return null;
    }
}
