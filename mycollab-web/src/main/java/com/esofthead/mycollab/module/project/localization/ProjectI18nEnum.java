package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/project")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum ProjectI18nEnum {
	FORM_HOME_PAGE,
	FORM_STATUS,
	FORM_PLAN_START_DATE,
	FORM_PLAN_END_DATE,
	FORM_ACTUAL_START_DATE,
	FORM_ACTUAL_END_DATE,
	FORM_BILLING_RATE,
	FORM_CURRENCY,
	FORM_TARGET_BUDGET,
	FORM_ACTUAL_BUDGET
}
