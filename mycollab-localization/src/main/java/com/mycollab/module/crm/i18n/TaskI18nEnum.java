package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("crm-assignment")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum TaskI18nEnum {
    NEW,
    SINGLE,

    SECTION_TASK_INFORMATION,

    FORM_SUBJECT,
    FORM_CONTACT,
    FORM_PRIORITY,
    FORM_RELATED_TO,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING
}
