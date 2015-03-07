package by.slesh.itechart.fullcontact.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.impl.ContactDaoImp;
import by.slesh.itechart.fullcontact.web.Email;
import by.slesh.itechart.fullcontact.web.Sender;

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
		getRequest().setAttribute("status", "All files successful loaded!");
	    }
	    if (StringUtils.isEmptyOrWhitespaceOnly(action)) {
		defaultAction();
	    }
	    getRequest().setAttribute("content", "send-email-form-content.jsp");
	    getRequest().setAttribute("title", "Send Mail Page");
	    getDispatcher().forward(getRequest(), getResponse());
	} catch (ClassNotFoundException | SQLException e) {
	    e.printStackTrace();
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

    private void sendAction() throws IOException, ServletException {
	final String path = (String) getRequest().getServletContext().getAttribute("upload-directory-path");
	LOGGER.info("destination: {}", path);

	String[] to = getRequest().getParameter("email-address").trim().replaceAll("\\s+", "").split(";");
	String subject = getRequest().getParameter("email-subject");
	String message = getRequest().getParameter("email-message");

	Collection<Part> parts = getRequest().getParts();
	List<File> files = new ArrayList<File>();
	
	LOGGER.info("obtained files: {}", parts.size());
	
	if (parts.size() > 0) {
	    files = processParts(parts, path);
	}

	if(files.size() > 0){
	    performeMailing(to, subject, message, files);
	}

	LOGGER.info("obtained files: {}", parts.size());
    }

    private void performeMailing(String[] to, String subject, String message, List<File> files) {
	Sender sender = Sender.createSender(Sender.SSL);
	for (int i = 0; i < to.length; ++i) {
	    Email email = sender.createEmail();
	    email.setTo(to);
	    email.setSubject(subject);
	    email.setBody(message);
	    if (files != null && files.size() > 0) {
		for (File file : files) {
		    email.addAttachment(file);
		}
	    }
	    sender.send(email);
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
	
	final String fileName = getFileName(part);
	if (fileName == null) {
	    LOGGER.info("END. file name is null. return.");
	    return null;
	}

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
