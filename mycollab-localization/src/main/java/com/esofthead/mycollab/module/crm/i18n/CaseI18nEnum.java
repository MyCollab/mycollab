package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/case")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum CaseI18nEnum {
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT,
	
	LIST_VIEW_TITLE,
	
	SECTION_CASE_INFORMATION,
	SECTION_DESCRIPTION,
	
	FORM_NEW_TITLE,
	
	FORM_PRIORITY,
	FORM_STATUS,
	FORM_ACCOUNT,
	FORM_ORIGIN,
	FORM_PHONE,
	FORM_TYPE,
	FORM_REASON,
	FORM_SUBJECT,
	FORM_EMAIL,
	FORM_CREATED_TIME,
	FORM_LAST_UPDATED_TIME,
	FORM_DESCRIPTION,
	FORM_RESOLUTION
}
