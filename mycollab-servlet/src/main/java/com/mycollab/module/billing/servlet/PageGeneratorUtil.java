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
package com.mycollab.module.billing.servlet;

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.spring.AppContextUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class PageGeneratorUtil {
    static void responseUserNotExistPage(HttpServletResponse response, String username, String loginURL) throws IOException, TemplateException {
        Map<String, Object> context = new HashMap<>();
        context.put("loginURL", loginURL);
        context.put("username", username);
        Map<String, String> defaultUrls = SiteConfiguration.defaultUrls();
        defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
        defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
        context.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        Configuration templateEngine = AppContextUtil.getSpringBean(Configuration.class);
        Template template = templateEngine.getTemplate("pageUserNotExist.ftl", Locale.US);
        template.process(context, writer);

        String html = writer.toString();
        PrintWriter out = response.getWriter();
        out.println(html);
    }
}
