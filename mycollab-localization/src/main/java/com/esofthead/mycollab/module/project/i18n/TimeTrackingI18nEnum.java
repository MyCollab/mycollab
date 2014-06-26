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

@BaseName("localization/project/timetracking")
@LocaleData({ @Locale("en_US") })
public enum TimeTrackingI18nEnum {
	BUTTON_LOG_TIME,
	BUTTON_LINK_TASK,
	BUTTON_DETACH_TASK,
	
	TIME_RECORD_HEADER,
	TASK_LIST_RANGE,
	TASK_LIST_RANGE_WITH_TOTAL_HOUR,
	SEARCH_TIME_TITLE,
	DISPLAY_TIME_LOGGING_SUMMARY_WITHOUT_TYPE,
	DISPLAY_TIME_LOGGING_SUMMARY_WITH_TYPE,
	
	LOG_USER,
	LOG_FOR_DATE,
	LOG_VALUE,
	
	FORM_SUMMARY,
	FORM_IS_BILLABLE,
	FORM_PROJECT,
	FORM_WEEK,
	FORM_WHO,
	
	MONDAY_FIELD,
	TUESDAY_FIELD,
	WEDNESDAY_FIELD,
	THURSDAY_FIELD,
	FRIDAY_FIELD,
	SATURDAY_FIELD,
	SUNDAY_FIELD
}
