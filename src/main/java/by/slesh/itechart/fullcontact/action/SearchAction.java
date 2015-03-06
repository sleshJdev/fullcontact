package by.slesh.itechart.fullcontact.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.impl.ContactDaoImp;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.HttpProcessUtil;

public class SearchAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(SearchAction.class);

    private String action;

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	if (!StringUtils.isEmptyOrWhitespaceOnly(action) && action.equals("search")) {
	    try {
		ContactEntity contact = HttpProcessUtil.readContactFrom(getRequest());
		LOGGER.info("obtain contact for search -->>))");
		LOGGER.info(contact.toString());
		ContactDao contactDao = new ContactDaoImp();
		List<ContactEntity> contacts = contactDao.search(contact);
		getRequest().setAttribute("status", "Success statuc from " + getClass().getSimpleName() + "!");
		getRequest().setAttribute("contacts", contacts);
		getRequest().setAttribute("content", "show-contacts-content.jsp");
		getRequest().setAttribute("title", "Full Contact Page");
		getDispatcher().forward(getRequest(), getResponse());
	    } catch (ClassNotFoundException | SQLException | ParseException e) {
		e.printStackTrace();
		throw new ServletException(e);
	    }
	}
	if (StringUtils.isEmptyOrWhitespaceOnly(action)) {
	    getRequest().setAttribute("status", "Success statuc from " + getClass().getSimpleName() + "!");
	    getRequest().setAttribute("content", "search-contact-form-content.jsp");
	    getRequest().setAttribute("title", "Search Contact Page");
	    getDispatcher().forward(getRequest(), getResponse());
	}

	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	super.init(request, response);
	action = request.getParameter("x");
	LOGGER.info("x: " + action);
    }
}
