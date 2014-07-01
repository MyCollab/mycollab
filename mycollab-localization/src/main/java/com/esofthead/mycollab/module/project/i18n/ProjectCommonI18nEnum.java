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

@BaseName("localization/project/prj_common")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum ProjectCommonI18nEnum {
	WIDGET_MY_PROJECTS_TITLE,
	WIDGET_MEMBERS_TITLE,
	WIDGET_OVERDUE_TASKS_TITLE,

	TASKS_TITLE,

	FEED_PROJECT_MESSAGE_TITLE,
	FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
	FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
	FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
	FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,

	TOOLTIP_GANTT_CHART_TITLE,

	GANTT_CHART_TITLE,
	BUTTON_NEW_PROJECT,
	BUTTON_EDIT_PROJECT,
	BUTTON_DELETE_PROJECT,
	BUTTON_ARCHIVE_PROJECT,

	DIALOG_CONFIRM_PROJECT_DELETE_MESSAGE,
	DIALOG_ARCHIVE_PROJECT_TITLE,

	VIEW_DASHBOARD,
	VIEW_MESSAGE,
	VIEW_MILESTONE,
	VIEW_TASK,
	VIEW_BUG,
	VIEW_FILE,
	VIEW_RISK,
	VIEW_PROBLEM,
	VIEW_TIME,
	VIEW_STANDAUP,
	VIEW_MEMBER,

	TAB_COMMENT,
	TAB_HISTORY
}
