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
import javax.servlet.ServletException;

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

    public void setTo(String[] to) throws ServletException {
	try {
	    Address[] address = new Address[to.length];
	    for (int i = 0; i < to.length; i++) {
		address[i] = new InternetAddress(to[i].trim().toLowerCase());
	    }
	    message.setRecipients(Message.RecipientType.TO, address);
	} catch (MessagingException e) {
	    throw new ServletException(e);
	}
    }

    public void setSubject(String subject) throws ServletException {
	try {
	    message.setSubject(subject);
	} catch (MessagingException e) {
	    throw new ServletException(e);
	}
    }

    public void setBody(String body) throws ServletException {
	try {
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setText(body);
	    multipart.addBodyPart(messageBodyPart);
	} catch (MessagingException e) {
	    throw new ServletException(e);
	}
    }

    public void addAttachment(File file) throws ServletException {
	try {
	    DataSource source = new FileDataSource(file);
	    BodyPart messageBodyPart = new MimeBodyPart();
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(file.getName());
	    multipart.addBodyPart(messageBodyPart);
	} catch (MessagingException e) {
	    throw new ServletException(e);
	}
    }
}
