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

@BaseName("localization/crm_contact")
@LocaleData(value = { @Locale("en_US"), @Locale("ja_JP") }, defaultCharset = "UTF-8")
public enum ContactI18nEnum {
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,
	VIEW_LIST_TITLE,
	VIEW_NEW_TITLE,

	SECTION_INFORMATION,
	SECTION_ADDRESS,
	SECTION_DESCRIPTION,

	BUTTON_NEW_CONTACT,

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
	FORM_OFFICE_PHONE,
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
	FORM_ANY_PHONE,
	FORM_ANY_EMAIL,
	FORM_ANY_CITY,

	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT,
	MAIL_CREATE_ITEM_HEADING,
	MAIL_UPDATE_ITEM_HEADING,
	MAIL_COMMENT_ITEM_HEADING,

	M_TITLE_SELECT_CONTACTS,
	M_VIEW_CONTACT_NAME_LOOKUP,
	M_TITLE_RELATED_CONTACTS
}
