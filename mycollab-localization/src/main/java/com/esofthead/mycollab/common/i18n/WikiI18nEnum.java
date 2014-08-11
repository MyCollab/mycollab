package com.esofthead.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/common/wiki")
@LocaleData(value = { @Locale("en_US"), @Locale("ja_JP") }, defaultCharset = "UTF-8")
public enum WikiI18nEnum {
	status_public, status_private, status_archieved
}
