package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;

public interface Deletable {
    public void delete(Long id) throws ClassNotFoundException, IOException, SQLException;

    public long deleteRange(Long contactId, Long[] ids) throws ClassNotFoundException, IOException, SQLException;
}
