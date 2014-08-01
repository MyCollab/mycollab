package com.esofthead.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class OptionI18nEnum {

	@BaseName("localization/common/generic_status")
	@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
	public enum StatusI18nEnum {
		Open, Closed
	}
}
