package by.slesh.itechart.fullcontact.util;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eugene Putsykovich(slesh) Feb 15, 2015
 *
 */
public final class PathUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(PathUtil.class);

    public static InputStream getResourceFile(String fileName) {
	ClassLoader classLoader = PathUtil.class.getClassLoader();
	return classLoader.getResourceAsStream(fileName);
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
