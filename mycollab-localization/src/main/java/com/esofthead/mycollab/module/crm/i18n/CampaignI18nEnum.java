package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/campaign")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum CampaignI18nEnum {
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,
	VIEW_LIST_TITLE,
	VIEW_NEW_TITLE,
	
	SECTION_CAMPAIGN_INFORMATION,
	SECTION_GOAL,
	SECTION_DESCRIPTION,
	
	BUTTON_NEW_CAMPAIGN,
	
	FORM_CAMPAIGN_NAME,
	FORM_STATUS,
	FORM_START_DATE,
	FORM_END_DATE,
	FORM_TYPE,
	FORM_EXPECTED_REVENUE,
	FORM_EXPECTED_COST,
	FORM_BUDGET,
	FORM_ACTUAL_COST,
	FORM_CURRENCY,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
