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
package com.esofthead.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/crm/opportunity")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum OpportunityI18nEnum {
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,
	
	VIEW_LIST_TITLE,
	VIEW_NEW_TITLE,
	
	SECTION_OPPORTUNITY_INFORMATION,
	SECTION_DESCRIPTION,
	
	BUTTON_NEW_OPPORTUNITY,
	
	FORM_AMOUNT,
	FORM_SALE_STAGE,
	FORM_EXPECTED_CLOSE_DATE,
	FORM_NAME,
	FORM_CURRENCY,
	FORM_PROBABILITY,
	FORM_ACCOUNT_NAME,
	FORM_SOURCE,
	FORM_TYPE,
	FORM_LEAD_SOURCE,
	FORM_CAMPAIGN_NAME,
	FORM_NEXT_STEP,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT,
	MAIL_CREATE_ITEM_HEADING,
	MAIL_UPDATE_ITEM_HEADING,
	MAIL_COMMENT_ITEM_HEADING
}
