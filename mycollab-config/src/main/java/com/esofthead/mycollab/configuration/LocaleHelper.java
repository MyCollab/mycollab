package com.esofthead.mycollab.configuration;

import java.util.Locale;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class LocaleHelper {
	public static Locale toLocale(String language) {
		if (language == null) {
			return Locale.US;
		}

		if ("Japanese".equals(language)) {
			return Locale.JAPAN;
		} else if ("English".equals(language)) {
			return Locale.US;
		}

		return SiteConfiguration.getDefaultLocale();
	}
}
