package by.slesh.itechart.fullcontact.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

/**
 * @author Eugene Putsykovich(slesh) Mar 7, 2015
 *
 */
public class LoadAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoadAction.class);

    private String fileName;
    private String attachmentsDirectory;
    private String filesDirectory;

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	if (!StringUtils.isEmptyOrWhitespaceOnly(fileName)) {
	    InputStream in = null;
	    ServletOutputStream out = null;
	    File file = null;
	    try {
		file = new File(String.format("%s%s%s", attachmentsDirectory, File.separator, fileName));
		if (!file.exists()) {
		    file = new File(String.format("%s%s%s", filesDirectory, File.separator, fileName));
		    if (!file.exists()) {
			getResponse().sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
			return;
		    }
		}
		in = new FileInputStream(file.getPath());
		String mimeType = getRequest().getServletContext().getMimeType(file.getPath());
		int length = (int) file.length();
		getResponse().setContentType(mimeType == null ? "application/octet-stream" : mimeType);
		getResponse().setContentLength(length);
		getResponse().setHeader("Content-Disposition", String.format("atachment; filename=\"%s\"", file));
		out = getResponse().getOutputStream();
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = in.read(buffer)) != -1) {
		    out.write(buffer, 0, read);
		}
	    } catch (Exception e) {
		throw new ServletException(String.format(
			"error dowload file %s. reason: maybe file not found on server", file));
	    } finally {
		if (in != null) {
		    in.close();
		    in = null;
		}
		if (out != null) {
		    out.flush();
		    out.close();
		    out = null;
		}
	    }

	    LOGGER.info("file {} dowload at client successfull!", file);

	} else {
	    throw new ServletException(String.format("%s - bad name format", fileName));
	}

	LOGGER.info("END");
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response) {
	LOGGER.info("BEGIN");

	super.init(request, response);
	fileName = getRequest().getParameter("name");
	attachmentsDirectory = getRequest().getServletContext().getAttribute("attachments-directory-path").toString();
	filesDirectory = getRequest().getServletContext().getAttribute("files-directory-path").toString();

	LOGGER.info("file name to load: {}", fileName);
	LOGGER.info("END");
    }
}
