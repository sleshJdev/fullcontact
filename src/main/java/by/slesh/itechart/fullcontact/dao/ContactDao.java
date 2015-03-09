package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Feb 13, 2015
 *
 *         Contact data access object interface
 */
public interface ContactDao {
    public void setAvatar(long contactId, String pathToSave) throws ClassNotFoundException, IOException, SQLException;

    public String getAvatar(long contactId) throws ClassNotFoundException, IOException, SQLException;

    public void add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public void update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public String getName(String email) throws ClassNotFoundException, IOException, SQLException;
    
    public String getEmail(long contactId) throws ClassNotFoundException, IOException, SQLException;

    public List<ContactEntity> search(ContactEntity contact, boolean isLessDate) throws ClassNotFoundException, IOException, SQLException;

    public ContactEntity getContact(long id) throws ClassNotFoundException, IOException, SQLException;
}
