/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.servlet;

import com.mycollab.configuration.SiteConfiguration;
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
 * @since 4.1
 */
public class SetupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        Configuration configuration = SiteConfiguration.freemarkerConfiguration();
        Map<String, Object> context = new HashMap<>();

        String postUrl = "/install";
        context.put("postUrl", postUrl);

        Map<String, String> defaultUrls = new HashMap<>();
        defaultUrls.put("cdn_url", "/assets/");
        defaultUrls.put("app_url", "/");
        defaultUrls.put("facebook_url", "https://www.facebook.com/mycollab2");
        defaultUrls.put("google_url", "https://plus.google.com/u/0/b/112053350736358775306/+Mycollab/about/p/pub");
        defaultUrls.put("twitter_url", "https://twitter.com/mycollabdotcom");
        context.put("defaultUrls", defaultUrls);
        context.put("current_year", new LocalDate().getYear());

        StringWriter writer = new StringWriter();
        Template template = configuration.getTemplate("pageSetupFresh.ftl");
        try {
            template.process(context, writer);
        } catch (TemplateException e) {
            throw new IOException(e);
        }

        PrintWriter out = response.getWriter();
        out.print(writer.toString());
    }
}
