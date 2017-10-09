package com.mycollab.module.file.servlet;

import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.utils.MimeTypesUtil;
import com.mycollab.servlet.GenericHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
@WebServlet(urlPatterns = "/assets/*", name = "assetHandler")
public class AssetHandler extends GenericHttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AssetHandler.class);

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String resourcePath = "assets" + path;

        InputStream inputStream = AssetHandler.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            resourcePath = "VAADIN/themes/mycollab" + path;
            inputStream = AssetHandler.class.getClassLoader().getResourceAsStream(resourcePath);
        }

        if (inputStream != null) {
            LOG.debug("Get resource {} successfully ", resourcePath);
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
            throw new ResourceNotFoundException("Can not find resource has path " + path);
        }
    }
}
