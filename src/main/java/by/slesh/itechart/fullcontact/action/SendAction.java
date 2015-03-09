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

import by.slesh.itechart.fullcontact.dao.AtachmentDao;
import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EmailDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.ContactDaoImp;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.dao.impl.ManyToManyDao;
import by.slesh.itechart.fullcontact.db.JdbcConnector;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
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
 */
public class SendAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(SendAction.class);
    
    private String action;

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
	action = request.getParameter("x");// action kind
	request.setAttribute("status", "");

	LOGGER.info("x: {}", action);
	LOGGER.info("END");
    }
    
    
//    private void buildEmail(String message){
//	String root = PathUtil.getResourceFile("templates").getPath();
//	StringTemplateGroup group = new StringTemplateGroup(null, root, DefaultTemplateLexer.class);
//	StringTemplate template = group.getInstanceOf("congratulation");
//	template.setAttribute("TITLE", "Congratulation" : );
//	template.setAttribute("NAME", parameters.getName());
//	template.setAttribute("MESSAGE_BODY", parameters.getMessageBody());
//	template.setAttribute("US_FULL_NAME", parameters.getUsFullName());
//	template.setAttribute("US_PHONE", parameters.getUsPhone());
//	template.setAttribute("US_EMAIL", parameters.getUsEmail());
//    }

    private void sendAction() throws IOException, ServletException, ClassNotFoundException, ParseException, SQLException {
	final String destination = (String) getRequest().getServletContext().getAttribute("upload-directory-path");

	LOGGER.info("destination: {}", destination);
	
	String[] to = getRequest().getParameter("email-address").trim().replaceAll("\\s+", "").split(";");
	String subject = getRequest().getParameter("email-subject");
	String message = getRequest().getParameter("email-message");
	
	Collection<Part> parts = getRequest().getParts();
	List<File> files = new ArrayList<File>();

	LOGGER.info("obtained files: {}", parts.size());

	if (parts.size() > 0) {
	    files = processParts(parts, destination);
	}

	performeMailing(to, subject, message, files);

	LOGGER.info("obtained files: {}", parts.size());
    }

    private void performeMailing(String[] to, String subject, String message, List<File> files) throws ParseException,
	    ClassNotFoundException, IOException, SQLException {
	try {
	    List<Long> atachmentsId = new ArrayList<Long>();
	    AtachmentDao atachmentDao = (AtachmentDao) DaoFactory.getAtachmentDao(true, false);// not close after work!
	    for (File file : files) {
		AtachmentEntity atachmentEntity = new AtachmentEntity(-1, G.MY_ID, file.getName(), null, DateUtil.getSqlDate(), null);
		long atachmentId = atachmentDao.add(atachmentEntity);
		atachmentsId.add(atachmentId);
	    }
	    Sender sender = Sender.createSender(Sender.SSL);
	    EntityDao<EmailEntity> emailDao = DaoFactory.getEmailDao(true, false);// not close after work!
	    EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, false);//not close after work!
	    for (int t = 0; t < to.length; ++t) {
		StringTemplate template = new StringTemplate(message, DefaultTemplateLexer.class);
		String name = ((ContactDao) contactDao).getName(to[t]);
		template.setAttribute("NAME", name);
		template.setAttribute("US_FULL_NAME", "Putsykovich Eugene");
		template.setAttribute("US_PHONE", "2030327");
		template.setAttribute("US_EMAIL", "slesh@gmail.com");
		String body = template.toString();
		
		Email email = sender.createEmail();
		email.setTo(new String[] { to[t] });
		email.setSubject(subject);
		email.setBody(body);
		if (files != null && files.size() > 0) {
		    for (File file : files) {
			email.addAttachment(file);
		    }
		}
		sender.send(email);
		
		EmailEntity emailEntity = new EmailEntity(-1, G.MY_ID, subject, body, DateUtil.getSqlDate());
		long emailId = ((EmailDao) emailDao).add(emailEntity);
		emailEntity.setId(emailId);

		for (int i = 0; i < to.length; ++i) {
		    long contactId = contactDao.getId(to[i]);
		    ManyToManyDao.getInstance(true, false).doLinkEmailContact(emailId, contactId);// not close connection  after  work
		}
		for (Long atachmentId : atachmentsId) {
		    ManyToManyDao.getInstance(true, false).doLinkEmailAtachment(emailId, atachmentId);
		}
	    }
	} finally {
	    JdbcConnector.close();//close current opened connection
	}
    }

    private void defaultAction() throws IOException, ClassNotFoundException, SQLException {
	String[] contactsId = getRequest().getParameterValues("id");
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
	    LOGGER.info("RETURN: file name is null. return.");
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
	    final byte[] buffer = new byte[1024];

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

	LOGGER.info("END. file: {} uploaded successful!", fileName);

	return file;
    }

    private static final String getFileName(Part part) {
	LOGGER.info("BEGIN");

	for (String content : part.getHeader("content-disposition").split(";")) {
	    if (content.trim().startsWith("filename")) {
		String name = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
		LOGGER.info("END return {}", name);
		return name;
	    }
	}

	LOGGER.info("END return null");

	return null;
    }
}
