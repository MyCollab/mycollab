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

@BaseName("project-common")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ProjectCommonI18nEnum {
    WIDGET_ACTIVE_PROJECTS_TITLE,
    WIDGET_ARCHIVE_PROJECTS_TITLE,
    WIDGET_ALL_PROJECTS_TITLE,
    WIDGET_MEMBERS_TITLE,

    FEED_PROJECT_MESSAGE_TITLE,
    FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
    FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
    FEED_USER_ACTIVITY_DELETE_ACTION_TITLE,
    FEED_USER_ACTIVITY_COMMENT_ACTION_TITLE,
    FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
    FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,
    FEED_PROJECT_USER_ACTIVITY_DELETE_ACTION_TITLE,
    FEED_PROJECT_USER_ACTIVITY_COMMENT_ACTION_TITLE,

    BUTTON_SWITCH_PROJECT,
    BUTTON_DELETE_PROJECT,
    BUTTON_ACTIVE_PROJECT,
    BUTTON_ARCHIVE_PROJECT,
    BUTTON_ACTIVE_PROJECTS,
    BUTTON_ARCHIVE_PROJECTS,
    BUTTON_ALL_PROJECTS,
    ACTION_EDIT_NOTIFICATION,

    DIALOG_CONFIRM_PROJECT_DELETE_MESSAGE,
    DIALOG_CONFIRM_PROJECT_ARCHIVE_MESSAGE,

    VIEW_CLIENTS,
    VIEW_REPORTS,
    VIEW_DASHBOARD,
    VIEW_ROADMAP,
    VIEW_TAG,
    VIEW_FILE,
    VIEW_FINANCE,
    VIEW_TIME,
    VIEW_STANDUP,
    VIEW_CALENDAR,
    VIEW_MEMBER,
    VIEW_SETTINGS,
    VIEW_FAVORITES,
    VIEW_GANTT_CHART,

    SUB_INFO_PEOPLE,
    ITEM_CREATED_DATE,
    ITEM_UPDATED_DATE,
    SUB_INFO_DATES,
    ITEM_CREATED_PEOPLE,
    ITEM_ASSIGN_PEOPLE,

    OPT_ASSIGNMENT_LIST,
    OPT_ASSIGNMENT_VALUE,
    OPT_RESOLVING_TREND_IN_DURATION,
    OPT_OPEN_TASKS,
    OPT_OPEN_BUGS,
    OPT_DUE_IN,
    OPT_LIST,
    OPT_KANBAN,
    OPT_BOARD,
    OPT_ASSIGN_TO_ME_VALUE,
    OPT_ASSIGN_TO_OTHERS,
    OPT_UNASSIGNED,
    OPT_OVERDUE_ASSIGNMENTS_VALUE,
    OPT_CLOSE_SUB_ASSIGNMENTS,

    OPT_MARK_COMPLETE,
    OPT_MARK_INCOMPLETE,

    ERROR_NOT_EDIT_CELL_IN_GANTT_HELP,

    M_VIEW_PROJECT_ACTIVITIES;
}
