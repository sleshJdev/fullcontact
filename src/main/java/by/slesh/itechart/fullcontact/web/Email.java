package by.slesh.itechart.fullcontact.web;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class Email {
    private Message message;
    private Multipart multipart;

    public Email(Message message) {
	this.message = message;
	this.multipart = new MimeMultipart();
    }

    public Message getMessage() throws MessagingException {
	message.setContent(multipart);
	return message;
    }

    public void setTo(String[] to) {
	try {
	    Address[] address = new Address[to.length];
	    for (int i = 0; i < to.length; i++) {
		address[i] = new InternetAddress(to[i].trim().toLowerCase());
	    }
	    message.setRecipients(Message.RecipientType.TO, address);
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
    }

    public void setSubject(String subject) {
	try {
	    message.setSubject(subject);
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
    }

    public void setBody(String body) {
	try {
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setText(body);
	    multipart.addBodyPart(messageBodyPart);
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
    }

    public void addAttachment(File file) {
	try {
	    DataSource source = new FileDataSource(file);
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(file.getName());
	    multipart.addBodyPart(messageBodyPart);
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
    }
}
