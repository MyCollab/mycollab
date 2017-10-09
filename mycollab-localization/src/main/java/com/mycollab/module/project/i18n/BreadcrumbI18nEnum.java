package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-breadcrumb")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum BreadcrumbI18nEnum {
    FRA_TIME_TRACKING,
    FRA_FILES,
    FRA_STANDUP,
    FRA_STANDUP_FOR_DAY,
    FRA_MEMBERS,
    FRA_INVITE_MEMBERS,
    FRA_MEMBER_READ,
    FRA_MEMBER_EDIT,
    FRA_SETTING
}
