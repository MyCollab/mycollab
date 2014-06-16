package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/campaign")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum CampaignI18nEnum {
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT,
	
	LIST_VIEW_TITLE,
	
	SECTION_CAMPAIGN_INFORMATION,
	SECTION_GOAL,
	SECTION_DESCRIPTION,
	
	FORM_NEW_TITLE,
	
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
	FORM_DESCRIPTION
}
