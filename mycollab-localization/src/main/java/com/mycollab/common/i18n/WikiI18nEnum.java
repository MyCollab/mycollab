package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-wiki")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum WikiI18nEnum {
    status_public, status_private, status_archieved
}
