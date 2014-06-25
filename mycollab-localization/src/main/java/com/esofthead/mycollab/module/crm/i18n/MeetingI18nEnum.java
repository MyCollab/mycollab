package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/meeting")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum MeetingI18nEnum {
	VIEW_NEW_TITLE,
	BUTTON_NEW_MEETING,
	
	FORM_SUBJECT,
	FORM_START_DATE_TIME,
	FORM_STATUS,
	FORM_END_DATE_TIME,
	FORM_LOCATION,

	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
