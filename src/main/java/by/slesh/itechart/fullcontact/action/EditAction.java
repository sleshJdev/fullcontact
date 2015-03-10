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
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.db.JdbcConnector;
import by.slesh.itechart.fullcontact.db.local.Database;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.HttpProcessUtil;
import by.slesh.itechart.fullcontact.util.PathUtil;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public class EditAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(EditAction.class);
    private final String SUCCESS = "You Can Edit Your Some Contact";
    private final String PROBLEM = "Some problem during editing/save contact !";

    private String action;
    private String idParameter;

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	try {
	    if (StringUtils.isEmptyOrWhitespaceOnly(idParameter)) {
		getRequest().setAttribute("status", PROBLEM);
	    } else {
		if (!StringUtils.isEmptyOrWhitespaceOnly(action) && action.equals("save")) {
		    save();
		} else {
		    editing();
		}
		getRequest().setAttribute("status", SUCCESS);
	    }
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	LOGGER.info("BEGIN");

	super.init(request, response);
	action = getRequest().getParameter("x");
	idParameter = getRequest().getParameter("id");

	LOGGER.info("action: " + action + ", id: " + idParameter);
	LOGGER.info("END");
    }

    private void editing() throws IOException, ServletException {
	LOGGER.info("BEGIN");

	long id = Long.parseLong(idParameter);

	try {
	    ContactEntity contact = DaoFactory.getContactDao(true, true).get(id);
	    File avatar = new File(String.format("%s%s", PathUtil.goToRoot(), contact.getAvatarPath()));
	    if (!avatar.exists()) {
		contact.setAvatarPath(null);
	    }
	    getRequest().setAttribute("nationalitiesList", Database.getNationalities());
	    getRequest().setAttribute("familyStatusesList", Database.getFamilyStatuses());
	    getRequest().setAttribute("sexesList", Database.getSexes());
	    getRequest().setAttribute("phonesTypesList", Database.getPhoneTypes());
	    getRequest().setAttribute("contact", contact);
	} catch (ClassNotFoundException | SQLException e) {
	    throw new ServletException(e);
	}

	getRequest().setAttribute("content", "edit-contact-form-content.jsp");
	getRequest().setAttribute("title", "Edit Contact Page");
	getDispatcher().forward(getRequest(), getResponse());

	LOGGER.info("END");
    }

    ContactEntity contact;

    private void save() throws Exception {
	LOGGER.info("BEGIN");

	contact = HttpProcessUtil.readContactFrom(getRequest());

	Collection<Part> parts = getRequest().getParts();
	processParts(parts, contact);

	((ContactDao) DaoFactory.getContactDao(true, true)).update(contact);

	Long[] ids = null;

	if ((ids = HttpProcessUtil.checkForDeletingAtachments(getRequest())) != null) {
	    final String uploadDirectory = getRequest().getServletContext().getAttribute("upload-directory-path")
		    .toString();
	    EntityDao<AtachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, false);// open
											      // connection
	    for (long id : ids) {
		AtachmentEntity atachment = atachmentDao.get(id);

		LOGGER.info("emtity is null? - {}", (atachment == null));

		File file = new File(String.format("%s%s%s", uploadDirectory, File.separator, atachment.getName()));
		if (file.delete()) {

		    LOGGER.info("atachment by path {} deleted successful", file.getPath());

		} else {

		    LOGGER.info("atachment by path {} not found or not exists", file.getPath());

		}
	    }
	    JdbcConnector.close();// close connection
	    atachmentDao.deleteRange(contact.getId(), ids);
	}

	if ((ids = HttpProcessUtil.checkForDeletingPhones(getRequest())) != null) {
	    DaoFactory.getPhoneDao(true, true).deleteRange(contact.getId(), ids);
	}
	getResponse().sendRedirect(getRequest().getHeader("referer"));

	LOGGER.info("END");
    }

    private List<File> processParts(Collection<Part> parts, ContactEntity contact) throws IOException,
	    ClassNotFoundException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("parts quantity: {}", parts == null ? null : parts.size());

	ContactDao dao = (ContactDao) DaoFactory.getContactDao(true, true);
	ServletContext context = getRequest().getServletContext();
	final long contactId = Long.parseLong(idParameter);
	final String publicDirectory = context.getAttribute("public-directory-path").toString();
	final String uploadDirectory = context.getAttribute("upload-directory-path").toString();
	final String root = PathUtil.goToRoot().getPath();// .../'application-name'
	List<File> files = null;
	File file = null;
	String pathSave = null;

	for (Part part : parts) {
	    String name = valueOf(part, "name");// name of input field from
	    if (!StringUtils.isEmptyOrWhitespaceOnly(name)) {
		String fileName = valueOf(part, "filename");// name of file
		if (StringUtils.isEmptyOrWhitespaceOnly(fileName)) {
		    continue;
		}

		LOGGER.info("part name: {}", name);

		switch (name) {
		case "hidden-avatar-file":
		    file = processPart(part, publicDirectory, fileName);// saved file
		    pathSave = file.getPath().substring(root.length());// "/'path-to-target-folder'/'file-name'"
		    String pathAvatar = dao.getAvatar(contactId);// same as pathSave, but from db 
		    LOGGER.info("path save avatar: {}", pathSave);

		    if (!StringUtils.isEmptyOrWhitespaceOnly(pathAvatar)) {
			String fullPath = String.format("%s%s", root, pathAvatar);// absolute path to current avatar
			File avatar = new File(fullPath);
			if (avatar.delete()) {

			    LOGGER.info("avatar by path {} deleted successful", fullPath);

			    contact.setAvatarPath(null);
			} else {

			    LOGGER.info("avatar by path {} not found or not exists", fullPath);

			}
		    }
		    dao.setAvatar(contactId, pathSave);
		    break;
		case "atachment-file":
		    LOGGER.info("atachment file process...");

		    AtachmentEntity atachment = null;
		    for (AtachmentEntity item : contact.getAtachments()) {
			if (fileName.equals(item.getName())) {

			    LOGGER.info("origin name: {}, changed name: {}", fileName, item.getChangedName());

			    fileName = item.getChangedName();
			    atachment = item;
			}
		    }

		    file = processPart(part, uploadDirectory, fileName);
		    pathSave = file.getPath().substring(root.length());// /'path-to-target-folder'/'file-name'

		    LOGGER.info("atachment file obtained: {}, path to save: {}", file, pathSave);

		    atachment.setName(file.getName());
		    atachment.setChangedName(file.getName());

		    if (files == null) {
			files = new ArrayList<File>();
		    }

		    files.add(file);

		    break;
		}
	    }
	}

	LOGGER.info("update atachments names....");

	/*
	 * 1. Rename according file on disc 
	 * 2. Update names atachments: 'simple name' -> 'salt + changed name'
	 */
	for (AtachmentEntity atachment : contact.getAtachments()) {
	    String name = atachment.getName();
	    String changedName = atachment.getChangedName();
	    String salt = atachment.getSalt();

	    String newName = String.format("%s%s", salt, changedName);

	    LOGGER.info("id: {}, name: {}, changedName: {}, salt: {}", atachment.getId(), name, changedName, salt);
	    LOGGER.info("new name: {}", newName);

	    if (!name.equals(changedName)) {
		// 1
		file = new File(String.format("%s%s%s%s", uploadDirectory, File.separator, salt, name));

		LOGGER.info("origin file: {}", file.getPath());

		if (file.exists()) {
		    File newFile = new File(String.format("%s%s%s", uploadDirectory, File.separator, newName));
		    file.renameTo(newFile);

		    LOGGER.info("old name: {}", file.getPath());
		    LOGGER.info("new name: {}", newFile.getPath());
		    LOGGER.info("changed name successful!");
		}
	    }
	    // 2
	    atachment.setName(newName);
	    atachment.setChangedName(newName);
	}

	LOGGER.info("END");
	return files;
    }

    private static final File processPart(Part part, final String destination, String fileName) throws IOException {
	LOGGER.info("BEGIN");

	LOGGER.info("destination: {}", destination);
	LOGGER.info("fileName: {}", fileName);

	fileName = String.format("%s_%s", UUID.randomUUID().toString(), fileName);

	OutputStream out = null;
	InputStream fileContent = null;
	File file = null;

	try {
	    file = new File(String.format("%s%s%s", destination, File.separator, fileName));

	    out = new FileOutputStream(file);
	    fileContent = part.getInputStream();

	    int read = 0;
	    final byte[] bytes = new byte[1024];

	    while ((read = fileContent.read(bytes)) != -1) {
		out.write(bytes, 0, read);
	    }
	} finally {
	    if (out != null) {
		out.close();
	    }
	    if (fileContent != null) {
		fileContent.close();
	    }
	}

	LOGGER.info("destination file: {}", file);
	LOGGER.info("file {} uploaded successful!", fileName);
	LOGGER.info("END");

	return file;
    }

    private static String valueOf(Part part, String key) {
	String source = part.getHeader("content-disposition");
	String value = null;
	for (String token : source.split(";")) {
	    if (token.trim().startsWith(key)) {
		value = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
		break;
	    }
	}
	return value;
    }

    // private void describePart(Part part) {
    // LOGGER.info("BEGIN");
    // LOGGER.info("part info:");
    // LOGGER.info("\t{}: {}", "conten type", part.getContentType());
    // LOGGER.info("\t{}: {}", "name", part.getName());
    // LOGGER.info("\t{}: {}", "size", part.getSize());
    // LOGGER.info("part headers:");
    //
    // Collection<String> headers = part.getHeaderNames();
    // for (String header : headers) {
    // LOGGER.info("\t{}: {}", header, part.getHeader(header));
    // }
    //
    // LOGGER.info("END");
    // }
}
