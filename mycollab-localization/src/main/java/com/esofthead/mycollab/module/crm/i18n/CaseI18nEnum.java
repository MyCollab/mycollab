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

@BaseName("localization/crm_case")
@LocaleData(value = { @Locale("en_US"), @Locale("ja_JP") }, defaultCharset = "UTF-8")
public enum CaseI18nEnum {
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,
	VIEW_LIST_TITLE,
	VIEW_NEW_TITLE,

	SECTION_CASE_INFORMATION,
	SECTION_DESCRIPTION,

	BUTTON_NEW_CASE,

	FORM_PRIORITY,
	FORM_STATUS,
	FORM_ACCOUNT,
	FORM_ORIGIN,
	FORM_PHONE,
	FORM_TYPE,
	FORM_REASON,
	FORM_SUBJECT,
	FORM_EMAIL,
	FORM_RESOLUTION,

	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT,
	MAIL_CREATE_ITEM_HEADING,
	MAIL_UPDATE_ITEM_HEADING,
	MAIL_COMMENT_ITEM_HEADING,

	M_TITLE_SELECT_CASES,
	M_VIEW_CASE_NAME_LOOKUP,
	M_TITLE_RELATED_CASES
}
