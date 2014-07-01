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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.template.velocity.TemplateContext;
import com.esofthead.mycollab.template.velocity.TemplateEngine;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("appExceptionHandlerServlet")
public class AppExceptionHandler extends GenericServletRequestHandler {

	@Autowired
	private TemplateEngine templateEngine;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Integer status_code = (Integer) request
				.getAttribute("javax.servlet.error.status_code");
		String requestUri = (String) request
				.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		if (status_code == 404) {
			responsePage404(response);
		} else {
			responsePage500(response);
		}
	}

	private void responsePage404(HttpServletResponse response)
			throws IOException {

		String pageNotFoundTemplate = "templates/page/404Page.mt";
		TemplateContext context = new TemplateContext();

		Reader reader;
		try {
			reader = new InputStreamReader(
					AppExceptionHandler.class.getClassLoader()
							.getResourceAsStream(pageNotFoundTemplate), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(AppExceptionHandler.class
					.getClassLoader().getResourceAsStream(pageNotFoundTemplate));
		}
		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		templateEngine.evaluate(context, writer, "log task", reader);

		String html = writer.toString();
		PrintWriter out = response.getWriter();
		out.println(html);
	}

	private void responsePage500(HttpServletResponse response)
			throws IOException {
		String pageNotFoundTemplate = "templates/page/500Page.mt";
		TemplateContext context = new TemplateContext();

		Reader reader;
		try {
			reader = new InputStreamReader(
					AppExceptionHandler.class.getClassLoader()
							.getResourceAsStream(pageNotFoundTemplate), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(AppExceptionHandler.class
					.getClassLoader().getResourceAsStream(pageNotFoundTemplate));
		}
		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		templateEngine.evaluate(context, writer, "log task", reader);

		String html = writer.toString();
		PrintWriter out = response.getWriter();
		out.println(html);
	}
}
