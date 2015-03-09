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

import by.slesh.itechart.fullcontact.dao.ContactDao;
import by.slesh.itechart.fullcontact.dao.impl.ContactDaoImp;
import by.slesh.itechart.fullcontact.db.local.Database;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.HttpProcessUtil;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 7, 2015
 *
 */
public class SearchAction extends AbstractAction {
    private static final String PROBLEM = "Any problems during the execution of the search";
    private static final String SUCCESS = "Search result: %d records.";
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchAction.class);
    private static final int LIMIT_ROWS_PER_PAGE = 10;
    private int begin;
    private int pages;
    private String page = null;
    private String action = null;
    private static List<ContactEntity> contacts = null;

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");
	try {
	    if (StringUtils.isEmptyOrWhitespaceOnly(action)) {
		LOGGER.info("go search page");
		getRequest().setAttribute("nationalitiesList", Database.getNationalities());
		getRequest().setAttribute("familyStatusesList", Database.getFamilyStatuses());
		getRequest().setAttribute("sexesList", Database.getSexes());
		getRequest().setAttribute("content", "search-contact-form-content.jsp");
		getRequest().setAttribute("title", "Search Contact Page");
	    } else {
		switch (action) {
		case "search":
		    contacts = search();
		    page = "1";
		    initPage();
		case "show":
		    initPage();
		    break;
		}
		getRequest().setAttribute("status", String.format(SUCCESS, contacts.size()));
		getRequest().setAttribute("content", "show-contacts-content.jsp");
		getRequest().setAttribute("title", "Search Result Page");
	    }
	    getDispatcher().forward(getRequest(), getResponse());
	} catch (ClassNotFoundException | SQLException | ParseException e) {
	    getRequest().setAttribute("status", PROBLEM);
	    throw new ServletException(PROBLEM);
	}
	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	LOGGER.info("BEGIN");

	super.init(request, response);
	action = request.getParameter("x");
	page = request.getParameter("page");
	begin = 0;

	LOGGER.info("action(x): " + action);
	LOGGER.info("page: " + page);
	LOGGER.info("END");
    }

    private void initPage() {
	int number = Integer.parseInt(page) - 1;
	List<ContactEntity> pageList = getPage(contacts, number);
	getRequest().setAttribute("isSearchResult", true);
	getRequest().setAttribute("begin", begin);
	getRequest().setAttribute("pages", pages);
	getRequest().setAttribute("contacts", pageList);
    }

    private List<ContactEntity> getPage(List<ContactEntity> contacts, int pageNumber) {
	int quantityContacts = contacts.size();
	int quantityPages = (int) (quantityContacts / LIMIT_ROWS_PER_PAGE);
	int quantityRestPages = (int) (quantityContacts % LIMIT_ROWS_PER_PAGE);
	if (quantityRestPages > 0) {
	    ++quantityPages;
	}
	begin = LIMIT_ROWS_PER_PAGE * pageNumber;
	pages = quantityPages;
	int toIndex = begin + LIMIT_ROWS_PER_PAGE;
	toIndex = toIndex >= quantityContacts ? quantityContacts : toIndex;

	return contacts.subList(begin, toIndex);
    }

    private List<ContactEntity> search() throws ParseException, ClassNotFoundException, IOException, SQLException {
	ContactEntity contact = HttpProcessUtil.readContactFrom(getRequest());
	ContactDao contactDao = new ContactDaoImp();
	String searchType = getRequest().getParameter("age-search-type");
	boolean isLess = searchType.equals("less") ? true : false;

	return contactDao.search(contact, isLess);
    }
}
