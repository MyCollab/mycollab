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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.configuration.SharingOptions;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.template.velocity.TemplateContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContentGenerator implements IContentGenerator, InitializingBean {

	private TemplateContext templateContext;

	@Autowired
	private VelocityEngine templateEngine;

	@Override
	public void afterPropertiesSet() throws Exception {
		templateContext = new TemplateContext();
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

	@Override
	public void putVariable(String key, Object value) {
		templateContext.put(key, value);
	}

	@Override
	public String generateSubjectContent(String subject) {
		StringWriter writer = new StringWriter();
		Reader reader = new StringReader(subject);
		templateEngine.evaluate(templateContext.getVelocityContext(), writer,
				"log task", reader);
		return writer.toString();
	}

	@Override
	public String generateBodyContent(String templateFilePath) {
		StringWriter writer = new StringWriter();
		InputStream resourceStream = LocalizationHelper.class.getClassLoader()
				.getResourceAsStream(templateFilePath);

		Reader reader;
		try {
			reader = new InputStreamReader(resourceStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(resourceStream);
		}

		templateEngine.evaluate(templateContext.getVelocityContext(), writer,
				"log task", reader);
		return writer.toString();
	}

	@Override
	public String generateBodyContent(String templateFilePath,
			Locale currentLocale, Locale defaultLocale) {
		StringWriter writer = new StringWriter();
		Reader reader = LocalizationHelper.templateReader(templateFilePath,
				currentLocale);
		if (reader == null) {
			if (defaultLocale == null) {
				throw new MyCollabException("Can not find file "
						+ templateFilePath + " in locale " + currentLocale);
			}
			reader = LocalizationHelper.templateReader(templateFilePath,
					defaultLocale);
			if (reader == null) {
				throw new MyCollabException("Can not find file "
						+ templateFilePath + " in locale " + currentLocale
						+ " and default locale " + defaultLocale);
			}
		}

		templateEngine.evaluate(templateContext.getVelocityContext(), writer,
				"log task", reader);
		return writer.toString();
	}

	@Override
	public String generateBodyContent(String templateFilePath,
			Locale currentLocale) {
		return this.generateBodyContent(templateFilePath, currentLocale, null);
	}
}
