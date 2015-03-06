package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.EmailEntity;

public interface EmailDao {
    public long add(EmailEntity email) throws ClassNotFoundException, IOException, SQLException;

    public long update(ContactEntity contact) throws ClassNotFoundException, IOException, SQLException;

    public List<EmailEntity> gets(long contactId) throws ClassNotFoundException, IOException, SQLException;
}
