/**
 * This file is part of mycollab-server-runner.
 *
 * mycollab-server-runner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-server-runner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-server-runner.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.servlet;

import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class AssetHttpServletRequestHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AssetHttpServletRequestHandler.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String resourcePath = "assets" + path;

        InputStream inputStream = AssetHttpServletRequestHandler.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            resourcePath = "VAADIN/themes/mycollab" + path;
            inputStream = AssetHttpServletRequestHandler.class.getClassLoader().getResourceAsStream(resourcePath);
        }

        if (inputStream != null) {
            response.setHeader("Content-Type", MimeTypesUtil.detectMimeType(path));
            response.setHeader("Content-Length", String.valueOf(inputStream.available()));

            try (BufferedInputStream input = new BufferedInputStream(inputStream);
                 BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
                byte[] buffer = new byte[8192];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
        } else {
            LOG.error("Can not find resource has path {}", path);
        }
    }
}
