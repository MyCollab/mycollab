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

@BaseName("localization/crm-common")
@LocaleData(value = { @Locale("en-US"), @Locale("ja-JP") }, defaultCharset = "UTF-8")
public enum CrmCommonI18nEnum {
	WIDGET_ACTIVITY_CREATE_ACTION,
	WIDGET_ACTIVITY_UPDATE_ACTION,
	WIDGET_ACTIVITY_COMMENT_ACTION,

	DIALOG_CHANGE_LOG_TITLE,
	DIALOG_DELETE_RELATIONSHIP_TITLE,

	TOOLBAR_DASHBOARD_HEADER,
	TOOLBAR_CRMNOTIFICATION_HEADER,
	TOOLBAR_ACCOUNTS_HEADER,
	TOOLBAR_CASES_HEADER,
	TOOLBAR_CONTACTS_HEADER,
	TOOLBAR_CAMPAIGNS_HEADER,
	TOOLBAR_LEADS_HEADER,
	TOOLBAR_OPPORTUNTIES_HEADER,
	TOOLBAR_ACTIVITIES_HEADER,
	TOOLBAR_DOCUMENT_HEADER,

	SUB_INFO_DATES,
	ITEM_CREATED_DATE,
	ITEM_UPDATED_DATE,
	SUB_INFO_PEOPLE,
	ITEM_CREATED_PEOPLE,
	ITEM_ASSIGN_PEOPLE,

	BUTTON_CREATE,
	BUTTON_NEW_NOTE,
	BUTTON_REPLY,

	ACCOUNT,
	CONTACT,
	CAMPAIGN,
	LEAD,
	OPPORTUNITY,
	CASE,

	TAB_ABOUT,
	TAB_ACCOUNT,
	TAB_ACTIVITY,
	TAB_CAMPAIGN,
	TAB_CASE,
	TAB_CONTACT,
	TAB_LEAD,
	TAB_OPPORTUNITY
}
