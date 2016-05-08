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

@BaseName("localization/project-task")
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum TaskI18nEnum {
    NEW,
    DETAIL,
    LIST,

    FORM_PHASE,
    FORM_COMMENT,
    FORM_NOTES_ATTACHMENT,
    FORM_TASK_KEY,
    FORM_ACTUAL_START_DATE,
    FORM_ACTUAL_END_DATE,
    FORM_DURATION,
    FORM_PRIORITY,
    FORM_PRIORITY_HELP,
    FORM_PERCENTAGE_COMPLETE,
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
    M_VIEW_RELATED_TITLE
}
