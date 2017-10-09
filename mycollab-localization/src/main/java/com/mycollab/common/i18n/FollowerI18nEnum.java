package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-follower")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum FollowerI18nEnum {
    FORM_PROJECT_NAME,
    FORM_SUMMARY,

    FOLLOWER_EXPLAIN_HELP,
    OPT_MY_FOLLOWING_TICKETS,
    OPT_FOLLOWER_CREATE_DATE,
    OPT_SUB_INFO_WATCHERS,
}
