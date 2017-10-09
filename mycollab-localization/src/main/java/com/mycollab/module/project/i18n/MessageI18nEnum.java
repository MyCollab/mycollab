package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-message")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum MessageI18nEnum {
    LIST,
    NEW,
    SINGLE,

    FORM_TITLE,
    FORM_IS_STICK,

    USER_COMMENT_ADD,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_FORM_CONTENT_FIELD_PROMPT
}
