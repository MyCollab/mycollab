package com.esofthead.mycollab.module.file.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import com.esofthead.mycollab.module.ecm.MimeTypesUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
@Component("assetsHandlerServlet")
public class AnnotatedAssetServlet implements HttpRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(AnnotatedAssetServlet.class);

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		String resourcePath = "assets" + path;

		InputStream inputStream = AnnotatedAssetServlet.class.getClassLoader()
				.getResourceAsStream(resourcePath);

		if (inputStream == null) {
			resourcePath = "VAADIN/themes/mycollab" + path;
			inputStream = AnnotatedAssetServlet.class.getClassLoader()
					.getResourceAsStream(resourcePath);
		}

		if (inputStream != null) {
			response.setHeader("Content-Type",
					MimeTypesUtil.detectMimeType(inputStream));
			response.setHeader("Content-Length",
					String.valueOf(inputStream.available()));
			// response.setHeader("Content-Disposition", "inline; filename=\""
			// + avatarFile.getName() + "\"");

			BufferedInputStream input = null;
			BufferedOutputStream output = null;

			try {
				input = new BufferedInputStream(inputStream);
				output = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[8192];
				int length = 0;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}
			} finally {
				if (output != null)
					try {
						output.close();
					} catch (IOException logOrIgnore) {
					}
				if (input != null)
					try {
						input.close();
					} catch (IOException logOrIgnore) {
					}
			}
		} else {
			log.error("Can not find resource has path {}", path);
		}

	}

}
