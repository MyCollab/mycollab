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
package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("crm-opportunity")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum OpportunityI18nEnum {
    LIST,
    NEW,
    SINGLE,

    SECTION_OPPORTUNITY_INFORMATION,
    SECTION_DESCRIPTION,

    FORM_AMOUNT,
    FORM_SALE_STAGE,
    FORM_EXPECTED_CLOSE_DATE,
    FORM_PROBABILITY,
    FORM_ACCOUNT_NAME,
    FORM_SOURCE,
    FORM_LEAD_SOURCE,
    FORM_CAMPAIGN_NAME,
    FORM_NEXT_STEP,

    OPT_SALES_DASHBOARD,
    OPT_LEAD_SOURCES,
    OPT_SALES_STAGE,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_TITLE_SELECT_OPPORTUNITIES,
    M_VIEW_OPPORTUNITY_NAME_LOOKUP,
    M_TITLE_RELATED_OPPORTUNITIES
}
