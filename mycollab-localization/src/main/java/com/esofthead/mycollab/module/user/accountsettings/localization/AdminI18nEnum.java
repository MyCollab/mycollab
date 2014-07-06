package com.esofthead.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/accountsettings/admin")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum AdminI18nEnum {
	VIEW_PROFILE,
	VIEW_BILLING,
	VIEW_CUSTOMIZE,
	VIEW_USERS_AND_ROLES,
	
	TAB_USER,
	TAB_ROLE
}
