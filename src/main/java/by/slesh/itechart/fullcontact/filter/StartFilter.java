package by.slesh.itechart.fullcontact.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class StartFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	HttpServletResponse httpRepsonse = (HttpServletResponse) response;
	httpRepsonse.sendRedirect("action/show");
    }

    public void destroy() {
    }
}
