/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("crm-contact")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ContactI18nEnum {
    LIST,
    NEW,
    SINGLE,

    SECTION_INFORMATION,
    SECTION_ADDRESS,
    SECTION_DESCRIPTION,

    FORM_DECISION_ROLE,
    FORM_ACCOUNTS,
    FORM_TITLE,
    FORM_DEPARTMENT,
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

    ACTION_ADD_CONTACT_ROLES,
    OPT_ADD_EDIT_CONTACT_ROLES,

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
