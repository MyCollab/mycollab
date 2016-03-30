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
package com.esofthead.mycollab.servlet;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.IDeploymentMode;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.template.velocity.TemplateContext;
import com.esofthead.mycollab.template.velocity.service.TemplateEngine;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = "/error", name = "appExceptionHandlerServlet")
public class AppExceptionHandler extends GenericHttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AppExceptionHandler.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private IDeploymentMode deploymentMode;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer status_code = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (request.getHeader("User-Agent") == null) {
            return;
        }

        try {
            if ((status_code != null && status_code == 404) || ("404".equals(request.getParameter("param")))) {
                HttpURI uri = ((Request) request).getUri();
                if (uri != null && (uri.getCompletePath().startsWith("/HEARTBEAT") || uri.getCompletePath().startsWith("/APP/global"))) {
                    return;
                }
                LOG.error("Page 404: " + uri);
                responsePage404(response);
            } else {
                // Analyze the servlet exception
                Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
                responsePage500(response, throwable);
            }

        } catch (Exception e) {
            LOG.error("Error in servlet", e);
        }
    }

    private void responsePage404(HttpServletResponse response) throws IOException {
        String pageNotFoundTemplate = "templates/page/404Page.mt";
        TemplateContext context = new TemplateContext();

        Reader reader = LocalizationHelper.templateReader(pageNotFoundTemplate, response.getLocale());

        Map<String, String> defaultUrls = new HashMap<>();

        defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
        defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
        context.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        templateEngine.evaluate(context, writer, "log task", reader);

        String html = writer.toString();
        PrintWriter out = response.getWriter();
        out.println(html);
    }

    private void responsePage500(HttpServletResponse response, Throwable throwable) throws IOException {
        if (throwable != null) {
            DataIntegrityViolationException integrityViolationException = getExceptionType(throwable,
                    DataIntegrityViolationException.class);
            if (integrityViolationException != null) {
                response.getWriter().println(LocalizationHelper.getMessage(LocalizationHelper.defaultLocale,
                        GenericI18Enum.ERROR_USER_NOTICE_INFORMATION_MESSAGE));
                LOG.error("Exception in MyCollab", throwable);
                return;
            }
            DataAccessException exception = getExceptionType(throwable, DataAccessException.class);
            if (exception != null) {
                response.getWriter().println("<h1>Error establishing a database connection</h1>");
                return;
            }
            LOG.error("Exception in mycollab", throwable);
        }

        String errorPage = "templates/page/500Page.mt";
        TemplateContext context = new TemplateContext();

        Reader reader = LocalizationHelper.templateReader(errorPage,
                response.getLocale());
        Map<String, String> defaultUrls = new HashMap<>();

        defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
        defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
        context.put("defaultUrls", defaultUrls);


        StringWriter writer = new StringWriter();
        templateEngine.evaluate(context, writer, "log task", reader);

        String html = writer.toString();
        PrintWriter out = response.getWriter();
        out.println(html);
    }

    private String printRequest(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        String subDomain;
        if (deploymentMode.isDemandEdition()) {
            subDomain = request.getServerName().split("\\.")[0];
        } else {
            subDomain = request.getServerName();
        }
        builder.append(String.format("Request: %s--- Agent: %s-- Sub domain: %s", ((Request) request).getUri(),
                request.getHeader("User-Agent"), subDomain));
        builder.append("\n Parameters: ");
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            builder.append(String.format("\n    param: %s----%s", param, request.getParameter(param)));
        }
        return builder.toString();
    }

    private static <T> T getExceptionType(Throwable e, Class<T> exceptionType) {
        if (exceptionType.isAssignableFrom(e.getClass())) {
            return (T) e;
        } else if (e.getCause() != null) {
            return getExceptionType(e.getCause(), exceptionType);
        } else {
            return null;
        }
    }
}
