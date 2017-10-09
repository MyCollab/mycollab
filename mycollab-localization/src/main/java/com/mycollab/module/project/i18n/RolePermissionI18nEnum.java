package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-role-permission")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum RolePermissionI18nEnum {
    LIST,

    Message,
    Milestone,
    Task,
    Bug,
    Version,
    Component,
    Page,
    Risk,
    Time,
    Finance,
    Invoice,
    User,
    Role,
    Project
}
