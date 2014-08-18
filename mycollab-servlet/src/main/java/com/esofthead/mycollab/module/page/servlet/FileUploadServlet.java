package com.esofthead.mycollab.module.page.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.configuration.StorageConfiguration;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@Component("pageUploadServlet")
public class FileUploadServlet extends GenericServletRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(FileUploadServlet.class);

	@Autowired
	private ResourceService resourceService;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String path = request.getParameter("path");

		String ckEditorFuncNum = request.getParameter("CKEditorFuncNum");
		// http://st.f1.vnecdn.net/responsive/i/v9/graphics/img_logo_vne_web.gif

		// Create path components to save the file
		final Part filePart = request.getPart("upload");
		final String fileName = getFileName(filePart);
		InputStream filecontent = null;
		final PrintWriter writer = response.getWriter();

		try {
			filecontent = filePart.getInputStream();

			Content content = new Content();
			content.setPath(path + "/" + fileName);
			resourceService.saveContent(content, "", filecontent, 1);

			String filePath = "";
			StorageConfiguration storageConfiguration = SiteConfiguration
					.getStorageConfiguration();
			if (SiteConfiguration.isSupportFileStorage()) {
				filePath = SiteConfiguration.getAppUrl() + "file/"
						+ content.getPath();
			} else if (SiteConfiguration.isSupportS3Storage()) {
				filePath = storageConfiguration.getResourcePath(content
						.getPath());
			}

			String responseHtml = "<html><body><script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction('%s','%s','%s');</script></body></html>";
			responseHtml = String.format(responseHtml, ckEditorFuncNum,
					filePath, "");
			writer.write(responseHtml);
		} catch (FileNotFoundException fne) {
			writer.println("You either did not specify a file to upload or are "
					+ "trying to upload a file to a protected or nonexistent "
					+ "location.");
			writer.println("<br/> ERROR: " + fne.getMessage());

			log.error("Problems during file upload. Error: {0}",
					new Object[] { fne.getMessage() });
		} finally {
			if (filecontent != null) {
				filecontent.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}

}
