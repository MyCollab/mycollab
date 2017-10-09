package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-page")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum PageI18nEnum {
    LIST,
    NEW,
    DETAIL,
    SINGLE,

    NEW_GROUP,
    DETAIL_GROUP,

    FORM_GROUP,
    FORM_SUBJECT,
    FORM_VISIBILITY,
    FORM_CATEGORY,

    OPT_CREATED_USER,
    OPT_SORT_LABEL,
    OPT_SORT_BY_DATE,
    OPT_SORT_BY_NAME,
    OPT_SORT_BY_KIND,

    LABEL_LAST_UPDATE,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING
}