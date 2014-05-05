package com.esofthead.mycollab.common.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/lang")
@LocaleData({ @Locale("en_US") })
public enum LangI18Enum {
	ENGLISH, SPANISH, JAPANESE
}
