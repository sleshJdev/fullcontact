package by.slesh.itechart.fullcontact.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 *
 */
public interface Getable<T> {
    public T get(Long id) throws ClassNotFoundException, IOException, SQLException;

    public List<T> getAll() throws ClassNotFoundException, IOException, SQLException;

    public List<T> getLimit(long start, long size) throws ClassNotFoundException, IOException, SQLException;

    public Long getId(String fieldValue) throws ClassNotFoundException, IOException, SQLException;
}
