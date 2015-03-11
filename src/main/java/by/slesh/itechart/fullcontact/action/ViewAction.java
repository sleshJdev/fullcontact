package by.slesh.itechart.fullcontact.action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

public class ViewAction extends AbstractAction {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoadAction.class);

    private String fileName;
    private String attachmentsDirectory;

    @Override
    public void execute() throws ServletException, IOException {
	LOGGER.info("BEGIN");

	if (!StringUtils.isEmptyOrWhitespaceOnly(fileName)) {
	    if (fileName == null) {
		return;
	    }

	    File file = new File(String.format("%s%s%s", attachmentsDirectory, File.separator, fileName));

	    // Get content type by filename.
	    String contentType = getRequest().getServletContext().getMimeType(file.getName());

//	    // Check if file is actually an image (avoid download of other files
//	    // by hackers!).
//	    // For all content types, see:
//	    // http://www.w3schools.com/media/media_mimeref.asp
//	    if (contentType == null || !contentType.startsWith("image")) {
//		// Do your thing if the file appears not being a real image.
//		// Throw an exception, or send 404, or show default/warning
//		// image, or just ignore it.
//		getResponse().sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
//		return;
//	    }

	    getResponse().setContentType(contentType);
	    getResponse().setHeader("Content-Length", String.valueOf(file.getName()));
	    Files.copy(file.toPath(), getResponse().getOutputStream());
	    
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

	LOGGER.info("file name to load: {}", fileName);
	LOGGER.info("END");
    }
}
