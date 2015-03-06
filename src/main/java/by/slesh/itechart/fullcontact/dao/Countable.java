package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 *
 */
public interface Countable {
    public long count() throws ClassNotFoundException, IOException, SQLException;
}
