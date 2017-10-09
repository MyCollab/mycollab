package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-standup")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum StandupI18nEnum {
    SINGLE,
    VIEW_LIST_TITLE,
    CHOOSE_REPORT_DATE,
    BUTTON_ADD_REPORT_LABEL,
    STANDUP_LASTDAY,
    STANDUP_TODAY,
    STANDUP_ISSUE,
    STANDUP_ISSUE_SHORT,
    STANDUP_MEMBER_NOT_REPORT,
    HINT1_MSG,
    HINT2_MG,
    FORM_EDIT_TITLE,
}
