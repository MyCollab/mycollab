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

import com.esofthead.mycollab.configuration.SharingOptions;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.template.velocity.TemplateContext;
import com.esofthead.mycollab.template.velocity.TemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public abstract class VelocityWebServletRequestHandler extends GenericHttpServlet {

	@Autowired
	private TemplateEngine templateEngine;

	protected TemplateContext pageContext = new TemplateContext();

	public String generatePageByTemplate(Locale locale, String templatePath,
			Map<String, Object> params) {
		Reader reader = LocalizationHelper.templateReader(templatePath, locale);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				pageContext.put(entry.getKey(), entry.getValue());
			}
		}

		Map<String, String> defaultUrls = new HashMap<>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		defaultUrls.put("app_url", SiteConfiguration.getAppUrl());

		SharingOptions sharingOptions = SharingOptions.getDefaultSharingOptions();

		defaultUrls.put("facebook_url", sharingOptions.getFacebookUrl());
		defaultUrls.put("google_url", sharingOptions.getGoogleplusUrl());
		defaultUrls.put("linkedin_url", sharingOptions.getLinkedinUrl());
		defaultUrls.put("twitter_url", sharingOptions.getTwitterUrl());

		pageContext.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		templateEngine.evaluate(pageContext, writer, "log task", reader);
		return writer.toString();
	}
}
