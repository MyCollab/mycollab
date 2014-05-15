package com.esofthead.mycollab.common.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.1
 * 
 */
@BaseName("localization/window")
@LocaleData({ @Locale("en_US") })
public enum WindowI18nEnum {
	INFORMATION_WINDOW_TITLE,
	WARNING_WINDOW_TITLE,
	ERROR_WINDOW_TITLE,
	NO_PERMISSION_DO_TASK;
}
