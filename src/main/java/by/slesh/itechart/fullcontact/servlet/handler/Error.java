package by.slesh.itechart.fullcontact.servlet.handler;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class Error {
    private String header;
    private Integer statusCode;
    private String requestUri;
    private String servletName;
    private String className;
    private String message;

    public Error() {
    }

    public Error(String header, Integer statusCode, String requestUri, String servletName, String className,
	    String message) {
	super();
	this.header = header;
	this.statusCode = statusCode;
	this.requestUri = requestUri;
	this.servletName = servletName;
	this.className = className;
	this.message = message;
    }

    public String getHeader() {
	return header;
    }

    public void setHeader(String header) {
	this.header = header;
    }

    public Integer getStatusCode() {
	return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
	this.statusCode = statusCode;
    }

    public String getRequestUri() {
	return requestUri;
    }

    public void setRequestUri(String requestUri) {
	this.requestUri = requestUri;
    }

    public String getServletName() {
	return servletName;
    }

    public void setServletName(String servletName) {
	this.servletName = servletName;
    }

    public String getClassName() {
	return className;
    }

    public void setClassName(String className) {
	this.className = className;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }
}
