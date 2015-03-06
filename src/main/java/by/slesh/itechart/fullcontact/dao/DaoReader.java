package by.slesh.itechart.fullcontact.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import by.slesh.itechart.fullcontact.domain.Entity;

public interface DaoReader<T extends Entity> {
    public T read(ResultSet resultSet) throws SQLException;
}
