/**
 * mycollab-servlet - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.servlet;

import com.mycollab.configuration.ApplicationConfiguration;
import com.mycollab.configuration.IDeploymentMode;
import com.mycollab.configuration.ServerConfiguration;
import com.mycollab.configuration.SiteConfiguration;
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

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private IDeploymentMode deploymentMode;

    public String generatePageByTemplate(Locale locale, String templatePath, Map<String, Object> params) throws IOException, TemplateException {
        Map<String, Object> pageContext = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                pageContext.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String, String> defaultUrls = applicationConfiguration.defaultUrls();

        defaultUrls.put("cdn_url", deploymentMode.getCdnUrl());
        pageContext.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        //Load template from source folder
        Template template = templateEngine.getTemplate(templatePath, locale);
        template.process(pageContext, writer);
        return writer.toString();
    }
}
