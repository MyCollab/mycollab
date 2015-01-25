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

@BaseName("localization/project-taskgroup")
@LocaleData(value = { @Locale("en-US"), @Locale("ja-JP") }, defaultCharset = "UTF-8")
public enum TaskGroupI18nEnum {
	FORM_NEW_TASKGROUP_TITLE,
	FORM_EDIT_TASKGROUP_TITLE,
	FORM_VIEW_TASKGROUP_TITLE,

	FORM_NAME_FIELD,
	FORM_STATUS,
	FORM_DESCRIPTION_FIELD,
	FORM_PHASE_FIELD,
	FORM_PROGRESS_FIELD,
	FORM_SUB_TASKS_FIELD,

	FILTER_ALL_TASK_GROUPS_TITLE,
	FILTER_ACTIVE_TASK_GROUPS_TITLE,
	FILTER_ARCHIEVED_TASK_GROUPS_TITLE,

	FILTER_ALL_TASKS,
	FILTER_ACTIVE_TASKS,
	FILTER_PENDING_TASKS,
	FILTER_ARCHIEVED_TASKS,

	ADVANCED_VIEW_TOOLTIP,
	LIST_VIEW_TOOLTIP,

	TASKS_TAB,

	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT,

	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT,
	MAIL_CREATE_ITEM_HEADING,
	MAIL_UPDATE_ITEM_HEADING,
	MAIL_COMMENT_ITEM_HEADING,

	M_VIEW_LIST_TITLE,
	M_VIEW_NEW_TITLE,
	M_VIEW_TASKLIST_LOOKUP,
	M_FORM_READ_TITLE

}
