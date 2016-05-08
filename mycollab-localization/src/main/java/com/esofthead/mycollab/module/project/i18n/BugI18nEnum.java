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
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum BugI18nEnum {
    BUTTON_RESOLVED,
    BUTTON_WONT_FIX,
    BUTTON_START_PROGRESS,
    BUTTON_WONTFIX,
    BUTTON_STOP_PROGRESS,
    BUTTON_APPROVE_CLOSE,

    ERROR_WONT_FIX_EXPLAIN_REQUIRE_MSG,

    WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE,
    WIDGET_UNRESOLVED_BY_PRIORITY_TITLE,
    WIDGET_UNRESOLVED_BY_STATUS_TITLE,

    NEW,
    DETAIL,
    LIST,

    FORM_SUMMARY,
    FORM_SEVERITY,
    FORM_RESOLUTION,
    FORM_RESOLUTION_HELP,
    FORM_BUG_KEY,
    FORM_ENVIRONMENT,
    FORM_STATUS_HELP,
    FORM_LOG_BY,
    FORM_PRIORITY,
    FORM_COMPONENTS,
    FORM_COMPONENTS_HELP,
    FORM_AFFECTED_VERSIONS,
    FORM_AFFECTED_VERSIONS_HELP,
    FORM_FIXED_VERSIONS,
    FORM_FIXED_VERSIONS_HELP,
    FORM_ORIGINAL_ESTIMATE,
    FORM_ORIGINAL_ESTIMATE_HELP,
    FORM_REMAIN_ESTIMATE,
    FORM_REMAIN_ESTIMATE_HELP,
    FORM_PHASE,
    FORM_COMMENT,
    FORM_RESOLVED_DATE,
    FORM_ANY_TEXT,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,

    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_FORM_READ_TITLE
}
