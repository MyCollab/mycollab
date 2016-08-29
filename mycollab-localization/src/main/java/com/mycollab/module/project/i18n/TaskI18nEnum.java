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
package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-task")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum TaskI18nEnum {
    NEW,
    DETAIL,
    LIST,
    SINGLE,

    ACTION_SELECT_TASK,
    ACTION_NEW_COLUMN,
    ACTION_RENAME_COLUMN,
    ACTION_DELETE_COLUMN,
    ACTION_HIDE_COLUMN,
    ACTION_HIDE_COLUMNS,
    ACTION_SHOW_COLUMN,
    ACTION_SHOW_COLUMNS,
    ACTION_CHANGE_COLOR,

    FORM_PHASE,
    FORM_TASK_KEY,
    FORM_DURATION,
    FORM_PRIORITY,
    FORM_PRIORITY_HELP,
    FORM_PERCENTAGE_COMPLETE,
    FORM_LOG_BY,
    FORM_NOTES,
    FORM_SUB_TASKS,
    FORM_SUB_TASKS_HELP,
    FORM_IS_ESTIMATED,
    FORM_IS_ESTIMATED_HELP,
    FORM_ORIGINAL_ESTIMATE,
    FORM_ORIGINAL_ESTIMATE_HELP,
    FORM_REMAIN_ESTIMATE,
    FORM_REMAIN_ESTIMATE_HELP,
    FORM_STATUS_HELP,
    FORM_PARENT_TASK,
    FORM_COLUMN_COLOR,
    FORM_COLUMN_DEFAULT_FOR_NEW_PROJECT,

    DIALOG_ASSIGN_TASK_TITLE,

    WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE,
    WIDGET_UNRESOLVED_BY_PRIORITY_TITLE,
    WIDGET_UNRESOLVED_BY_STATUS_TITLE,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_VIEW_LIST_TITLE,
    M_FORM_READ_TITLE,
    M_VIEW_RELATED_TITLE,

    VAL_ALL_TASKS,
    VAL_ALL_OPEN_TASKS,
    VAL_OVERDUE_TASKS,
    VAL_MY_TASKS,
    VAL_NEW_THIS_WEEK,
    VAL_UPDATE_THIS_WEEK,
    VAL_NEW_LAST_WEEK,
    VAL_UPDATE_LAST_WEEK,

    OPT_INVISIBLE_COLUMN_DESCRIPTION,
    OPT_EDIT_TASK_NAME,

    ERROR_CAN_NOT_DELETE_COLUMN_HAS_TASK,
    ERROR_CAN_NOT_ASSIGN_PARENT_TASK_TO_ITSELF,
    ERROR_THERE_IS_ALREADY_COLUMN_NAME,
    ERROR_COLUMN_NAME_NOT_NULL,
    ERROR_CAN_NOT_EDIT_PARENT_TASK_FIELD
}
