package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/activity")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum ActivityI18nEnum {
	TAB_CALENDAR_TITLE, TAB_ACTIVITY_TITLE, FORM_SUBJECT, FORM_STATUS
}
