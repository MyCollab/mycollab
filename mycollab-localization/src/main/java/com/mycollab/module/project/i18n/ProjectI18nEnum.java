/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ProjectI18nEnum {
    NEW,
    EDIT,
    LIST,
    SINGLE,

    FORM_HOME_PAGE,
    FORM_ACCOUNT_NAME,
    FORM_ACCOUNT_NAME_HELP,
    FORM_SHORT_NAME,
    FORM_SHORT_NAME_HELP,
    FORM_BILLING_RATE,
    FORM_BILLING_RATE_HELP,
    FORM_OVERTIME_BILLING_RATE,
    FORM_OVERTIME_BILLING_RATE_HELP,
    FORM_CURRENCY_HELP,
    FORM_TARGET_BUDGET,
    FORM_TARGET_BUDGET_HELP,
    FORM_ACTUAL_BUDGET,
    FORM_ACTUAL_BUDGET_HELP,
    FORM_LEADER,
    FORM_TEMPLATE,

    ACTION_ADD_MEMBERS,
    ACTION_BROWSE,
    ACTION_MARK_TEMPLATE,
    ACTION_UNMARK_TEMPLATE,
    ACTION_CHANGE_LOGO,
    ACTION_VIEW_TICKETS,
    ACTION_HIDE_TICKETS,

    SECTION_PROJECT_INFO,
    SECTION_FINANCE_SCHEDULE,
    SECTION_DESCRIPTION,

    OPT_CREATE_PROJECT_FROM_TEMPLATE,
    OPT_MARK_TEMPLATE_HELP,
    OPT_TO_ADD_PROJECT,
    OPT_ASK_TO_ADD_MEMBERS,
    OPT_PROJECT_TICKET,
    OPT_NO_TICKET,
    OPT_NO_OVERDUE_TICKET,
    OPT_UNRESOLVED_TICKET_THIS_WEEK,
    OPT_UNRESOLVED_TICKET_NEXT_WEEK,
    OPT_SEARCH_TERM,
    OPT_GENERAL,
    OPT_FEATURES,
    OPT_CLIENT_AND_BILLING,

    ERROR_MUST_CHOOSE_TEMPLATE_PROJECT,
    ERROR_PROJECT_KEY_INVALID
}
