package by.slesh.itechart.fullcontact.action;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.impl.DaoFactory;
import by.slesh.itechart.fullcontact.domain.ContactEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public class DeleteAction extends AbstractAction {
    /**
     * Id of contacts for remove
     */
    private String[] ids;

    @Override
    public void execute() throws ServletException, IOException {
	if (ids != null) {
	    for (String item : ids) {
		long id = Long.parseLong(item);
		try {
		    EntityDao<ContactEntity> contactDao = DaoFactory.getContactDao(true, true);
		    contactDao.delete(id);
		} catch (ClassNotFoundException | SQLException e) {
		    e.printStackTrace();
		    throw new ServletException(e);
		}
	    }
	}
	getRequest().setAttribute("status", "Success statuc from " + getClass().getSimpleName() + "!");
	getResponse().sendRedirect("show");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	super.init(request, response);
	ids = request.getParameterValues("id");
    }
}
