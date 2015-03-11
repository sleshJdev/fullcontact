package by.slesh.itechart.fullcontact.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Putsykovich(slesh) Feb 14, 2015
 *
 *         Contact entity
 */
public class ContactEntity extends Entity {
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private String avatarPath;
    private String sex;
    private String nationality;
    private String familyStatus;
    private String webSite;
    private String emailAddress;
    private String currentEmployment;
    private String country;
    private String city;
    private String street;
    private String house;
    private String block;
    private String apartment;
    private String cityIndex;
    private List<PhoneEntity> phones = new ArrayList<PhoneEntity>();
    private List<AttachmentEntity> atachments = new ArrayList<AttachmentEntity>();

    public ContactEntity() {
    }

    public ContactEntity(Long id, String firstName, String lastName, String middleName, Date dateOfBirth,
	    String avatarPath, String sex, String nationality, String familyStatus, String webSite,
	    String emailAddress, String currentEmployment, String country, String city, String street, String house,
	    String block, String apartment, String cityIndex, List<PhoneEntity> phones, List<AttachmentEntity> atachments) {
	super(id, firstName);
	this.middleName = middleName;
	this.dateOfBirth = dateOfBirth;
	this.avatarPath = avatarPath;
	this.sex = sex;
	this.nationality = nationality;
	this.familyStatus = familyStatus;
	this.webSite = webSite;
	this.emailAddress = emailAddress;
	this.currentEmployment = currentEmployment;
	this.country = country;
	this.city = city;
	this.street = street;
	this.house = house;
	this.block = block;
	this.apartment = apartment;
	this.cityIndex = cityIndex;
	this.phones = phones;
	this.atachments = atachments;
    }

    public String getFirstName() {
	return getValue();
    }

    public void setFirstName(String firstName) {
	setValue(firstName);
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getMiddleName() {
	return middleName;
    }

    public void setMiddleName(String middleName) {
	this.middleName = middleName;
    }

    public Date getDateOfBirth() {
	return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
    }

    public String getAvatarPath() {
	return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
	this.avatarPath = avatarPath;
    }

    public String getSex() {
	return sex;
    }

    public void setSex(String sex) {
	this.sex = sex;
    }

    public String getNationality() {
	return nationality;
    }

    public void setNationality(String nationality) {
	this.nationality = nationality;
    }

    public List<PhoneEntity> getPhones() {
	return phones;
    }

    public void setPhones(List<PhoneEntity> phones) {
	if (phones == null) {
	    return;
	}
	for (PhoneEntity phone : phones) {
	    addPhone(phone);
	}
    }

    public void addPhone(PhoneEntity phone) {
	phones.add(phone);
    }

    public List<AttachmentEntity> getAtachments() {
	return atachments;
    }

    public void setAtachments(List<AttachmentEntity> atachments) {
	if (atachments == null) {
	    return;
	}
	for (AttachmentEntity atachment : atachments) {
	    addAtachment(atachment);
	}
    }

    public void addAtachment(AttachmentEntity atachment) {
	atachments.add(atachment);
    }

    public String getFamilyStatus() {
	return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
	this.familyStatus = familyStatus;
    }

    public String getWebSite() {
	return webSite;
    }

    public void setWebSite(String webSite) {
	this.webSite = webSite;
    }

    public String getEmailAddress() {
	return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
    }

    public String getCurrentEmployment() {
	return currentEmployment;
    }

    public void setCurrentEmployment(String currentEmployment) {
	this.currentEmployment = currentEmployment;
    }

    public String getStreet() {
	return street;
    }

    public void setStreet(String street) {
	this.street = street;
    }

    public String getHouse() {
	return house;
    }

    public void setHouse(String house) {
	this.house = house;
    }

    public String getBlock() {
	return block;
    }

    public void setBlock(String block) {
	this.block = block;
    }

    public String getApartment() {
	return apartment;
    }

    public void setApartment(String apartment) {
	this.apartment = apartment;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getCityIndex() {
	return cityIndex;
    }

    public void setCityIndex(String cityIndex) {
	this.cityIndex = cityIndex;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder("\n\t***Contact id={ " + id + " }***");
	sb.append("\n\t first name: ");
	sb.append(getFirstName());
	sb.append("\n\t last name: ");
	sb.append(lastName);
	sb.append("\n\t middle name: ");
	sb.append(middleName);
	sb.append("\n\t date of birth: ");
	sb.append(dateOfBirth);
	sb.append("\n\t sex: ");
	sb.append(sex);
	sb.append("\n\t nationality: ");
	sb.append(nationality);
	sb.append("\n\t avatart path: ");
	sb.append(avatarPath);

	if (phones != null) {
	    for (PhoneEntity phone : phones) {
		sb.append("\n\t");
		sb.append(phone);
	    }
	}

	if (atachments != null) {
	    for (AttachmentEntity atachment : atachments) {
		sb.append("\n\t");
		sb.append(atachment);
	    }
	}

	sb.append("\n\t family_status: ");
	sb.append(familyStatus);
	sb.append("\n\t webSite: ");
	sb.append(webSite);
	sb.append("\n\t emailAddress: ");
	sb.append(emailAddress);
	sb.append("\n\t currentEmlpoyment: ");
	sb.append(currentEmployment);
	sb.append("\n\t country: ");
	sb.append(country);
	sb.append("\n\t city: ");
	sb.append(city);
	sb.append("\n\t street: ");
	sb.append(street);
	sb.append("\n\t house: ");
	sb.append(house);
	sb.append("\n\t block: ");
	sb.append(block);
	sb.append("\n\t apartment: ");
	sb.append(apartment);
	sb.append("\n\t index: ");
	sb.append(cityIndex);

	return sb.toString();
    }
}
