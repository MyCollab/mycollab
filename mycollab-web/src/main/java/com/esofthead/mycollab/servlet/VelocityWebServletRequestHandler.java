package com.esofthead.mycollab.servlet;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.configuration.SharingOptions;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.billing.servlet.VerifyUserServletRequestHandler;
import com.esofthead.template.velocity.TemplateContext;
import com.esofthead.template.velocity.TemplateEngine;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public abstract class VelocityWebServletRequestHandler extends
		GenericServletRequestHandler {

	protected TemplateContext pageContext = new TemplateContext();

	protected String generatePageByTemplate(String templatePath,
			Map<String, Object> params) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(
					VerifyUserServletRequestHandler.class.getClassLoader()
							.getResourceAsStream(templatePath), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(
					VerifyUserServletRequestHandler.class.getClassLoader()
							.getResourceAsStream(templatePath));
		}

		if (params != null) {
			for (String key : params.keySet()) {
				pageContext.put(key, params.get(key));
			}
		}

		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		defaultUrls.put("app_url", SiteConfiguration.getAppUrl());
		
		SharingOptions sharingOptions = SiteConfiguration
				.getSharingOptions();
		
		defaultUrls.put("facebook_url", sharingOptions.getFacebookUrl());
		defaultUrls.put("google_url", sharingOptions.getGoogleplusUrl());
		defaultUrls.put("linkedin_url", sharingOptions.getLinkedinUrl());
		defaultUrls.put("twitter_url", sharingOptions.getTwitterUrl());
		
		pageContext.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		TemplateEngine.evaluate(pageContext, writer, "log task", reader);
		return writer.toString();
	}
}
