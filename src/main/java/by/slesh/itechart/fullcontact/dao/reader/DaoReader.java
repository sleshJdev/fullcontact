package by.slesh.itechart.fullcontact.dao.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import by.slesh.itechart.fullcontact.domain.Entity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public interface DaoReader<T extends Entity> {
    public T read(ResultSet resultSet) throws SQLException;
}
