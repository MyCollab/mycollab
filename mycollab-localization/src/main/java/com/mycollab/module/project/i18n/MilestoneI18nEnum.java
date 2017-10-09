package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-milestone")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum MilestoneI18nEnum {
    LIST,
    NEW,
    DETAIL,
    SINGLE,

    WIDGET_CLOSED_PHASE_TITLE,
    WIDGET_INPROGRESS_PHASE_TITLE,
    WIDGET_FUTURE_PHASE_TITLE,

    OPT_ROADMAP_VALUE,
    OPT_TIMELINE,
    OPT_EDIT_PHASE_NAME,
    OPT_HIDE_CLOSED_MILESTONES,
    OPT_SHOW_CLOSED_MILESTONES,

    FORM_STATUS_FIELD_HELP,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING
}
