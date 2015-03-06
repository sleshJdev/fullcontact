package by.slesh.itechart.fullcontact.domain;

import java.sql.Date;

public class AtachmentEntity extends Entity {
    private long contactId;
    private String path;
    private Date uploadDate;
    private String comment;

    public AtachmentEntity() {
    }

    public AtachmentEntity(long id, long contactId, String name, String path,
	    Date uploadDate, String comment) {
	super(id, name);//value == name
	this.contactId = contactId;
	this.path = path;
	this.uploadDate = uploadDate;
	this.comment = comment;
    }

    public long getContactId() {
	return contactId;
    }

    public void setContactId(long contactId) {
	this.contactId = contactId;
    }

    public String getName() {
	return getValue();
    }

    public void setName(String name) {
	setValue(name);
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public Date getUploadDate() {
	return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
	this.uploadDate = uploadDate;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    @Override
    public String toString() {
	return "atachment:: [id: " + id + ",ontactId: " + contactId
		+ ", name: " + super.value + ", path: " + path + ", uploadDate: "
		+ uploadDate + ", comment:" + comment + "]";
    }
}
