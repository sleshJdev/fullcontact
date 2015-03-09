package by.slesh.itechart.fullcontact.domain;

public class PhoneEntity extends Entity {
    private Long contactId;
    private String countryCode;
    private String operatorCode;
    private String type;
    private String comment;

    public PhoneEntity() {
    }

    public PhoneEntity(Long id, Long contactId, String countryCode,
	    String operatorCode, String value, String type, String comment) {
	super(id, value);
	this.contactId = contactId;
	this.value = value;
	this.comment = comment;
	this.countryCode = countryCode;
	this.operatorCode = operatorCode;
    }

    public Long getContactId() {
	return contactId;
    }

    public void setContactId(Long contactId) {
	this.contactId = contactId;
    }

    public String getCountryCode() {
	return countryCode;
    }

    public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
    }

    public String getOperatorCode() {
	return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
	this.operatorCode = operatorCode;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    @Override
    public String toString() {
	return "phone:: [id: " + id + ", contactId: " + contactId
		+ ", countryCode: " + countryCode + ", operatorCode: "
		+ operatorCode + ", type: " + type + ", value: " + value
		+ ", comment: " + comment + "]";
    }
}
