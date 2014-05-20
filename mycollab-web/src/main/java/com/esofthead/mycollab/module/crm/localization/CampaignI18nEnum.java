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
package com.esofthead.mycollab.module.crm.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/campaign")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum CampaignI18nEnum {
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT,
	
	LIST_VIEW_TITLE,
	
	SECTION_CAMPAIGN_INFORMATION,
	SECTION_GOAL,
	SECTION_DESCRIPTION,
	
	FORM_NEW_TITLE,
	
	FORM_CAMPAIGN_NAME,
	FORM_STATUS,
	FORM_START_DATE,
	FORM_END_DATE,
	FORM_TYPE,
	FORM_EXPECTED_REVENUE,
	FORM_EXPECTED_COST,
	FORM_BUDGET,
	FORM_ACTUAL_COST,
	FORM_CURRENCY,
	FORM_DESCRIPTION
}
