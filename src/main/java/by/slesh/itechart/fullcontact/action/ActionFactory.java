package by.slesh.itechart.fullcontact.action;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public class ActionFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(ActionFactory.class);
    private static final String PACKAGE = "by.slesh.itechart.fullcontact.action.";
    private static final String SUFFIX = "Action";

    public static final Action getControllerByClass(Class<?> actionClass) throws ServletException {
	try {
	    return (Action) actionClass.newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
	    e.printStackTrace();
	    throw new ServletException(e);
	}
    }

    public static final Action getActionByName(String className) throws ServletException {
	try {
	    String[] tokens = className.split("-");
	    className = "";
	    for (String token : tokens) {
		className += makeUppercaseFirstChar(token.trim());
	    }
	    String name = String.format("%s%s%s", PACKAGE, className, SUFFIX);
	    LOGGER.info("full controller name = {}", name);

	    return getControllerByClass(Class.forName(name));
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    throw new ServletException(e);
	}
    }

    private static String makeUppercaseFirstChar(String word) {
	word = word.toLowerCase();
	String firstChar = Character.toString(word.charAt(0)).toUpperCase();
	word = firstChar.concat(word.substring(1));

	return word;
    }
}
