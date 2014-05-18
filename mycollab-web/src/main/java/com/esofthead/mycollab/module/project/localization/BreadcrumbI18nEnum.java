package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/breadcrumb")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum BreadcrumbI18nEnum {
	DASHBOARD,
	MESSAGES,
	RISKS,
	PHASES,
	PROBLEMS,
	TASKS,
	BUGS,
	VERSIONS,
	COMPONENTS,
	TIME_TRACKING, 
	FILES,
	STANDUP,
	USERS,
	ROLES
}
