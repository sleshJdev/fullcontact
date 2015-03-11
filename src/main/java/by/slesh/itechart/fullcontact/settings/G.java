package by.slesh.itechart.fullcontact.settings;

import java.io.File;

import by.slesh.itechart.fullcontact.util.PathUtil;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 * 
 *         Global settings
 */
public final class G {
    public static long MY_ID = 1;
    public static final int LIMIT_ROWS_PER_PAGE = 10;
    public static String US_NAME;
    public static String US_EMAIL;
    public static String US_PHONE;
    public static final File CACHE = new File(String.format("%s%s%s", PathUtil.goToRoot(), File.separator, ".cache"));
    static {
	if (!CACHE.exists()) {
	    CACHE.mkdirs();
	}
    }
    public static String AVATARS_DIRECTORY;
    public static String ATTACHMENTS_DIRECTORY;
    public static String FILES_DIRECTORY;
}
