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
package com.esofthead.mycollab.module.billing.servlet;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.template.velocity.TemplateContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class PageGeneratorUtil {
    static void responeUserNotExistPage(HttpServletResponse response, String username, String loginURL) throws IOException {
        String pageNotFoundTemplate = "templates/page/UserNotExistPage.html";
        TemplateContext context = new TemplateContext();

        Reader reader = LocalizationHelper.templateReader(pageNotFoundTemplate, Locale.US);

        context.put("loginURL", loginURL);
        context.put("username", username);
        Map<String, String> defaultUrls = new HashMap<>();
        defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
        defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
        defaultUrls.put("facebook_url", SiteConfiguration.getFacebookUrl());
        defaultUrls.put("google_url", SiteConfiguration.getGoogleUrl());
        defaultUrls.put("linkedin_url", SiteConfiguration.getLinkedinUrl());
        defaultUrls.put("twitter_url", SiteConfiguration.getTwitterUrl());
        context.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        VelocityEngine templateEngine = AppContextUtil.getSpringBean(VelocityEngine.class);
        templateEngine.evaluate(context.getVelocityContext(), writer, "log task", reader);

        String html = writer.toString();
        PrintWriter out = response.getWriter();
        out.println(html);
    }
}
