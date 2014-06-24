package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/assignment")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum TaskI18nEnum {
	BUTTON_NEW_TASK,
	
	FORM_SUBJECT,
	FORM_START_DATE,
	FORM_STATUS,
	FORM_DUE_DATE,
	FORM_CONTACT,
	FORM_PRIORITY,
	TABLE_TYPE_HEADER,
	TABLE_END_DATE_HEADER,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
