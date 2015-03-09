package by.slesh.itechart.fullcontact.db.local;

import java.util.List;

import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.FamilyStatusEntity;
import by.slesh.itechart.fullcontact.domain.NationalityEntity;
import by.slesh.itechart.fullcontact.domain.PhoneTypeEntity;
import by.slesh.itechart.fullcontact.domain.SexEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public final class Database {
    private static List<ContactEntity> birthdayMans;
    private static List<NationalityEntity> nationalities;
    private static List<SexEntity> sexes;
    private static List<PhoneTypeEntity> phoneTypes;
    private static List<FamilyStatusEntity> familyStatuses;

    public static List<ContactEntity> getBirthdayMans() {
	return birthdayMans;
    }

    public static void setBirthdayMans(List<ContactEntity> birthdayMans) {
	Database.birthdayMans = birthdayMans;
    }

    public static List<NationalityEntity> getNationalities() {
	return nationalities;
    }

    public static void setNationalities(List<NationalityEntity> nationalities) {
	Database.nationalities = nationalities;
    }

    public static List<SexEntity> getSexes() {
	return sexes;
    }

    public static void setSexes(List<SexEntity> sexes) {
	Database.sexes = sexes;
    }

    public static List<PhoneTypeEntity> getPhoneTypes() {
	return phoneTypes;
    }

    public static void setPhoneTypes(List<PhoneTypeEntity> phoneTypes) {
	Database.phoneTypes = phoneTypes;
    }

    public static List<FamilyStatusEntity> getFamilyStatuses() {
	return familyStatuses;
    }

    public static void setFamilyStatuses(List<FamilyStatusEntity> familyStatuses) {
	Database.familyStatuses = familyStatuses;
    }
}
