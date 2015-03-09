package by.slesh.itechart.fullcontact.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 8, 2015
 *
 */
public class ShowAction extends AbstractAction {
    private static final int LIMIT_ROWS_PER_PAGE = 10;
    private int page = 1;

    @Override
    public void execute() throws ServletException, IOException {
	List<ContactEntity> contacts = null;
	try {
	    EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
	    contacts = contactDao.getLimit(LIMIT_ROWS_PER_PAGE * page, LIMIT_ROWS_PER_PAGE);
	    long quantityContacts = contactDao.count();
	    int quantityPages = (int) (quantityContacts / LIMIT_ROWS_PER_PAGE);
	    int quantityRestPages = (int) (quantityContacts % LIMIT_ROWS_PER_PAGE);
	    if (quantityRestPages > 0) {
		++quantityPages;
	    }
	    getRequest().setAttribute("status", String.format("Your Contacts. Total %d", quantityContacts));
	    getRequest().setAttribute("begin", LIMIT_ROWS_PER_PAGE * page);
	    getRequest().setAttribute("pages", quantityPages);
	    getRequest().setAttribute("contacts", contacts);
	    getRequest().setAttribute("content", "show-contacts-content.jsp");
	    getRequest().setAttribute("title", "Full Contact Page");
	    getDispatcher().forward(getRequest(), getResponse());
	} catch (ClassNotFoundException | IOException | SQLException e) {
	    e.printStackTrace();
	    throw new ServletException(e);
	}
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	super.init(request, response);
	page = 1;
	String pageParameter = request.getParameter("page");
	if (pageParameter != null) {
	    page = Integer.parseInt(pageParameter);
	}
	--page;
    }
}
