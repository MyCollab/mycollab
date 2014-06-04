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
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum BugI18nEnum {
	BUTTON_REOPEN,
	BUTTON_RESOLVED,
	BUTTON_WONT_FIX,
	
	WONT_FIX_EXPLAIN_REQUIRE_MSG,
	
	BUG_DASHBOARD_TITLE,
	BUG_SEARCH_TITLE,
	RECENT_BUGS_WIDGET_TITLE,
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
	
	START_PROGRESS,
	RESOLVED,
	WONTFIX,
	STOP_PROGRESS,
	REOPEN,
	APPROVE_CLOSE,
	
	LIST_VIEW_TITLE,
	
	FORM_NEW_BUG_TITLE,
	FORM_EDIT_BUG_TITLE,
	FORM_SUMMARY,
	FORM_SEVERITY,
	FORM_RESOLUTION,
	FORM_DUE_DATE,
	FORM_DESCRIPTION,
	FORM_ENVIRONMENT,
	FORM_STATUS,
	FORM_LOG_BY,
	FORM_PRIORITY,
	FORM_CREATED_TIME,
	FORM_COMPONENTS,
	FORM_AFFECTED_VERSIONS,
	FORM_FIXED_VERSIONS,
	FORM_ORIGINAL_ESTIMATE,
	FORM_REMAIN_ESTIMATE,
	FORM_ATTACHMENT,
	FORM_PHASE,
	FORM_COMMENT,
	
	TABLE_EXPORT_BUTTON,
	
	RELATED_BUG_NAME,
	RELATED_BUG_TYPE,
	RELATED_BUG_COMMENT,
	
	DASHBOARD_TAB,
	BUG_TAB,
	COMPONENT_TAB,
	VERSION_TAB,
	
	RELATED_BUGS_TAB,
	TIME_TAB,
	FOLLOWERS_TAB,
	
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT
}
