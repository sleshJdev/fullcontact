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
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.FamilyStatusEntity;
import by.slesh.itechart.fullcontact.domain.NationalityEntity;
import by.slesh.itechart.fullcontact.domain.PhoneTypeEntity;
import by.slesh.itechart.fullcontact.domain.SexEntity;
import by.slesh.itechart.fullcontact.util.HttpProcessUtil;
import by.slesh.itechart.fullcontact.util.PathUtil;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public class EditAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(EditAction.class);
    private final String SUCCESS = "Success statuc from " + getClass().getSimpleName() + "!";
    private final String PROBLEM = "Some problem during editing/save contact " + getClass().getSimpleName() + "!";

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
	    List<NationalityEntity> list1 = DaoFactory.getNationalityDao(true, true).getAll();
	    List<FamilyStatusEntity> list2 = DaoFactory.getFamilyStatusDao(true, true).getAll();
	    List<SexEntity> list3 = DaoFactory.getSexDao(true, true).getAll();
	    List<PhoneTypeEntity> list4 = DaoFactory.getPhoneTypeDao(true, true).getAll();
	    getRequest().setAttribute("nationalitiesList", list1);
	    getRequest().setAttribute("familyStatusesList", list2);
	    getRequest().setAttribute("sexesList", list3);
	    getRequest().setAttribute("phonesTypesList", list4);
	    getRequest().setAttribute("contact", contact);
	} catch (ClassNotFoundException | SQLException e) {
	    throw new ServletException(e);
	}
	
	getRequest().setAttribute("content", "edit-contact-form-content.jsp");
	getRequest().setAttribute("title", "Edit Contact Page");
	getDispatcher().forward(getRequest(), getResponse());

	LOGGER.info("END");
    }

    private void save() throws Exception {
	LOGGER.info("BEGIN");
	
	Collection<Part> parts = getRequest().getParts();
	processParts(parts);

	ContactEntity contact = HttpProcessUtil.readContactFrom(getRequest());
	((ContactDao) DaoFactory.getContactDao(true, true)).update(contact);

	long[] ids = null;
	
	if ((ids = HttpProcessUtil.checkForDeletingAtachments(getRequest())) != null) {
	    DaoFactory.getAtachmentDao(true, true).deleteRange(contact.getId(), ids);
	}
	
	if ((ids = HttpProcessUtil.checkForDeletingPhones(getRequest())) != null) {
	    DaoFactory.getPhoneDao(true, true).deleteRange(contact.getId(), ids);
	}
	getResponse().sendRedirect("show");

	LOGGER.info("END");
    }

    private List<File> processParts(Collection<Part> parts) throws IOException, ClassNotFoundException, SQLException {
	ContactDao dao = (ContactDao) DaoFactory.getContactDao(true, true);
	ServletContext context = getRequest().getServletContext();
	List<File> files = new ArrayList<File>();
	final long contactId = Long.parseLong(idParameter);
	final String publicDirectory = context.getAttribute("public-directory-path").toString();
	for (Part part : parts) {
	    /*
	     * name of input field from
	     */
	    String name = valueOf(part, "name");
	    if (!StringUtils.isEmptyOrWhitespaceOnly(name) && name.equals("hidden-avatar-file")) {
		/*
		 * name of file name
		 */
		String fileName = valueOf(part, "filename");
		if (StringUtils.isEmptyOrWhitespaceOnly(fileName)) {
		    continue;
		}
		/*
		 * .../'application-name'
		 */
		String root = PathUtil.goToRoot().getPath();
		/*
		 * /folder1/folder2/.../'application-name'/'path-to-target-folder'/'file-name'
		 */
		File file = processPart(part, publicDirectory, fileName);
		/*
		 *  /'path-to-target-folder'/'file-name'
		 */
		String pathSave = file.getPath().substring(root.length());
		/*
		 * // same as pathSave, but from db
		 */
		String pathAvatar = dao.getAvatar(contactId);
		if (!StringUtils.isEmptyOrWhitespaceOnly(pathAvatar)) {
		    /*
		     * /folder1/folder2/.../'application-name'/'path-to-target-folder'/'file-name'
		     */
		    String fullPath = String.format("%s%s", root, pathAvatar);
		    File avatar = new File(fullPath);
		    if(avatar.delete()){
			LOGGER.info("avatar by path {} deleted successful", fullPath);
		    }else{
			LOGGER.info("avatar by path {} not found", fullPath);
		    }

		}
		dao.setAvatar(contactId, pathSave);
		break;
	    }
	}
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
