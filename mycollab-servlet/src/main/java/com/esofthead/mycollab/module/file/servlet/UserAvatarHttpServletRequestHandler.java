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
package com.esofthead.mycollab.module.file.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.configuration.FileStorageConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.file.service.ContentService;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("userAvatarFSServlet")
public class UserAvatarHttpServletRequestHandler extends GenericServletRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(UserAvatarHttpServletRequestHandler.class);

	@Autowired
	private ContentService contentService;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (!SiteConfiguration.isSupportFileStorage()) {
			throw new MyCollabException(
					"This servlet support file system setting only");
		}

		String path = request.getPathInfo();
		File avatarFile = null;

		String username = "";
		int size = 0;

		if (path != null) {
			String[] params = path.split("/");
			if (params.length >= 3) {
				username = params[1];
				size = Integer.parseInt(params[2]);

				if (size <= 0) {
					log.error("Error to get avatar", new MyCollabException(
							"Invalid request for avatar " + path));
				}
			} else {
				log.error("Error to get avatar", new MyCollabException(
						"Invalid request for avatar " + path));
			}
		}

		FileStorageConfiguration fileConfiguration = (FileStorageConfiguration) SiteConfiguration
				.getStorageConfiguration();
		avatarFile = fileConfiguration.getAvatarFile(username, size);
		InputStream avatarInputStream = null;
		if (avatarFile != null) {
			avatarInputStream = new FileInputStream(avatarFile);
		} else {
			String userAvatarPath = "assets/images/default_user_avatar_" + size
					+ ".png";
			avatarInputStream = UserAvatarHttpServletRequestHandler.class
					.getClassLoader().getResourceAsStream(userAvatarPath);
		}

		response.setHeader("Content-Type", "image/png");
		response.setHeader("Content-Length",
				String.valueOf(avatarInputStream.available()));

		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			input = new BufferedInputStream(avatarInputStream);
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
	}
}
