package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/risk")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum RiskI18nEnum {
	VIEW_LIST_TITLE,
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,
	
	BUTTON_NEW_RISK,
	
	FORM_READ_TITLE,
	FORM_NAME,
	FORM_DESCRIPTION,
	FORM_RAISED_BY,
	FORM_RELATED,
	FORM_DATE_DUE,
	FORM_STATUS,
	FORM_RESPONSE,
	FORM_CONSEQUENCE,
	FORM_PROBABILITY,
	FORM_RATING,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
