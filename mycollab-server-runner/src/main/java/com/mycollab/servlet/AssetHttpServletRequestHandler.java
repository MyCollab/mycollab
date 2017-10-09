package com.mycollab.servlet;

import com.mycollab.core.utils.MimeTypesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

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
