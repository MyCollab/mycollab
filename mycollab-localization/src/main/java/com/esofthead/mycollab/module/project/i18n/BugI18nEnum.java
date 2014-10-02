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

@BaseName("localization/project-bug")
@LocaleData(value = { @Locale("en-US"), @Locale("ja-JP") }, defaultCharset = "UTF-8")
public enum BugI18nEnum {
	BUTTON_RESOLVED,
	BUTTON_WONT_FIX,
	BUTTON_START_PROGRESS,
	BUTTON_WONTFIX,
	BUTTON_STOP_PROGRESS,
	BUTTON_APPROVE_CLOSE,
	BUTTON_NEW_BUG,
	BUTTON_NEW_COMPONENT,
	BUTTON_NEW_VERSION,

	ERROR_WONT_FIX_EXPLAIN_REQUIRE_MSG,

	WIDGET_RECENT_BUGS_TITLE,
	WIDGET_MY_BUGS_TITLE,
	WIDGET_MY_OPEN_BUGS_TITLE,
	WIDGET_DUE_BUGS_TITLE,
	WIDGET_UPDATED_RECENTLY_TITLE,
	WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE,
	WIDGET_UNRESOLVED_BY_PRIORITY_TITLE,
	WIDGET_CHARTS_TITLE,
	WIDGET_CHART_RESOLUTION_TITLE,
	WIDGET_CHART_STATUS_TITLE,
	WIDGET_CHART_PRIORIY_TITLE,

	VIEW_BUG_DASHBOARD_TITLE,
	VIEW_READ_TITLE,
	VIEW_LIST_TITLE,
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,

	FORM_READ_TITLE,
	FORM_NEW_BUG_TITLE,
	FORM_EDIT_BUG_TITLE,
	FORM_SUMMARY,
	FORM_SEVERITY,
	FORM_RESOLUTION,
	FORM_DUE_DATE,
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
	FORM_RESOLVED_DATE,
	FORM_ANY_TEXT,

	RELATED_BUG_NAME,
	RELATED_BUG_TYPE,
	RELATED_BUG_COMMENT,

	TAB_DASHBOARD,
	TAB_BUG,
	TAB_COMPONENT,
	TAB_VERSION,
	TAB_RELATED_BUGS,
	TAB_TIME,
	TAB_FOLLOWERS,

	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT,

	MAIL_CREATE_ITEM_HEADING,
	MAIL_UPDATE_ITEM_HEADING,
	MAIL_COMMENT_ITEM_HEADING,

	OPT_UNDEFINED_USER
}
