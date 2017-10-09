package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-tag")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum TagI18nEnum {
    ACTION_ADD,
    ACTION_DELETE,

    OPT_TAG_CLOUD,
    OPT_NO_TAG_EXISTED,
    OPT_ENTER_TAG_NAME,

    ERROR_TAG_NAME_HAS_MORE_2_CHARACTERS
}
