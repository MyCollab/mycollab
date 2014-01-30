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
@LocaleData({ @Locale("en_US") })
public enum TaskI18nEnum {
	NEW_PHASE_ACTION,
	NEW_TASKGROUP_ACTION,
	NEW_TASK_ACTION,
	DISPLAY_GANTT_CHART_ACTION,
	REODER_TASKGROUP_ACTION,
	TABLE_TASK_NAME_HEADER,
	TABLE_START_DATE_HEADER,
	TABLE_DUE_DATE_HEADER,
	TABLE_PER_COMPLETE_HEADER,
	TABLE_ASSIGNEE_HEADER,
	TABLE_KEY_HEADER,
	FORM_TASKGROUP_FIELD,
	FORM_PHASE_FIELD,
	FILTER_ALL_TASK_GROUPS_TITLE,
	FILTER_ACTIVE_TASK_GROUPS_TITLE,
	FILTER_ARCHIEVED_TASK_GROUPS_TITLE,
	NEW_TASKGROUP_TITLE
}
