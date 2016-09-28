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

@BaseName("project-timetracking")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum TimeTrackingI18nEnum {
    BUTTON_LOG_TIME,

    ACTION_LINK_TICKET,
    ACTION_UNLINK_TICKET,

    DIALOG_LOG_TIME_ENTRY_TITLE,

    TASK_LIST_RANGE,
    TASK_LIST_RANGE_WITH_TOTAL_HOUR,
    SEARCH_TIME_TITLE,

    LOG_FOR_DATE,
    LOG_VALUE,

    FORM_SUMMARY,
    FORM_IS_BILLABLE,
    FORM_IS_OVERTIME,
    FORM_WEEK,
    FORM_WHO,

    MONDAY_FIELD,
    TUESDAY_FIELD,
    WEDNESDAY_FIELD,
    THURSDAY_FIELD,
    FRIDAY_FIELD,
    SATURDAY_FIELD,
    SUNDAY_FIELD,

    OPT_TIMESHEET,
    TIME_EXPLAIN_HELP,
    SUB_INFO_TIME,
    OPT_BILLABLE_HOURS,
    OPT_BILLABLE_HOURS_VALUE,
    OPT_NON_BILLABLE_HOURS,
    OPT_NON_BILLABLE_HOURS_VALUE,
    OPT_REMAIN_HOURS,
    OPT_TOTAL_HOURS_VALUE,
    OPT_TOTAL_SPENT_HOURS,
    OPT_REMAINING_WORK_HOURS,
    OPT_COST,
    OPT_TIME_FORMAT,

    M_FORM_SPENT_HOURS,
    M_FORM_IS_BILLABLE,
    M_DIALOG_UPDATE_REMAIN_HOURS,
    M_DIALOG_ADD_TIME_LOG_ENTRY,

    ERROR_MEMBER_NOT_NULL,
    ERROR_NOT_INVOLVED_ANY_PROJECT,
    ERROR_TIME_FORMAT
}
