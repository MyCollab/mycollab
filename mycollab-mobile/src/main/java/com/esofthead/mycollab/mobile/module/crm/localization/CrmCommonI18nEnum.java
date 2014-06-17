/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("mobile/localization.common/crm/common")
@LocaleData({ @Locale("en_US") })
public enum CrmCommonI18nEnum {
	TABLE_OFFICE_PHONE_HEADER,
	TABLE_CREATED_DATE_HEADER,
	TABLE_ASSIGNED_USER_HEADER,
	TABLE_NAME_HEADER,
	TABLE_CITY_HEADER,
	TABLE_EMAIL_ADDRESS_HEADER,
	TABLE_ACCOUNT_NAME_HEADER,
	TABLE_ACTION_HEADER,
	TABLE_END_DATE_HEADER,
	TABLE_STATUS_HEADER,
	TABLE_TYPE_HEADER,
	TABLE_EXPECTED_REVENUE_HEADER,
	TABLE_SUBJECT_HEADER,
	TABLE_PRIORITY_HEADER,
	TABLE_TITLE_HEADER,
	TABLE_WEBSITE_HEADER,
	TABLE_SELECTED_ITEM_TITLE,
	WIDGET_ACTIVITY_CREATE_ACTION,
	WIDGET_ACTIVITY_UPDATE_ACTION,
	DIALOG_DELETE_RELATIONSHIP_TITLE,
	TOOLBAR_CRMNOTIFICATION_HEADER,
	TOOLBAR_ACCOUNTS_HEADER,
	TOOLBAR_CASES_HEADER,
	TOOLBAR_CONTACTS_HEADER,
	TOOLBAR_CAMPAIGNS_HEADER,
	TOOLBAR_LEADS_HEADER,
	TOOLBAR_OPPORTUNTIES_HEADER,
	TOOLBAR_ACTIVITIES_HEADER,
	TOOLBAR_DOCUMENT_HEADER,
	TOOLBAR_ACCOUNT_NEW_ACTION,
	TOOLBAR_CASE_NEW_ACTION,
	TOOLBAR_CONTACT_NEW_ACTION,
	TOOLBAR_CAMPAIGN_NEW_ACTION,
	TOOLBAR_LEAD_NEW_ACTION,
	TOOLBAR_OPPORTUNITY_NEW_ACTION,
	TOOLBAR_CALL_NEW_ACTION,
	TOOLBAR_MEETING_NEW_ACTION,
	TOOLBAR_TASK_NEW_ACTION,
	FORM_PHONE_OFFICE_FIELD
}
