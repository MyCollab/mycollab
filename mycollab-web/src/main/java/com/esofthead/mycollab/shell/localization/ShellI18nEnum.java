package com.esofthead.mycollab.shell.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/shell/shell")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum ShellI18nEnum {
	NO_SUB_DOMAIN_ERROR, BACK_TO_HOME_PAGE
}
