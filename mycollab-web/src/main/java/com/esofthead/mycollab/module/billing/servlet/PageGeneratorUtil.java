package com.esofthead.mycollab.module.billing.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;

import com.esofthead.mycollab.configuration.SharingOptions;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.template.velocity.TemplateContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class PageGeneratorUtil {

	public static void responeUserNotExistPage(
			HttpServletResponse response, String loginURL)
			throws IOException {
		String pageNotFoundTemplate = "templates/page/UserNotExistPage.mt";
		TemplateContext context = new TemplateContext();

		Reader reader;
		try {
			reader = new InputStreamReader(PageGeneratorUtil.class
					.getClassLoader().getResourceAsStream(
							pageNotFoundTemplate), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(PageGeneratorUtil.class
					.getClassLoader().getResourceAsStream(
							pageNotFoundTemplate));
		}
		context.put("loginURL", loginURL);
		Map<String, String> defaultUrls = new HashMap<String, String>();
		SharingOptions sharingOptions = SiteConfiguration
				.getSharingOptions();
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
