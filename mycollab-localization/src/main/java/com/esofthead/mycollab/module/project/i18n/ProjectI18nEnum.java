package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/project")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum ProjectI18nEnum {
	BUTTON_MORE,
	
	DIALOG_NEW_PROJECT_TITLE,
	
	FORM_HOME_PAGE,
	FORM_NAME,
	FORM_SHORT_NAME,
	FORM_STATUS,
	FORM_PLAN_START_DATE,
	FORM_PLAN_END_DATE,
	FORM_ACTUAL_START_DATE,
	FORM_ACTUAL_END_DATE,
	FORM_BILLING_RATE,
	FORM_BILLABLE_HOURS,
	FORM_NON_BILLABLE_HOURS,
	FORM_CURRENCY,
	FORM_TARGET_BUDGET,
	FORM_ACTUAL_BUDGET
}
