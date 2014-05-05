/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

/**
 * Wrapper class to get localization string.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class LocalizationHelper {
	private static Logger log = LoggerFactory
			.getLogger(LocalizationHelper.class);

	public static final String ENGLISH = "English";
	public static final String JAPANESE = "Japanese";
	public static final String SPANISH = "Spanish";

	public static final String[] SUPPORTED_LANGUAGE = { ENGLISH, JAPANESE,
			SPANISH };

	private static final Map<String, IMessageConveyor> languageMap;

	static {
		languageMap = new HashMap<String, IMessageConveyor>();
		languageMap.put(ENGLISH, new MessageConveyor(Locale.US));
		languageMap.put(JAPANESE, new MessageConveyor(Locale.JAPAN));
		languageMap.put(SPANISH, new MessageConveyor(new Locale("es", "ES")));
	}

	public static IMessageConveyor getMessageConveyor(String language) {
		if (language == null) {
			return languageMap.get(ENGLISH);
		} else {
			IMessageConveyor messageConveyor = languageMap.get(language);
			if (messageConveyor == null) {
				return languageMap.get(ENGLISH);
			}

			return messageConveyor;
		}
	}

	// LOCALIZATION
	private static IMessageConveyor english = new MessageConveyor(Locale.US);

	public static String getMessage(Enum key) {
		try {
			return english.getMessage(key);
		} catch (Exception e) {
			log.error("Can not find resource key {}", key);
			return "Undefined";
		}
	}

	public static String getMessage(Enum key, Object... objects) {
		return english.getMessage(key, objects);
	}
}
