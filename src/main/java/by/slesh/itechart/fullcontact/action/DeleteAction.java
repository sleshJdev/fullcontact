package by.slesh.itechart.fullcontact.action;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.AttachmentDao;
import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.dao.impl.ManyToManyDao;
import by.slesh.itechart.fullcontact.db.JdbcConnector;
import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.EmailEntity;
import by.slesh.itechart.fullcontact.settings.G;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 *         Delete contacts and letters
 */
public class DeleteAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeleteAction.class);
    private static final String SUCCESS1 = "Success Removed %s Letters And %s Attachments";
    private static final String SUCCESS2 = "Success Removed %s Contacts";
    private static final String STATUS = "No Emails For Delete";
    /**
     * Id of contacts for remove
     */
    private String[] ids;

    @Override
    public void execute() throws ServletException, IOException {
	getResponse().sendRedirect(getRequest().getHeader("referer"));
	if (ids == null || ids.length == 0) {
	    getRequest().setAttribute("status", STATUS);
	    return;
	}
	StringBuffer url = getRequest().getRequestURL();
	String whatDelete = url.substring(url.lastIndexOf("/") + 1, url.length());

	switch (whatDelete) {
	case "delete-contacts":
	    try {
		List<String> avatarsToDelete = new ArrayList<String>();
		List<AttachmentEntity> attachmentsToDelete = new ArrayList<AttachmentEntity>();
		// no close connection
		EntityDao<AttachmentEntity> attachmentDao = DaoFactory.getAtachmentDao(true, false);
		// no close connection
		EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, false);
		for (String item : ids) {
		    Long id = Long.parseLong(item);
		    if (id == null) {
			continue;
		    }

		    List<AttachmentEntity> part = ((AttachmentDao) attachmentDao).getAttachmentsOfContact(id);
		    if(part != null){
			
			LOGGER.info("part size: {}", part.size());
			
			for (AttachmentEntity attachmentEntity : part) {
			    LOGGER.info("atachment from part name, changedname, value, path: {}, {}, {}, {}", attachmentEntity.getName(), attachmentEntity.getChangedName(), attachmentEntity.getValue(), attachmentEntity.getPath());
			}
			
			attachmentsToDelete.addAll(part);
			
			LOGGER.info("quantity atachments to delete: {}", attachmentsToDelete.size());
		    }

		    String avatarPath = ((ContactDao) contactDao).getAvatar(id);
		    if(avatarPath != null){
			avatarsToDelete.add(avatarPath);

			LOGGER.info("quantity avatars to delete: {}", avatarsToDelete.size());
		    }
		    
		    contactDao.delete(id);
		}

		LOGGER.info("begin deleting files: {} avatar, {} atachments...", avatarsToDelete.size(), attachmentsToDelete.size());
		
		// remove !file! avatars
		for (String path : avatarsToDelete) {
		    removeFile(String.format("%s%s%s", G.AVATARS_DIRECTORY, File.separator, path));
		}

		// remove !file! atachments according contact
		removeAttachmets(attachmentsToDelete, G.ATTACHMENTS_DIRECTORY);
		getRequest().setAttribute("status", String.format(SUCCESS2, ids.length));
	    } catch (ClassNotFoundException | SQLException e) {
		throw new ServletException(e);
	    } finally {
		try {
		    // close current connection
		    JdbcConnector.close();
		} catch (SQLException e) {
		    throw new ServletException(e);
		}
	    }

	    break;
	case "delete-letters":
	    try {
		List<AttachmentEntity> attachmentsToDelete = new ArrayList<AttachmentEntity>();
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < ids.length; ++i) {
		    Long id = Long.parseLong(ids[i]);
		    if (id == null) {
			continue;
		    }
		    // not close current connection
		    List<AttachmentEntity> list = ManyToManyDao.getInstance(true, false).getAtachmentsOfEmail(id);
		    if(list != null){
			attachmentsToDelete.addAll(list);
		    }

		    listIds.add(id);
		}

		// close current connection
		EntityDao<EmailEntity> lettersDao = DaoFactory.getEmailDao(true, true);
		Long[] letterIds = new Long[listIds.size()];
		// remove letters
		lettersDao.deleteRange(G.MY_ID, listIds.toArray(letterIds));

		// remove !file! attachments according letters
		removeAttachmets(attachmentsToDelete, G.FILES_DIRECTORY);

		getRequest().setAttribute("status", String.format(SUCCESS1, letterIds.length, attachmentsToDelete.size()));
	    } catch (ClassNotFoundException | SQLException e) {
		throw new ServletException(e);
	    } finally {
		// close connection only
		// because statemets will be closed in dao
		try {
		    JdbcConnector.close();
		} catch (SQLException e) {
		    throw new ServletException(e);
		}
	    }
	    break;
	default:
	    throw new ServletException("bad request");
	}
    }

    private void removeFile(String path) {
	File file = new File(path);
	if (file.delete()) {
	    LOGGER.info("file {} by path {} deleted successful", file.getName(), file.getPath());
	} else {
	    LOGGER.info("file {} by path {} not found or not exists", file.getName(), file.getPath());
	}
    }

    private void removeAttachmets(List<AttachmentEntity> atachments, String folder) {
	for (AttachmentEntity item : atachments) {
	    // remove file
	    removeFile(String.format("%s%s%s", folder, File.separator, item.getName()));
	}
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	super.init(request, response);
	ids = getRequest().getParameterValues("id");
    }
}
