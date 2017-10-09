package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
@BaseName("project-invoice")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum InvoiceI18nEnum {
    NEW,
    EDIT,
    SINGLE,
    LIST,

    BUTTON_NEW_INVOICE,
    FORM_NOID_FIELD,
    FORM_NOID_FIELD_HELP,
    FORM_CLIENT_FIELD,
    FORM_ISSUE_DATE_FIELD,
    FORM_STATUS_HELP,
    FORM_TYPE,
    FORM_TYPE_HELP,
    FORM_CONTACT_PERSON,
    FORM_AMOUNT,
    FORM_NOTE,
    FIX_PRICE,
    TIME_MATERIAL,
    OPT_NO_INVOICE
}
