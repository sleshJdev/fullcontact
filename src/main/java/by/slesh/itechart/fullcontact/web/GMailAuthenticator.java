package by.slesh.itechart.fullcontact.web;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class GMailAuthenticator extends Authenticator {
    private String userName;
    private String password;

    public GMailAuthenticator(String userName, String password) {
	this.userName = userName;
	this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
	return new PasswordAuthentication(userName, password);
    }
}
