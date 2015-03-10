package by.slesh.itechart.fullcontact.servlet.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.action.Action;
import by.slesh.itechart.fullcontact.action.ActionFactory;
import by.slesh.itechart.fullcontact.util.PathUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(PathUtil.class);

    @Override
    public void init() throws ServletException {
	super.init();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	LOGGER.info("BEGIN");

	try {
	    response.setContentType("text/html;charset=UTF-8");
	    request.setCharacterEncoding("utf-8");

	    StringBuffer url = request.getRequestURL();
	    String actionName = url.substring(url.lastIndexOf("/") + 1, url.length());

	    LOGGER.info("request url: " + url);
	    LOGGER.info("action : " + actionName);

	    Action action = ActionFactory.getActionByName(actionName);
	    action.init(request, response);
	    action.execute();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new ServletException(e);
	}

	LOGGER.info("END\n\n\n\n\n\n\n");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	processRequest(request, response);
    }
}
