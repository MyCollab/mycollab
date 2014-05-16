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

@BaseName("localization/project/bug")
@LocaleData({ @Locale("en_US") })
public enum BugI18nEnum {
	BUG_DASHBOARD_TITLE,
	BUG_SEARCH_TITLE,
	MY_BUGS_WIDGET_TITLE,
	MY_OPEN_BUGS_WIDGET_TITLE,
	DUE_BUGS_WIDGET_TITLE,
	UPDATED_RECENTLY_WIDGET_TITLE,
	UNRESOLVED_BY_ASSIGNEE_WIDGET_TITLE,
	UNRESOLVED_BY_PRIORITY_WIDGET_TITLE,
	CHARTS_WIDGET_TITLE,
	NEW_BUG_ACTION,
	NEW_COMPONENT_ACTION,
	NEW_VERSION_ACTION,
	CHART_RESOLUTION_TITLE,
	CHART_STATUS_TITLE,
	CHART_PRIORIY_TITLE,
	CHART_TREND_TITLE,
	FORM_SUMMARY,
	FORM_ASSIGN_USER,
	FORM_SEVERITY,
	FORM_RESOLUTION,
	FORM_DUE_DATE,
	FORM_DESCRIPTION,
	FORM_ENVIRONMENT,
	FORM_STATUS,
	FORM_LOG_BY,
	FORM_PRIORITY,
	FORM_CREATED_TIME,
	TABLE_EXPORT_BUTTON
}
