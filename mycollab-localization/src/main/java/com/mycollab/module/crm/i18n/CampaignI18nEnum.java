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
package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("crm-campaign")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum CampaignI18nEnum {
    LIST,
    NEW,
    SINGLE,

    SECTION_CAMPAIGN_INFORMATION,
    SECTION_GOAL,
    SECTION_DESCRIPTION,

    FORM_EXPECTED_REVENUE,
    FORM_EXPECTED_COST,
    FORM_BUDGET,
    FORM_ACTUAL_COST,

    MAIL_CREATE_ITEM_SUBJECT,
    MAIL_UPDATE_ITEM_SUBJECT,
    MAIL_COMMENT_ITEM_SUBJECT,
    MAIL_CREATE_ITEM_HEADING,
    MAIL_UPDATE_ITEM_HEADING,
    MAIL_COMMENT_ITEM_HEADING,

    M_TITLE_SELECT_CAMPAIGNS,
    M_VIEW_CAMPAIGN_NAME_LOOKUP,
    M_TITLE_RELATED_CAMPAIGNS
}
