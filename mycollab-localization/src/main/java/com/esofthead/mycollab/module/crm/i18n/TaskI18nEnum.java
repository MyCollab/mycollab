package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/task")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum TaskI18nEnum {
	FORM_START_DATE,
	FORM_STATUS,
	FORM_DUE_DATE,
	FORM_CONTACT,
	FORM_PRIORITY,
	TABLE_SUBJECT_HEADER,
	TABLE_TYPE_HEADER,
	TABLE_END_DATE_HEADER,
	
}
