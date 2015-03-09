package by.slesh.itechart.fullcontact.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.db.local.Database;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.HttpProcessUtil;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public class AddAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(EditAction.class);

    private String action;

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	getRequest().setAttribute("status", "You Can Add A New Contact");

	if (!StringUtils.isEmptyOrWhitespaceOnly(action) && action.equals("add")) {// add
	    try {
		EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
		((ContactDao) contactDao).add(HttpProcessUtil.readContactFrom(getRequest()));
	    } catch (ClassNotFoundException | SQLException | ParseException e) {
		e.printStackTrace();
		throw new ServletException(e);
	    }
	    
	    LOGGER.info("redirect to show page");
	    
	    getResponse().sendRedirect("show");
	} else {// open page add
	    LOGGER.info("go add page");
	    
	    getRequest().setAttribute("nationalitiesList", Database.getNationalities());
	    getRequest().setAttribute("familyStatusesList", Database.getFamilyStatuses());
	    getRequest().setAttribute("sexesList", Database.getSexes());
	    getRequest().setAttribute("content", "add-contact-form-content.jsp");
	    getRequest().setAttribute("title", "Add Contact Page");
	    getDispatcher().forward(getRequest(), getResponse());
	}

	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	super.init(request, response);
	action = request.getParameter("x");// action kind

	LOGGER.info("action(x) : {}", action);
    }
}
