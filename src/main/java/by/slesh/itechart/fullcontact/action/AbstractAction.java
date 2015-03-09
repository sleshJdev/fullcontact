package by.slesh.itechart.fullcontact.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.slesh.itechart.fullcontact.db.local.Database;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public abstract class AbstractAction implements Action {
    public static final String TEMPLATE_PATH = "/WEB-INF/views/template.jsp";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	setRequest(request);
	setResponse(response);
	dispatcher = request.getRequestDispatcher(TEMPLATE_PATH);
	request.setAttribute("quantity", Database.getBirthdayMans().size());
    }

    public RequestDispatcher getDispatcher() {
	return dispatcher;
    }

    public HttpServletRequest getRequest() {
	return request;
    }

    public void setRequest(HttpServletRequest request) {
	this.request = request;
    }

    public HttpServletResponse getResponse() {
	return response;
    }

    public void setResponse(HttpServletResponse response) {
	this.response = response;
    }

}
