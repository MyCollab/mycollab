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

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.server.jetty.ServerInstance;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.joda.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class UpgradeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        Configuration configuration = SiteConfiguration.freemarkerConfiguration();

        Template template = ServerInstance.getInstance().isUpgrading() ? configuration.getTemplate("pageWaitingUpgrade.ftl") :
                configuration.getTemplate("pageNoUpgrade.ftl");

        Map<String, Object> context = new HashMap<>();
        Map<String, String> defaultUrls = new HashMap<>();
        defaultUrls.put("cdn_url", "/assets/");
        defaultUrls.put("app_url", "/");
        defaultUrls.put("facebook_url", SiteConfiguration.getFacebookUrl());
        defaultUrls.put("google_url", SiteConfiguration.getGoogleUrl());
        defaultUrls.put("linkedin_url", SiteConfiguration.getLinkedinUrl());
        defaultUrls.put("twitter_url", SiteConfiguration.getTwitterUrl());

        context.put("current_year", new LocalDate().getYear());
        context.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        try {
            template.process(context, writer);
        } catch (TemplateException e) {
            throw new IOException(e);
        }
        PrintWriter out = response.getWriter();
        out.print(writer.toString());
    }
}
