/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/common")
@LocaleData({ @Locale("en_US") })
public enum ProjectCommonI18nEnum {
	DASHBOARD_TITLE,
	FEEDS_TITLE,
	MY_PROJECTS_TITLE,
	NEWS_TITLE,
	TASKS_TITLE,
	FEED_MESSAGE_TITLE,
	FEED_PROJECT_MESSAGE_TITLE,
	FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
	FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
	FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
	FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,
	PROJECT_TASK_TITLE,
	GANTT_CHART_TITLE,
	TASK_TITLE,
	NEW_PROJECT_ACTION,
	VIEW_PROJECT_DETAIL_ACTION,
	EDIT_PROJECT_ACTION,
	DELETE_PROJECT_ACTION,
	ARCHIVE_PROJECT_ACTION,
	CONFIRM_PROJECT_DELETE_MESSAGE,
	CONFIRM_PROJECT_ARCHIVE_MESSAGE,
	DIALOG_ARCHIVE_PROJECT_TITLE
}
