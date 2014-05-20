package com.esofthead.mycollab.module.crm.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/meeting")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum MeetingI18nEnum {
	FORM_START_DATE_TIME, FORM_STATUS, FORM_END_DATE_TIME, FORM_LOCATION
}
