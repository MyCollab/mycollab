package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class OptionI18nEnum {

	@BaseName("localization/crm/accounttype")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public static enum AccountType {
		Analyst,
		Competitor,
		Customer,
		Integrator,
		Investor,
		Partner,
		Press,
		Prospect,
		Reseller,
		Other;
	}

}
