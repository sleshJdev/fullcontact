package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.Getable;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.SexEntity;

public class SexDaoImplTest {
    @Test
    public void testGetIdMale() throws ClassNotFoundException, IOException, SQLException {
	Getable<SexEntity> sexDao = DaoFactory.getSexDao(true, true);
	long id = sexDao.getId("Male");
	assertEquals("Not equals ids", 1, id);
    }

    @Test
    public void testGetIdFemale() throws ClassNotFoundException, IOException, SQLException {
	Getable<SexEntity> sexDao = DaoFactory.getSexDao(true, true);
	long id = sexDao.getId("Female");
	assertEquals("Not equals ids", 2, id);
    }
    
    @Test
    public void testGetAll() throws ClassNotFoundException, IOException, SQLException{
	Getable<SexEntity> sexDao = DaoFactory.getSexDao(true, true);
	List<SexEntity> list = sexDao.getAll();
	assertNotNull("List is null", list);
    }
    
    @Test
    public void testGetLimit() throws ClassNotFoundException, IOException, SQLException{
	Getable<SexEntity> sexDao = DaoFactory.getSexDao(true, true);
	List<SexEntity> list = sexDao.getLimit(0, 1);
	assertEquals("Incorrect quantity", 1, list.size());
    }
}
