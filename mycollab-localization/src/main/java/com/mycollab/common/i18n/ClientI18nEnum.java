package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-client")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ClientI18nEnum {
    LIST,
    NEW,
    SINGLE,
    DETAIL,
    EDIT,

    OPT_NUM_PROJECTS,
    OPT_REMOVE_CLIENT,

    MY_ITEMS,

    SECTION_ACCOUNT_INFORMATION,
    SECTION_ADDRESS_INFORMATION,
    SECTION_DESCRIPTION,

    FORM_ACCOUNT_NAME,
    FORM_WEBSITE,
    FORM_FAX,
    FORM_EMPLOYEES,
    FORM_OFFICE_PHONE,
    FORM_OTHER_PHONE,
    FORM_INDUSTRY,
    FORM_OWNERSHIP,
    FORM_ANNUAL_REVENUE,
    FORM_BILLING_ADDRESS,
    FORM_SHIPPING_ADDRESS,
    FORM_BILLING_CITY,
    FORM_SHIPPING_CITY,
    FORM_BILLING_STATE,
    FORM_SHIPPING_STATE,
    FORM_BILLING_POSTAL_CODE,
    FORM_SHIPPING_POSTAL_CODE,
    FORM_BILLING_COUNTRY,
    FORM_SHIPPING_COUNTRY,
    FORM_COPY_ADDRESS,
    FORM_ANY_PHONE,
    FORM_ANY_CITY,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_TITLE_SELECT_ACCOUNTS,
    M_VIEW_ACCOUNT_NAME_LOOKUP,
    M_TITLE_RELATED_ACCOUNTS;

    @BaseName("client-type")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum ClientType {
        Analyst,
        Competitor,
        Customer,
        Integrator,
        Investor,
        Partner,
        Press,
        Prospect,
        Reseller,
        Other
    }

    @BaseName("client-industry")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum ClientIndustry {
        Apparel,
        Banking,
        Biotechnology,
        Chemicals,
        Communications,
        Construction,
        Consulting,
        Education,
        Electronics,
        Energy,
        Engineering,
        Entertainment,
        Environmental,
        Finance,
        Government,
        Healthcare,
        Hospitality,
        Insurance,
        Machinery,
        Manufactory,
        Media,
        Not_For_Profit,
        Retail,
        Shipping,
        Technology,
        Telecommunications,
        Other
    }
}
