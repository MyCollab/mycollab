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

import com.esofthead.mycollab.configuration.FileStorage;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.servlet.GenericHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = "/avatar/*", name = "userAvatarFSServlet")
public class UserAvatarHttpServletRequestHandler extends GenericHttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(UserAvatarHttpServletRequestHandler.class);

    @Override
    protected void onHandleRequest(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        if (!StorageFactory.getInstance().isFileStorage()) {
            throw new MyCollabException("This servlet support file system setting only");
        }

        String path = request.getPathInfo();
        String username;
        int size;

        if (path != null) {
            String[] params = path.split("/");
            if (params.length >= 3) {
                username = params[1];
                size = Integer.parseInt(params[2]);

                if (size <= 0) {
                    LOG.error("Error to get avatar", new MyCollabException(
                            String.format("Invalid request for avatar %s", path)));
                    return;
                }
            } else {
                LOG.error("Error to get avatar", new MyCollabException(
                        String.format("Invalid request for avatar %s", path)));
                return;
            }

            FileStorage fileStorage = (FileStorage) StorageFactory.getInstance();
            File avatarFile = fileStorage.getAvatarFile(username, size);
            InputStream avatarInputStream;
            if (avatarFile != null) {
                avatarInputStream = new FileInputStream(avatarFile);
            } else {
                String userAvatarPath = String.format("assets/icons/default_user_avatar_%d.png", size);
                avatarInputStream = UserAvatarHttpServletRequestHandler.class
                        .getClassLoader().getResourceAsStream(userAvatarPath);
                if (avatarInputStream == null) {
                    LOG.error("Error to get avatar", new MyCollabException(
                            "Invalid request for avatar " + path));
                    return;
                }
            }

            response.setHeader("Content-Type", "image/png");
            response.setHeader("Content-Length", String.valueOf(avatarInputStream.available()));

            try (BufferedInputStream input = new BufferedInputStream(avatarInputStream);
                 BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
                byte[] buffer = new byte[8192];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
        }
    }
}
