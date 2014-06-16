package com.esofthead.mycollab.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.LanguageSupport;

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

	private static final Map<LanguageSupport, IMessageConveyor> languageMap;

	static {
		languageMap = new HashMap<LanguageSupport, IMessageConveyor>();
		languageMap
				.put(LanguageSupport.ENGLISH, new MessageConveyor(Locale.US));
		languageMap.put(LanguageSupport.JAPAN,
				new MessageConveyor(Locale.JAPAN));
	}

	public static IMessageConveyor getMessageConveyor(LanguageSupport language) {
		if (language == null) {
			return languageMap.get(LanguageSupport.ENGLISH);
		} else {
			IMessageConveyor messageConveyor = languageMap.get(language);
			if (messageConveyor == null) {
				return languageMap.get(LanguageSupport.ENGLISH);
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
			log.error("Can not find resource key " + key, e);
			return "Undefined";
		}
	}

	public static String getMessage(Enum key, Object... objects) {
		try {
			return english.getMessage(key, objects);
		} catch (Exception e) {
			log.error("Can not find resource key " + key, e);
			return "Undefined";
		}
	}
}
