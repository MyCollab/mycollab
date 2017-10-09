package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-role")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ProjectRoleI18nEnum {
    LIST,
    DETAIL,
    NEW,
    SINGLE,

    SECTION_PERMISSIONS,
    OPT_ADMIN_ROLE_DISPLAY,

    ERROR_ONLY_OWNER_ASSIGN_ROLE_OWNER
}
