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
import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.domain.FamilyStatusEntity;
import by.slesh.itechart.fullcontact.domain.NationalityEntity;
import by.slesh.itechart.fullcontact.domain.SexEntity;
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

	getRequest().setAttribute("status", "Success statuc from " + getClass().getSimpleName() + "!");

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
	}

	if (StringUtils.isEmptyOrWhitespaceOnly(action)) {// open page add
	    LOGGER.info("go add page");
	    try {
		List<NationalityEntity> list1 = DaoFactory.getNationalityDao(true, true).getAll();
		List<FamilyStatusEntity> list2 = DaoFactory.getFamilyStatusDao(true, true).getAll();
		List<SexEntity> list3 = DaoFactory.getSexDao(true, true).getAll();
		getRequest().setAttribute("nationalitiesList", list1);
		getRequest().setAttribute("familyStatusesList", list2);
		getRequest().setAttribute("sexesList", list3);
	    } catch (ClassNotFoundException | SQLException e) {
		e.printStackTrace();
		throw new ServletException(e);
	    }
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

	LOGGER.info("x : {}", action);
    }
}
