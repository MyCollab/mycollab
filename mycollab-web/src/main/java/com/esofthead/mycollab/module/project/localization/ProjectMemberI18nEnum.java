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
package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/people")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum ProjectMemberI18nEnum {
	LIST_VIEW_TITLE,
	READ_VIEW_TITLE,
	
	FORM_INVITE_MEMBERS,
	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_USER,
	FORM_ROLE,
	FORM_MESSAGE,
	FORM_INVITEES_EMAIL,
	FORM_INFORMATION_SECTION,
	NEW_INVITEE_ACTION,
	NEW_USER_ACTION,
	NEW_ROLE_ACTION,
	RESEND_INVITATION_ACTION,
	CAN_NOT_DELETE_ROLE_MESSAGE,
	WAITING_ACCEPT_INVITATION,
	SENDING_EMAIL_INVITATION,
	USER_TOKEN_INVITE_HINT,
	EMPTY_EMAILS_OF_USERS_TO_INVITE_ERRPR_MESSAGE
}
