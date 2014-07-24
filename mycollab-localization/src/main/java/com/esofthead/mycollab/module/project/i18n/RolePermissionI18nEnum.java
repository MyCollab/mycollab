package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/prj_role_permission")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum RolePermissionI18nEnum {
	Message,
	Milestone,
	Task,
	Bug,
	Version,
	Component,
	Risk,
	Problem,
	User,
	Role,
	Project
}
