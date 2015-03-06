package by.slesh.itechart.fullcontact.domain;

import java.sql.Date;

public class EmailEntity extends Entity {
    private long id;
    private long contactIdSender;
    private String subject;
    private String text;
    private Date sendDate;

    public EmailEntity() {
    }

    public EmailEntity(long id, long contactIdSender, String subject, String text, Date sendDate) {
	super(id, text);
	this.contactIdSender = contactIdSender;
	this.subject = subject;
	this.sendDate = sendDate;
    }

    public long getContactIdSender() {
	return contactIdSender;
    }

    public void setContactIdSender(long contactIdSender) {
	this.contactIdSender = contactIdSender;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getText() {
	return getValue();
    }

    public void setText(String text) {
	setValue(text);
    }

    public Date getSendDate() {
	return sendDate;
    }

    public void setSendDate(Date sendDate) {
	this.sendDate = sendDate;
    }

    public String toString() {
	return "email:: [id: " + id + ", contactIdSender: " + contactIdSender + ", subject: " + subject + "text: "
		+ text + "sendData: " + sendDate + "]";
    }
}
