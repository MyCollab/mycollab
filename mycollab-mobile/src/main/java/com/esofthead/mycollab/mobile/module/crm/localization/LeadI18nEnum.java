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

/**
 * 
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */

@BaseName("localization.common/crm/lead")
@LocaleData({ @Locale("en_US") })
public enum LeadI18nEnum {
	FORM_NAME, FORM_TITLE, FORM_DEPARTMENT, FORM_ACCOUNT_NAME, FORM_LEAD_SOURCE, FORM_INDUSTRY, FORM_EMAIL, FORM_MOBILE, FORM_FAX, FORM_STATUS, FORM_WEBSITE, CONVERT_FROM_LEAD_TITLE
}
