package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("crm-activity")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ActivityI18nEnum {
    TAB_CALENDAR_TITLE,
    TAB_ACTIVITY_TITLE,
    FORM_SUBJECT,
    M_TITLE_RELATED_ACTIVITIES,
    M_VIEW_LIST_TITLE
}
