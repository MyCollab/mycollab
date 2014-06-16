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
package com.esofthead.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/accountsettings/user")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum UserI18nEnum {
	CAN_NOT_DELETE_ROLE_MESSAGE,
	PASSWORDS_ARE_NOT_MATCH,
	CHANGE_PASSWORD_WINDOW_TITLE,
	PASSWORD_INSTRUCT_LABEL_1,
	PASSWORD_INSTRUCT_LABEL_2,
	CHANGE_CONTACT_INFO_WINDOW_TITLE,
	BUTTON_CHANGE_PASSWORD,
	CANCEL_ACCOUNT_FIRST_LINE,
	CANCEL_ACCOUNT_MESSAGE,
	CANCEL_ACCOUNT_NOTE,
	CANCEL_ACCOUNT_CONFIRM_NOTE,
	CHANGE_AVATAR_ACTION,
	
	SECTION_BASIC_INFORMATION,
	SECTION_CONTACT_INFORMATION,
	SECTION_ADVANCED_INFORMATION,

	PROFILE_VIEW,
	BILLING_VIEW,
	CUSTOMIZE_VIEW,
	USERS_VIEW,
	DETAIL_USER_VIEW,
	
	FORM_FIRST_NAME,
	FORM_LAST_NAME,
	FORM_NICK_NAME,
	FORM_BIRTHDAY,
	FORM_LANGUAGE,
	FORM_EMAIL,
	FORM_TIMEZONE,
	FORM_ROLE,
	FORM_COMPANY,
	FORM_COUNTRY,
	FORM_WEBSITE,
	FORM_HOME_PHONE,
	FORM_WORK_PHONE
}
