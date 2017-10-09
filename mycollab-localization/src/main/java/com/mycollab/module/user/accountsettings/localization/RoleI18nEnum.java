package com.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("account-role")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum RoleI18nEnum {
    SINGLE,
    LIST,
    NEW,
    DETAIL,

    FORM_IS_DEFAULT,
    FORM_IS_DEFAULT_HELP,

    SECTION_INFORMATION,
    FORM_PERMISSION_HEADER,
    SECTION_PROJECT_MANAGEMENT_TITLE,
    SECTION_CRM_TITLE,
    SECTION_DOCUMENT_TITLE,
    SECTION_ACCOUNT_MANAGEMENT_TITLE,

    OPT_ACCOUNT_OWNER,
    OPT_BILLING_MANAGEMENT,
    OPT_THEME,
    OPT_CREATE_NEW_PROJECT,
    OPT_GLOBAL_PROJECT_SETTINGS,
    OPT_PUBLIC_DOCUMENTS,

    ERROR_CAN_NOT_DELETE_SYSTEM_ROLE,
    ERROR_ONLY_OWNER_CAN_ASSIGN_OWNER_ROLE
}
