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

@BaseName("localization/project/task")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum TaskI18nEnum {
	NEW_TASKGROUP_ACTION,
	NEW_TASK_ACTION,
	DISPLAY_GANTT_CHART_ACTION,
	REODER_TASKGROUP_ACTION,
	BACK_TO_DASHBOARD_ACTION,

	TABLE_TASK_NAME_HEADER,
	TABLE_START_DATE_HEADER,
	TABLE_DUE_DATE_HEADER,
	TABLE_PER_COMPLETE_HEADER,
	TABLE_ASSIGNEE_HEADER,
	TABLE_KEY_HEADER,

	FORM_NEW_TASK_TITLE,
	FORM_EDIT_TASK_TITLE,
	FORM_TASKGROUP_FIELD,
	FORM_PHASE_FIELD,
	FORM_COMMENT_FIELD,
	FORM_NOTES_ATTACHMENT_FIELD,
	FORM_TASK_NAME,
	FORM_START_DATE,
	FORM_END_DATE,
	FORM_ACTUAL_START_DATE,
	FORM_ACTUAL_END_DATE,
	FORM_DEADLINE,
	FORM_PRIORITY,
	FORM_PERCENTAGE_COMPLETE,
	FORM_NOTES_FIELD,
	FORM_ATTACHMENT_FIELD,
	FORM_IS_ESTIMATED_FIELD,
	
	FILTER_TASK_BY_ASSIGNEE,

	ASSIGN_TASKGROUP_TITLE,
	ASSIGN_TASK_TITLE,
	NEW_TASK_TITLE,
	NEW_TASKGROUP_TITLE,

	UNRESOLVED_BY_ASSIGNEE_WIDGET_TITLE,
	UNRESOLVED_BY_PRIORITY_WIDGET_TITLE,
	
	FOLLOWERS_TAB,
	TIME_TAB,
	
	UNDEFINED_USER
}
