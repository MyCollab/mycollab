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

@BaseName("localization/project-people")
@LocaleData(value = {@Locale("en-US"), @Locale("ja-JP")}, defaultCharset = "UTF-8")
public enum ProjectMemberI18nEnum {
    LIST,
    DETAIL,
    NEW,
    FORM_INVITE_MEMBERS,

    FORM_USER,
    FORM_ROLE,
    FORM_MESSAGE,
    FORM_INVITEES_EMAIL,
    FORM_INFORMATION_SECTION,

    BUTTON_NEW_INVITEE,
    BUTTON_NEW_INVITEES,
    BUTTON_RESEND_INVITATION,

    CAN_NOT_DELETE_ROLE_MESSAGE,
    WAITING_ACCEPT_INVITATION,
    SENDING_EMAIL_INVITATION,
    USER_TOKEN_INVITE_HINT,
    ERROR_EMPTY_EMAILS_OF_USERS_TO_INVITE_MESSAGE,
    MSG_DEFAULT_INVITATION_COMMENT,

    MAIL_INVITE_USERS_SUBJECT,

    M_VIEW_MEMBER_LOOKUP,
    M_FORM_EMAIL,
    M_FORM_PROJECT_ADMIN
}
