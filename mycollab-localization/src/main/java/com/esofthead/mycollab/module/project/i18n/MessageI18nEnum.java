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
package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/message")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum MessageI18nEnum {
	VIEW_LIST_TITLE,
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,
	
	WIDGET_LASTEST_NEWS,
	
	BUTTON_NEW_MESSAGE,
	
	FORM_TITLE,
	FORM_IS_STICK,
	FORM_ATTACHMENT_FIELD,
	FORM_TITLE_REQUIRED_ERROR,
	
	USER_COMMENT_ADD,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT,
	MAIL_CREATE_ITEM_HEADING,
	MAIL_UPDATE_ITEM_HEADING,
	MAIL_COMMENT_ITEM_HEADING,
	
	OPT_FROM_TIME_ENTRY
}
