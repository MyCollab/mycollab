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

import com.esofthead.mycollab.configuration.SiteConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public abstract class TemplateWebServletRequestHandler extends GenericHttpServlet {

    @Autowired
    private Configuration templateEngine;

    public String generatePageByTemplate(Locale locale, String templatePath, Map<String, Object> params) throws IOException, TemplateException {
        Map<String, Object> pageContext = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                pageContext.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String, String> defaultUrls = SiteConfiguration.defaultUrls();

        defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
        defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
        pageContext.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        //Load template from source folder
        Template template = templateEngine.getTemplate(templatePath, locale);
        template.process(pageContext, writer);
        return writer.toString();
    }
}
