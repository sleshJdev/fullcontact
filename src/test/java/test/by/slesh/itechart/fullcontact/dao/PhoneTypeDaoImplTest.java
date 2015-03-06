package test.by.slesh.itechart.fullcontact.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import by.slesh.itechart.fullcontact.dao.Getable;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.PhoneTypeEntity;

public class PhoneTypeDaoImplTest {
    @Test
    public void testGetIdHome() throws ClassNotFoundException, IOException, SQLException {
	Getable<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, true);
	long id = phoneTypeDao.getId("Home");
	assertEquals("Not equal ids", 2, id);
    }

    @Test
    public void testGetIdMobile() throws ClassNotFoundException, IOException, SQLException {
	Getable<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, true);
	long id = phoneTypeDao.getId("Mobile");
	assertEquals("Not equal ids", 1, id);
    }
    
    @Test
    public void testGetAll() throws ClassNotFoundException, IOException, SQLException{
	Getable<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, true);
	List<PhoneTypeEntity> list = phoneTypeDao.getAll();
	assertNotNull("List is null", list);
    }
    
    @Test
    public void testGetLimit() throws ClassNotFoundException, IOException, SQLException{
	Getable<PhoneTypeEntity> phoneTypeDao = DaoFactory.getPhoneTypeDao(true, true);
	List<PhoneTypeEntity> list = phoneTypeDao.getLimit(0, 1);
	assertNotNull("List is null", list);
    }
}
