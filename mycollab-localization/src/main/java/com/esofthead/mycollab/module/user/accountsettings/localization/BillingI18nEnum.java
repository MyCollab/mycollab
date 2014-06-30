package com.esofthead.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/accountsettings/billing")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum BillingI18nEnum {
	VIEW_CHANGE_BILLING_PLAN_TITLE,
	
	BUTTON_CANCEL_ACCOUNT,
	
	HELP_QUESTION,
	HELP_INFO,
	
	FORM_BILLING_PRICE,
	
	QUESTION_CHANGE_PLAN
}
