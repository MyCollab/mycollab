package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/type")
@LocaleData({ @Locale("en_US") })
public enum CrmTypeI18nEnum {
	ACCOUNT, CONTACT, CAMPAIGN, LEAD, OPPORTUNITY, CASES, TASK, MEETING, CALL
}
