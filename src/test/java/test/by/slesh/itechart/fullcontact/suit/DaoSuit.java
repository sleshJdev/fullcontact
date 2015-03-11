package test.by.slesh.itechart.fullcontact.suit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.by.slesh.itechart.fullcontact.dao.AtachmentDaoImplTest;
import test.by.slesh.itechart.fullcontact.dao.ContactDaoImplTest;
import test.by.slesh.itechart.fullcontact.dao.EmailDaoImplTest;
import test.by.slesh.itechart.fullcontact.dao.FamilyStatusDaoImplTest;
import test.by.slesh.itechart.fullcontact.dao.ManyToManyDaoTest;
import test.by.slesh.itechart.fullcontact.dao.PhoneTypeDaoImplTest;
import test.by.slesh.itechart.fullcontact.dao.SexDaoImplTest;

@RunWith(Suite.class)
@SuiteClasses({
    SexDaoImplTest.class,
    PhoneTypeDaoImplTest.class,
    ManyToManyDaoTest.class,
    FamilyStatusDaoImplTest.class,
    EmailDaoImplTest.class,
    AtachmentDaoImplTest.class,
    PhoneTypeDaoImplTest.class,
    ContactDaoImplTest.class})
public class DaoSuit {
    
}
