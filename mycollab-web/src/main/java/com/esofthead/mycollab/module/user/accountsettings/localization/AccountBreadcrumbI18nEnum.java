package com.esofthead.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/accountsettings/breadcrumb")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum AccountBreadcrumbI18nEnum {
	PROFILE, BILLING, USERS, ROLES, CANCEL_ACCOUNT, CUSTOMIZE
}
