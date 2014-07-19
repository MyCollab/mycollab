/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.i18n;

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
	ROLES,
	
	FRA_MESSAGE_READ,
	FRA_TASK_DASHBOARD,
	FRA_TASKGROUP_REORDER,
	FRA_TASKGROUP_NEW,
	FRA_TASKGROUP_READ,
	FRA_TASKGROUP_EDIT,
	FRA_TASK_NEW,
	FRA_TASK_READ,
	FRA_TASK_EDIT,
	FRA_PHASE_NEW,
	FRA_PHASE_READ,
	FRA_PHASE_EDIT,
	FRA_BUG_DASHBOARD,
	FRA_BUG_LIST,
	FRA_BUG_NEW,
	FRA_BUG_READ,
	FRA_BUG_EDIT,
	FRA_VERSION_NEW,
	FRA_VERSION_READ,
	FRA_VERSION_EDIT,
	FRA_COMPONENT_READ,
	FRA_COMPONENT_EDIT,
	FRA_PROBLEM_NEW,
	FRA_PROBLEM_READ,
	FRA_PROBLEM_EDIT,
	FRA_RISK_NEW,
	FRA_RISK_READ,
	FRA_RISK_EDIT,
	FRA_TIME_TRACKING,
	FRA_FILES,
	FRA_STANDUP,
	FRA_STANDUP_FOR_DAY,
	FRA_MEMBERS,
	FRA_INVITE_MEMBERS,
	FRA_MEMBER_READ,
	FRA_MEMBER_EDIT,
	FRA_ROLES,
	FRA_ROLE_NEW,
	FRA_ROLE_READ,
	FRA_ROLE_EDIT,
	FRA_NOTIFICATION_SETTING
}
