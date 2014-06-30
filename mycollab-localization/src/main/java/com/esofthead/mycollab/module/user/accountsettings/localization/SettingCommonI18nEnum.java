package com.esofthead.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/accountsettings/setting")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum SettingCommonI18nEnum {

	BUTTON_CHANGE_LOGO,
	BUTTON_RESET_DEFAULT,
	
	FORM_TOP_MENU,
	FORM_NORMAL_TAB,
	FORM_NORMAL_MENU,
	FORM_NORMAL_MENU_TEXT,
	FORM_NORMAL_TAB_TEXT
}
