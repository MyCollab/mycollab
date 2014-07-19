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

@BaseName("localization/crm/assignment")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum TaskI18nEnum {
	VIEW_NEW_TITLE,
	BUTTON_NEW_TASK,
	
	FORM_SUBJECT,
	FORM_START_DATE,
	FORM_STATUS,
	FORM_DUE_DATE,
	FORM_CONTACT,
	FORM_PRIORITY,
	TABLE_TYPE_HEADER,
	TABLE_END_DATE_HEADER,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT,
	MAIL_CREATE_ITEM_HEADING,
	MAIL_UPDATE_ITEM_HEADING,
	MAIL_COMMENT_ITEM_HEADING
}
