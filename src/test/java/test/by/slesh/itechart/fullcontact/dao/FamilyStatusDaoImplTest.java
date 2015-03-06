package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.Getable;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.FamilyStatusEntity;

public class FamilyStatusDaoImplTest {
    @Test
    public void testGetIdMarried() throws ClassNotFoundException, IOException, SQLException {
	Getable<FamilyStatusEntity> familyStatusDao = DaoFactory.getFamilyStatusDao(true, true);
	long id = familyStatusDao.getId("Married");
	assertEquals("Not equals ids", 1, id);
    }

    @Test
    public void testGetIdDivorced() throws ClassNotFoundException, IOException, SQLException {
	Getable<FamilyStatusEntity> familyStatusDao = DaoFactory.getFamilyStatusDao(true, true);
	long id = familyStatusDao.getId("Divorced");
	assertEquals("Not equals ids", 2, id);
    }

    @Test
    public void testGetIdWidower() throws ClassNotFoundException, IOException, SQLException {
	Getable<FamilyStatusEntity> familyStatusDao = DaoFactory.getFamilyStatusDao(true, true);
	long id = familyStatusDao.getId("Widower");
	assertEquals("Not equals ids", 3, id);
    }

    @Test
    public void testGetIdSingle() throws ClassNotFoundException, IOException, SQLException {
	Getable<FamilyStatusEntity> familyStatusDao = DaoFactory.getFamilyStatusDao(true, true);
	long id = familyStatusDao.getId("Single");
	assertEquals("Not equals ids", 4, id);
    }
    
    @Test
    public void testGetAll() throws ClassNotFoundException, IOException, SQLException{
	Getable<FamilyStatusEntity> familyStatusDao = DaoFactory.getFamilyStatusDao(true, true);
	List<FamilyStatusEntity> list = familyStatusDao.getAll();
	assertNotNull("List is null", list);
    }
    
    @Test
    public void testGetLimit() throws ClassNotFoundException, IOException, SQLException{
	Getable<FamilyStatusEntity> familyStatusDao = DaoFactory.getFamilyStatusDao(true, true);
	List<FamilyStatusEntity> list = familyStatusDao.getLimit(0, 3);
	assertEquals("Incorrect quantity", 3, list.size());
    }
}
