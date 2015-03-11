package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import by.slesh.itechart.fullcontact.domain.AttachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public interface AttachmentDao {
    public Long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public Long add(AttachmentEntity atachmentEntity) throws ClassNotFoundException, IOException, SQLException;

    public Long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public List<AttachmentEntity> getAttachmentsOfContact(Long contactId) throws SQLException, ClassNotFoundException,
	    IOException;
}
