package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/crm_type")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum CrmTypeI18nEnum {
	ACCOUNT, CONTACT, CAMPAIGN, LEAD, OPPORTUNITY, CASES, TASK, MEETING, CALL
}
