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
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.FamilyStatusEntity;
import by.slesh.itechart.fullcontact.domain.NationalityEntity;
import by.slesh.itechart.fullcontact.domain.SexEntity;
import by.slesh.itechart.fullcontact.util.HttpProcessUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 7, 2015
 *
 */
public class SearchAction extends AbstractAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchAction.class);
    private static final String SUCCESS = "Success result: %d records.";
    @SuppressWarnings("unused")
    private static final String PROBLEM = "Any problems during the execution of the search";

    private String action;

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	if (!StringUtils.isEmptyOrWhitespaceOnly(action) && action.equals("search")) {
	    try {
		ContactEntity contact = HttpProcessUtil.readContactFrom(getRequest());

		LOGGER.info("obtain contact for search: {}");
		LOGGER.info(contact.toString());

		ContactDao contactDao = new ContactDaoImp();
		String searchType = getRequest().getParameter("age-search-type");
		boolean isLess = searchType.equals("less") ? true : false;

		List<ContactEntity> contacts = contactDao.search(contact, isLess);
		getRequest().setAttribute("status", String.format(SUCCESS, contacts.size()));
		getRequest().setAttribute("contacts", contacts);
		getRequest().setAttribute("content", "show-contacts-content.jsp");
		getRequest().setAttribute("title", "Full Contact Page");
		getDispatcher().forward(getRequest(), getResponse());
	    } catch (ClassNotFoundException | SQLException | ParseException e) {
		throw new ServletException(e);
	    }
	}
	if (StringUtils.isEmptyOrWhitespaceOnly(action)) {
	    LOGGER.info("go search page");

	    try {
		List<NationalityEntity> list1 = DaoFactory.getNationalityDao(true, true).getAll();
		List<FamilyStatusEntity> list2 = DaoFactory.getFamilyStatusDao(true, true).getAll();
		List<SexEntity> list3 = DaoFactory.getSexDao(true, true).getAll();
		getRequest().setAttribute("nationalitiesList", list1);
		getRequest().setAttribute("familyStatusesList", list2);
		getRequest().setAttribute("sexesList", list3);
	    } catch (ClassNotFoundException | SQLException e) {
		throw new ServletException(e);
	    }
	    getRequest().setAttribute("content", "search-contact-form-content.jsp");
	    getRequest().setAttribute("title", "Search Contact Page");
	    getDispatcher().forward(getRequest(), getResponse());
	}

	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	LOGGER.info("BEGIN");

	super.init(request, response);
	action = request.getParameter("x");

	LOGGER.info("x: " + action);
	LOGGER.info("END");
    }
}
