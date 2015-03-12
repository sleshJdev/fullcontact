package by.slesh.itechart.fullcontact.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.db.JdbcConnector;
import by.slesh.itechart.fullcontact.db.local.Database;
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.settings.G;
import by.slesh.itechart.fullcontact.util.HttpProcessUtil;
import by.slesh.itechart.fullcontact.util.PathUtil;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 *         Edit contact and save
 */
public class EditAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(EditAction.class);
    private final String SUCCESS = "You Can Edit Your Some Contact";
    private final String PROBLEM = "Some problem during editing/save contact !";

    private String action;
    private String idParameter;
    
    private String avatarsDirectory;
    private String attachmentsDirectory;

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
	ServletContext context = getRequest().getServletContext();
	avatarsDirectory = context.getAttribute("avatars-directory-path").toString();
	attachmentsDirectory = context.getAttribute("attachments-directory-path").toString();
	action = getRequest().getParameter("x");
	idParameter = getRequest().getParameter("id");
	
	LOGGER.info("END. action: {}, id: {}", action, idParameter);
    }

    private void editing() throws IOException, ServletException, ClassNotFoundException, SQLException {
	LOGGER.info("BEGIN");

	Long id = Long.parseLong(idParameter);

	if (id == null) {
	    throw new ServletException("the user with such name isn't found");
	}
	
	if (!G.CACHE.exists()) {
	    G.CACHE.mkdirs();
	}
	if(G.CACHE.list().length > 2){
	    FileUtils.deleteDirectory(G.CACHE);
	    G.CACHE.mkdirs();
	}
	
	ContactEntity contact = DaoFactory.getContactDao(true, true).get(id);
	File avatar = new File(String.format("%s%s%s", avatarsDirectory, File.separator, contact.getAvatarPath()));
	if (!avatar.exists()) {
	    contact.setAvatarPath(null);
	}else{
	    File cachedFile = new File(String.format("%s%s%s", G.CACHE.getPath(), File.separator, avatar.getName()));
	    if(!cachedFile.exists()){
		Files.copy(avatar.toPath(), cachedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    }
	    contact.setAvatarPath(cachedFile.getPath().substring(PathUtil.goToRoot().getPath().length()));
	}
	
	getRequest().setAttribute("nationalitiesList", Database.getNationalities());
	getRequest().setAttribute("familyStatusesList", Database.getFamilyStatuses());
	getRequest().setAttribute("sexesList", Database.getSexes());
	getRequest().setAttribute("phonesTypesList", Database.getPhoneTypes());
	getRequest().setAttribute("contact", contact);
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

	try{
	    if ((ids = HttpProcessUtil.checkForDeletingAtachments(getRequest())) != null) {
		    // open connection
		    EntityDao<AttachmentEntity> atachmentDao = DaoFactory.getAtachmentDao(true, false);
		    for (long id : ids) {
			AttachmentEntity atachment = atachmentDao.get(id);

			LOGGER.info("emtity is null? - {}", (atachment == null));

			File file = new File(String.format("%s%s%s", attachmentsDirectory, File.separator, atachment.getName()));
			if (file.delete()) {
			    LOGGER.info("atachment by path {} deleted successful", file.getPath());
			} else {
			    LOGGER.info("atachment by path {} not found or not exists", file.getPath());
			}
		    }
		    atachmentDao.deleteRange(contact.getId(), ids);
		}

		if ((ids = HttpProcessUtil.checkForDeletingPhones(getRequest())) != null) {
		    DaoFactory.getPhoneDao(true, true).deleteRange(contact.getId(), ids);
		}
	} catch (SQLException e) {
	    JdbcConnector.rollback();
	    throw new ServletException(e);
	} finally {
	    JdbcConnector.close();// close connection
	}

	getResponse().sendRedirect(getRequest().getHeader("referer"));

	LOGGER.info("END");
    }

    private List<File> processParts(Collection<Part> parts, ContactEntity contact) throws IOException,
	    ClassNotFoundException, SQLException {
	LOGGER.info("BEGIN");
	LOGGER.info("parts quantity: {}", parts == null ? null : parts.size());

	final Long contactId = Long.parseLong(idParameter);

	ContactDao dao = (ContactDao) DaoFactory.getContactDao(true, true);
	
	List<File> files = null;
	
	for (Part part : parts) {
	    // name of input field from
	    String name = valueOf(part, "name");
	
	    if (!StringUtils.isEmptyOrWhitespaceOnly(name)) {
		
		// name of file
		String fileName = valueOf(part, "filename");
		if (StringUtils.isEmptyOrWhitespaceOnly(fileName)) {
		    continue;
		}

		LOGGER.info("part name: {}", name);

		switch (name) {
		case "hidden-avatar-file":
		    LOGGER.info("hidden avatar file process...");
		    
		    // save avatar and get file
		    File avatarFile = processPart(part, avatarsDirectory, fileName);
		    
		    String newAvatarName = avatarFile.getName();
		    String currentAvatarName = dao.getAvatar(contactId);
		    
		    LOGGER.info("current name: {}", currentAvatarName);
		    LOGGER.info("new name: {}", newAvatarName);
		    
		    if (!StringUtils.isEmptyOrWhitespaceOnly(currentAvatarName)) {
			// created file for current avatar 
			File avatar = new File(String.format("%s%s%s", avatarsDirectory, File.separator, currentAvatarName));
			
			if (avatar.delete()) {
			    LOGGER.info("avatar by path {} deleted successful", avatar);

			    contact.setAvatarPath(null);
			} else {
			    LOGGER.info("avatar by path {} not found or not exists", avatar);
			}
		    }
		    
		    LOGGER.info("new avatar name: {}", newAvatarName);
		    
		    dao.setAvatar(contactId, newAvatarName);
		    break;
		case "atachment-file":
		    LOGGER.info("atachment file process...");

		    AttachmentEntity atachment = null;
		    for (AttachmentEntity item : contact.getAtachments()) {
			if (fileName.equals(item.getName())) {

			    LOGGER.info("origin name: {}, changed name: {}", fileName, item.getChangedName());

			    fileName = item.getChangedName();
			    atachment = item;
			}
		    }

		    File attachmentFile = processPart(part, attachmentsDirectory, fileName);
		    String newAttachmentName = attachmentFile.getName();

		    LOGGER.info("atachment file obtained., path to save: {}", attachmentFile);
		    
		    atachment.setName(newAttachmentName);
		    atachment.setChangedName(newAttachmentName);
		    
		    if (files == null) {
			files = new ArrayList<File>();
		    }

		    files.add(attachmentFile);

		    break;
		}
	    }
	}

	LOGGER.info("update atachments names....");

	/*
	 * 1. Rename according file on disc 
	 * 2. Update names atachments: 'simple name' -> 'salt + changed name'
	 */
	for (AttachmentEntity atachment : contact.getAtachments()) {
	    String name = atachment.getName();
	    String changedName = atachment.getChangedName();
	    String salt = atachment.getSalt();

	    String newName = String.format("%s%s", salt, changedName);

	    LOGGER.info("id: {}, name: {}, changedName: {}, salt: {}", atachment.getId(), name, changedName, salt);

	    if (!name.equals(changedName)) {
		// 1
		File file = new File(String.format("%s%s%s%s", attachmentsDirectory, File.separator, salt, name));

		if (file.exists()) {
		    File newFile = new File(String.format("%s%s%s", attachmentsDirectory, File.separator, newName));
		    file.renameTo(newFile);

		    LOGGER.info("old path: {}, new path: {}", file.getPath(), newFile.getPath());
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
	LOGGER.info("fileName without salt: {}", fileName);

	fileName = String.format("%s_%s", UUID.randomUUID().toString(), fileName);

	LOGGER.info("fileName with salt: {}", fileName);

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
