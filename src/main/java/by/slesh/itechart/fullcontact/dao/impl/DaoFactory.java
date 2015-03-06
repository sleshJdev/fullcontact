package by.slesh.itechart.fullcontact.dao.impl;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.domain.AtachmentEntity;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.FamilyStatusEntity;
import by.slesh.itechart.fullcontact.domain.NationalityEntity;
import by.slesh.itechart.fullcontact.domain.PhoneEntity;
import by.slesh.itechart.fullcontact.domain.PhoneTypeEntity;
import by.slesh.itechart.fullcontact.domain.SexEntity;

public final class DaoFactory {
    public static EntityDao<SexEntity> getSexDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	return new SexDaoImpl(isUseCurrentConnection, isCloseConnectionAfterWork);
    }

    public static EntityDao<NationalityEntity> getNationalityDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	return new NationalityDaoImpl(isUseCurrentConnection, isCloseConnectionAfterWork);
    }

    public static EntityDao<FamilyStatusEntity> getFamilyStatusDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	return new FamilyStatusDaoImpl(isUseCurrentConnection, isCloseConnectionAfterWork);
    }

    public static EntityDao<PhoneTypeEntity> getPhoneTypeDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	return new PhoneTypeDaoImpl(isUseCurrentConnection, isCloseConnectionAfterWork);
    }

    public static EntityDao<AtachmentEntity> getAtachmentDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	return new AtachmentDaoImpl(isUseCurrentConnection, isCloseConnectionAfterWork);
    }

    public static EntityDao<PhoneEntity>  getPhoneDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	return new PhoneDaoImpl(isUseCurrentConnection, isCloseConnectionAfterWork);
    }
    
    public static EntityDao<ContactEntity>  getContactDao(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	return new ContactDaoImp(isUseCurrentConnection, isCloseConnectionAfterWork);
    }
}
