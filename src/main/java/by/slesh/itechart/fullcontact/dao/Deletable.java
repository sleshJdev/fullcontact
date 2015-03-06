package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;

public interface Deletable {
    public void delete(long id) throws ClassNotFoundException, IOException, SQLException;
    
    public long deleteRange(long contactId, long[] ids) throws ClassNotFoundException, IOException, SQLException;
}
