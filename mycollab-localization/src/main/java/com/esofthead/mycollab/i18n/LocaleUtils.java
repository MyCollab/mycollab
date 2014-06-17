package com.esofthead.mycollab.i18n;

import java.util.Locale;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class LocaleUtils {
	public static Locale toLocale(String language) {
		if (language == null) {
			return Locale.US;
		}

		if ("Japanese".equals(language)) {
			return Locale.JAPAN;
		}

		return Locale.US;
	}
}
