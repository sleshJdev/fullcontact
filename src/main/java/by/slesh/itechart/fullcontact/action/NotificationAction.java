package by.slesh.itechart.fullcontact.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.slesh.itechart.fullcontact.db.local.Database;

/**
 * @author Eugene Putsykovich(slesh) Mar 8, 2015
 *
 */
public class NotificationAction extends AbstractAction {
    @Override
    public void execute() throws ServletException, IOException {
	getRequest().setAttribute("status", String.format("Your Notifications. Today %s", Database.getBirthdayMans().size()));
	getRequest().setAttribute("contactsBirthdayMans", Database.getBirthdayMans());
	getRequest().setAttribute("content", "notification-content.jsp");
	getRequest().setAttribute("title", "Notifications Page");
	getDispatcher().forward(getRequest(), getResponse());
    }
    
    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	super.init(request, response);
    }
}
