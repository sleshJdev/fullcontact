package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;

import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public interface AtachmentDao {
    public long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;
}
