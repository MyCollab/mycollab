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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;

import com.esofthead.mycollab.configuration.SharingOptions;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.template.velocity.TemplateContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
class PageGeneratorUtil {

	static void responeUserNotExistPage(HttpServletResponse response,
			String username, String loginURL) throws IOException {
		String pageNotFoundTemplate = "templates/page/UserNotExistPage.mt";
		TemplateContext context = new TemplateContext();

		Reader reader = LocalizationHelper.templateReader(pageNotFoundTemplate,
				SiteConfiguration.getDefaultLocale());

		context.put("loginURL", loginURL);
		context.put("username", username);
		Map<String, String> defaultUrls = new HashMap<>();
		SharingOptions sharingOptions = SharingOptions
				.getDefaultSharingOptions();
		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
		defaultUrls.put("facebook_url", sharingOptions.getFacebookUrl());
		defaultUrls.put("google_url", sharingOptions.getGoogleplusUrl());
		defaultUrls.put("linkedin_url", sharingOptions.getLinkedinUrl());
		defaultUrls.put("twitter_url", sharingOptions.getTwitterUrl());
		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		VelocityEngine templateEngine = ApplicationContextUtil
				.getSpringBean(VelocityEngine.class);
		templateEngine.evaluate(context.getVelocityContext(), writer,
				"log task", reader);

		String html = writer.toString();
		PrintWriter out = response.getWriter();
		out.println(html);
	}
}
