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

@BaseName("localization/crm/contact")
@LocaleData({ @Locale("en_US") })
public enum ContactI18nEnum {
	FORM_NEW_TITLE,
	
	FORM_INFO_SECTION,
	FORM_ADDRESS_SECTION,
	FORM_DESC_SECTION,
	
	FORM_DECISION_ROLE,
	FORM_FIRSTNAME,
	FORM_LASTNAME,
	FORM_NAME,
	FORM_ACCOUNTS,
	FORM_TITLE,
	FORM_DEPARTMENT,
	FORM_EMAIL,
	FORM_ASSISTANT,
	FORM_ASSISTANT_PHONE,
	FORM_MOBILE,
	FORM_HOME_PHONE,
	FORM_FAX,
	FORM_BIRTHDAY,
	FORM_IS_CALLABLE,
	FORM_OTHER_PHONE,
	FORM_LEAD_SOURCE, 
	FORM_PRIMARY_ADDRESS,
	FORM_PRIMARY_CITY,
	FORM_PRIMARY_STATE,
	FORM_PRIMARY_POSTAL_CODE,
	FORM_PRIMARY_COUNTRY,
	FORM_OTHER_ADDRESS,
	FORM_OTHER_CITY,
	FORM_OTHER_STATE,
	FORM_OTHER_POSTAL_CODE,
	FORM_OTHER_COUNTRY,
	FORM_DESCRIPTION
}
