/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.i18n;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyorExt;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;

/**
 * Wrapper class to get localization string.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class LocalizationHelper {
	private static final Logger LOG = LoggerFactory
			.getLogger(LocalizationHelper.class);

	private static final Map<Locale, IMessageConveyor> languageMap;

	public static final Locale defaultLocale = Locale.US;

	private static IMessageConveyor defaultMessage = new MessageConveyorExt(
			Locale.US);

	static {
		languageMap = new HashMap<Locale, IMessageConveyor>();
		languageMap.put(Locale.US, defaultMessage);
	}

	public static IMessageConveyor getMessageConveyor(Locale language) {
		if (language == null) {
			return languageMap.get(SiteConfiguration.getDefaultLocale());
		} else {
			IMessageConveyor messageConveyor = languageMap.get(language);
			if (messageConveyor == null) {
				messageConveyor = new MessageConveyorExt(language);
				languageMap.put(language, messageConveyor);
			}

			return messageConveyor;
		}
	}

	public static String getMessage(Locale locale, Enum<?> key,
			Object... objects) {
		try {
			IMessageConveyor messageConveyor = getMessageConveyor(locale);
			return messageConveyor.getMessage(key, objects);
		} catch (Exception e) {
			try {
				return defaultMessage.getMessage(key, objects);
			} catch (Exception e1) {
				LOG.error("Can not find resource key " + key, e);
				return "Undefined";
			}

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getMessage(Locale locale, Class<? extends Enum> cls,
			String option, Object... objects) {
		if (StringUtils.isBlank(option)) {
			return "";
		}
		Enum key = Enum.valueOf(cls, option);
		try {
			IMessageConveyor messageConveyor = getMessageConveyor(locale);
			return messageConveyor.getMessage(key, objects);
		} catch (Exception e) {
			try {
				return defaultMessage.getMessage(key, objects);
			} catch (Exception e1) {
				LOG.error("Can not find resource key " + cls + "---" + option,
						e);
				return "Undefined";
			}
		}
	}

	private static Map<String, String> cacheFile = new HashMap<String, String>();

	public static String templatePath(String fileTemplatePath, Locale locale) {
		String key = (locale != null) ? (fileTemplatePath + locale.toString())
				: (fileTemplatePath + Locale.US.toString());
		String filePath = cacheFile.get(key);
		if (filePath != null) {
			return filePath;
		} else {
			int index = fileTemplatePath.indexOf("mt");
			if (index == -1) {
				throw new MyCollabException("File type is not supported "
						+ fileTemplatePath);
			}
			filePath = fileTemplatePath.substring(0, index - 1);
			filePath = String.format("%s_%s.mt", filePath, locale);
			cacheFile.put(key, filePath);
			return filePath;
		}
	}

	/**
	 * 
	 * @param fileTemplatePath
	 * @param locale
	 * @return the reader of <code>fileTemplatePath</code> if it is found.
	 *         Otherwise, return null
	 */
	public static Reader templateReader(String fileTemplatePath, Locale locale) {
		String templatePath = templatePath(fileTemplatePath, locale);
		InputStream resourceStream = LocalizationHelper.class.getClassLoader()
				.getResourceAsStream(templatePath);
		if (resourceStream == null) {
			return null;
		}

		Reader reader;
		try {
			reader = new InputStreamReader(resourceStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(resourceStream);
		}

		return reader;
	}
}
