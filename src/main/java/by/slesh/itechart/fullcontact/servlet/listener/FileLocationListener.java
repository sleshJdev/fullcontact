package by.slesh.itechart.fullcontact.servlet.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.slesh.itechart.fullcontact.util.PathUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 4, 2015
 *
 */
public class FileLocationListener implements ServletContextListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileLocationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
	LOGGER.info("BEGIN");

	ServletContext context = servletContextEvent.getServletContext();

	String webInfPath = PathUtil.goToWebInf().getPath();
	String webInfDirectory = context.getInitParameter("upload-directory");
	File webInfFile = new File(String.format("%s%s%s", webInfPath, File.separator, webInfDirectory));
	if (!webInfFile.exists()) {
	    webInfFile.mkdirs();
	}

	String publicPath = PathUtil.goToRoot().getPath();
	String publicDirectory = context.getInitParameter("public-directory");
	File publicFile = new File(String.format("%s%s%s", publicPath, File.separator, publicDirectory));
	if (!publicFile.exists()) {
	    publicFile.mkdirs();
	}

	context.setAttribute("upload-directory-file", webInfFile);
	context.setAttribute("upload-directory-path", webInfFile.getPath());

	context.setAttribute("public-directory-file", publicFile);
	context.setAttribute("public-directory-path", publicFile.getPath());

	LOGGER.info("upload directory path {}", webInfFile.getPath());
	LOGGER.info("public directory path {}", publicFile.getPath());
	LOGGER.info("servlet context initilize successful!");
	LOGGER.info("END");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
	LOGGER.info("OK");
    }
}