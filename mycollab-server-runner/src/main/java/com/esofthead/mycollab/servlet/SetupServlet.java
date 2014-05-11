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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.esofthead.mycollab.configuration.SharingOptions;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class SetupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		VelocityContext context = new VelocityContext();
		Reader reader = null;

		try {
			reader = new InputStreamReader(SetupServlet.class.getClassLoader()
					.getResourceAsStream("templates/page/SetupFresh.mt"),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(SetupServlet.class.getClassLoader()
					.getResourceAsStream("templates/page/SetupFresh.mt"));
		}

		String postUrl = "/install";
		context.put("redirectURL", postUrl);

		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", "/assets/images/email/");
		defaultUrls.put("app_url", "/");

		SharingOptions sharingOptions = SharingOptions
				.getDefaultSharingOptions();

		defaultUrls.put("facebook_url", sharingOptions.getFacebookUrl());
		defaultUrls.put("google_url", sharingOptions.getGoogleplusUrl());
		defaultUrls.put("linkedin_url", sharingOptions.getLinkedinUrl());
		defaultUrls.put("twitter_url", sharingOptions.getTwitterUrl());

		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		VelocityEngine voEngine = new VelocityEngine();

		voEngine.evaluate(context, writer, "log task", reader);

		PrintWriter out = response.getWriter();
		out.print(writer.toString());
	}

}
