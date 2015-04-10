/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.page.servlet;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.configuration.StorageConfiguration;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.servlet.GenericHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.GregorianCalendar;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@WebServlet(urlPatterns = "/page/upload", name = "pageUploadServlet")
@MultipartConfig(maxFileSize = 24657920, maxRequestSize = 24657920, fileSizeThreshold = 1024)
public class FileUploadServlet extends GenericHttpServlet {

	private static final Logger LOG = LoggerFactory.getLogger(FileUploadServlet.class);

	@Autowired
	private ResourceService resourceService;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String path = request.getParameter("path");

		String ckEditorFuncNum = request.getParameter("CKEditorFuncNum");

		// Create path components to save the file
		final Part filePart = request.getPart("upload");
		final String fileName = getFileName(filePart);
		if (fileName == null) {
			return;
		}
		PrintWriter writer = response.getWriter();
		try (InputStream fileContent = filePart.getInputStream()) {
			Content content = new Content(path + "/" + fileName);
			resourceService.saveContent(content, "", fileContent, 1);

			String filePath = "";
			StorageConfiguration storageConfiguration = StorageManager
					.getConfiguration();
			if (StorageManager.isFileStorage()) {
				filePath = SiteConfiguration.getAppUrl() + "file/"
						+ content.getPath();
			} else if (StorageManager.isS3Storage()) {
				filePath = storageConfiguration.getResourcePath(content
						.getPath());
			}

			String responseHtml = "<html><body><script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction('%s','%s','%s');</script></body></html>";
			responseHtml = String.format(responseHtml, ckEditorFuncNum,
					filePath, "");
			writer.write(responseHtml);
		}catch (FileNotFoundException fne) {
			writer.println("You either did not specify a file to upload or are "
					+ "trying to upload a file to a protected or nonexistent "
					+ "location.");
			writer.println("<br/> ERROR: " + fne.getMessage());

			LOG.error("Problems during file upload. Error: {0}",
					new Object[] { fne.getMessage() });
		}
	}

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				String fileName = content.substring(content.indexOf('=') + 1)
						.trim().replace("\"", "");
				int index;
				if ((index = fileName.lastIndexOf(".")) != -1) {
					fileName = fileName.substring(0, index - 1)
							+ (new GregorianCalendar().getTimeInMillis())
							+ fileName.substring(index);
					return fileName;
				}
			}
		}
		return null;
	}

}
