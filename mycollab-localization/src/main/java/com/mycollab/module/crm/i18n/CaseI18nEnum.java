package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("crm-case")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum CaseI18nEnum {
    LIST,
    NEW,
    SINGLE,

    SECTION_CASE_INFORMATION,
    SECTION_DESCRIPTION,

    FORM_PRIORITY,
    FORM_ACCOUNT,
    FORM_ORIGIN,
    FORM_REASON,
    FORM_SUBJECT,
    FORM_RESOLUTION,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_VIEW_CASE_NAME_LOOKUP,
    M_TITLE_RELATED_CASES
}
