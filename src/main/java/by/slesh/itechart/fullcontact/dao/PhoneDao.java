package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;

import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public interface PhoneDao {
    public Long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public Long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;
}
