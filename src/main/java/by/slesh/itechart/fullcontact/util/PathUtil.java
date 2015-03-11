package by.slesh.itechart.fullcontact.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eugene Putsykovich(slesh) Feb 15, 2015
 *
 */
public final class PathUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(PathUtil.class);

    public static File getResourceFile(String fileName) {
	ClassLoader classLoader = PathUtil.class.getClassLoader();

	File file = null;
	InputStream in = null;
	OutputStream out = null;
	try {
	    file = File.createTempFile("fullcontact.peroperties", "temp");
	    in = classLoader.getResourceAsStream(fileName);
	    out = new FileOutputStream(file);
	    int bytes = 0;
	    while ((bytes = in.read()) != -1) {
		out.write(bytes);
	    }
	} catch (IOException e) {
	    LOGGER.info("read properties file error: {}", e);
	} finally {
	    if (in != null) {
		try {
		    in.close();
		    in = null;
		} catch (IOException e) {
		    LOGGER.error("close input steream error: {}", e);
		}
	    }
	    if (out != null) {
		try {
		    out.close();
		    out = null;
		} catch (IOException e) {
		    LOGGER.error("close output stream erro: {}", e);
		}
	    }
	}
	
	LOGGER.info("read file properties: {}", file);

	return file;
    }

    /**
     * @return Path to ''classes' directory. E.g. we get path such as
     *         .../'application-name'/WEB-INF/classes/
     */
    public static final String goToClasses() {
	return PathUtil.class.getClassLoader().getResource("").getPath();
    }

    /**
     * @return Path to root folder of application. E.g. we get path such as
     *         .../'application-name'
     */
    public static File goToRoot() {
	String path = goToClasses();
	File file = new File(path).getParentFile().getParentFile();

	LOGGER.info("root: {}", file);
	return file;
    }

    /**
     * @return Path to WEB-INF folder of application. E.g. we get path such as
     *         .../'application-name'/WEB-INF
     */
    public static File goToWebInf() {
	String path = goToClasses();
	File file = new File(path).getParentFile();

	LOGGER.info("webInf: {}", file);
	return file;
    }
}
