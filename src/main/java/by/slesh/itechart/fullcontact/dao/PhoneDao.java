package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;

import by.slesh.itechart.fullcontact.domain.ContactEntity;

public interface PhoneDao {
    public long add(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;
}
