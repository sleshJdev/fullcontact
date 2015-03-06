package by.slesh.itechart.fullcontact.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public interface Action {
    public void execute() throws ServletException, IOException;

    public void init(HttpServletRequest request, HttpServletResponse response);
}
