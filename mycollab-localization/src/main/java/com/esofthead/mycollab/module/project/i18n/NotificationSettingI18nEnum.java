package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/prj_notification")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum NotificationSettingI18nEnum {
	VIEW_TITLE,
	EXT_LEVEL,
	DIALOG_UPDATE_SUCCESS,
	OPT_DEFAULT_SETTING,
	OPT_NONE_SETTING,
	OPT_MINIMUM_SETTING,
	OPT_MAXIMUM_SETTING
}
