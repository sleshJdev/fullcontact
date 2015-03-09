package by.slesh.itechart.fullcontact.domain;

import java.sql.Date;
import java.util.List;

public class EmailEntity extends Entity {
    private long contactIdSender;
    private ContactEntity sender;
    private List<AtachmentEntity> atachments;
    private List<ContactEntity> receivers;
    private String subject;
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

    public ContactEntity getSender() {
	return sender;
    }

    public void setSender(ContactEntity sender) {
	this.sender = sender;
    }

    public List<AtachmentEntity> getAtachments() {
	return atachments;
    }

    public void setAtachments(List<AtachmentEntity> atachments) {
	this.atachments = atachments;
    }

    public List<ContactEntity> getReceivers() {
	return receivers;
    }

    public void setReceivers(List<ContactEntity> receivers) {
	this.receivers = receivers;
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
	return "email:: [id: " + getId() + ", contactIdSender: " + contactIdSender + ", subject: " + subject
		+ ", text: " + getValue() + ", sendData: " + sendDate + "]";
    }
}
