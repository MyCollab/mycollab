package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-risk")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum RiskI18nEnum {
    LIST,
    NEW,
    EDIT,
    DETAIL,
    SINGLE,

    FORM_RAISED_BY,
    FORM_RELATED,
    FORM_RESPONSE,
    FORM_CONSEQUENCE,
    FORM_PROBABILITY,

    SECTION_RISK_INFORMATION,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING
}
