package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/call")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum CallI18nEnum {
	VIEW_NEW_TITLE,
	BUTTON_NEW_CALL,
	
	FORM_SUBJECT,
	FORM_STATUS,
	FORM_START_DATE_TIME,
	FORM_RELATED,
	FORM_DURATION,
	FORM_PURPOSE,
	FORM_RESULT,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
