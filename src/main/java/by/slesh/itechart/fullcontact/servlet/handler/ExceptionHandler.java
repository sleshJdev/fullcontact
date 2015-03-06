package by.slesh.itechart.fullcontact.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eugene Putsykovich(slesh) Feb 14, 2015
 *		
 *         Exceptino handling
 */
@WebServlet("/ExceptionHandlerServlet")
public class ExceptionHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	processError(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException,
	    ServletException {
	// Set response content type
	response.setContentType("text/html");

	// Analyze the servlet exception
	Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
	String message = throwable == null ? "" : throwable.getMessage();
	String className = throwable == null ? "" : throwable.getClass().getName();
	Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
	String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
	String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
	if (servletName == null) {
	    servletName = "Unknown";
	}
	if (requestUri == null) {
	    requestUri = "Unknown";
	}

	String header = statusCode == 500 ? "ERROR DETAILS" : "ERROR DETAILS";
	Error error = new Error(header, statusCode, requestUri, servletName, className, message);

	request.setAttribute("content", "error.jsp");
	request.setAttribute("error", error);
	request.setAttribute("title", "Error Page");
	request.getRequestDispatcher("/WEB-INF/views/template.jsp").forward(request, response);
    }
}
