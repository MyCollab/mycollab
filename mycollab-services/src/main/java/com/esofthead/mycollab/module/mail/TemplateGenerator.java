/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.mail;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.esofthead.mycollab.configuration.SharingOptions;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.template.velocity.TemplateContext;

/**
 * Generate subject and body content of email base on velocity template engine.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TemplateGenerator {

	private final String subjectTemplate;
	private final String contentTemplatePathFile;
	private final TemplateContext templateContext;
	private VelocityEngine templateEngine;

	/**
	 * Initiate Template Generator instance
	 * 
	 * @param subjectTemplate
	 *            subject template base on velocity format
	 * @param contentTemplatePathFile
	 *            path of velocity template file seek in MyCollab classpath
	 */
	public TemplateGenerator(String subjectTemplate,
			String contentTemplatePathFile) {
		this.templateEngine = ApplicationContextUtil
				.getSpringBean(VelocityEngine.class);
		templateContext = new TemplateContext();
		this.subjectTemplate = subjectTemplate;
		this.contentTemplatePathFile = contentTemplatePathFile;

		Map<String, String> defaultUrls = new HashMap<String, String>();

		SharingOptions sharingOptions = SharingOptions
				.getDefaultSharingOptions();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		defaultUrls.put("facebook_url", sharingOptions.getFacebookUrl());
		defaultUrls.put("google_url", sharingOptions.getGoogleplusUrl());
		defaultUrls.put("linkedin_url", sharingOptions.getLinkedinUrl());
		defaultUrls.put("twitter_url", sharingOptions.getTwitterUrl());

		templateContext.put("defaultUrls", defaultUrls);
	}

	public void putVariable(String key, Object value) {
		templateContext.put(key, value);
	}

	/**
	 * Generate subject content base template
	 * 
	 * @return email subject
	 */
	public String generateSubjectContent() {
		StringWriter writer = new StringWriter();
		Reader reader = new StringReader(subjectTemplate);
		templateEngine.evaluate(templateContext.getVelocityContext(), writer,
				"log task", reader);
		return writer.toString();
	}

	/**
	 * Generate body content base template
	 * 
	 * @return email body
	 */
	public String generateBodyContent() {
		StringWriter writer = new StringWriter();
		Reader reader;
		try {
			reader = new InputStreamReader(TemplateGenerator.class
					.getClassLoader().getResourceAsStream(
							contentTemplatePathFile), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(TemplateGenerator.class
					.getClassLoader().getResourceAsStream(
							contentTemplatePathFile));
		} catch (Exception e) {
			throw new MyCollabException("Exception while read file path "
					+ contentTemplatePathFile, e);
		}

		templateEngine.evaluate(templateContext.getVelocityContext(), writer,
				"log task", reader);
		return writer.toString();
	}
}
