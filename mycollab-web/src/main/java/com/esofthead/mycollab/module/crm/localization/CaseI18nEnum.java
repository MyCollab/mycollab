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

@BaseName("localization/crm/case")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum CaseI18nEnum {
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT,
	
	LIST_VIEW_TITLE,
	
	SECTION_CASE_INFORMATION,
	SECTION_DESCRIPTION,
	
	FORM_NEW_TITLE,
	
	FORM_PRIORITY,
	FORM_STATUS,
	FORM_ACCOUNT,
	FORM_ORIGIN,
	FORM_PHONE,
	FORM_TYPE,
	FORM_REASON,
	FORM_SUBJECT,
	FORM_EMAIL,
	FORM_CREATED_TIME,
	FORM_LAST_UPDATED_TIME,
	FORM_DESCRIPTION,
	FORM_RESOLUTION
}
