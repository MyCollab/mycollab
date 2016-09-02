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
package com.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("account-role")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum RoleI18nEnum {
    SINGLE,
    LIST,
    NEW,
    DETAIL,

    SECTION_INFORMATION,
    FORM_PERMISSION_HEADER,
    SECTION_PROJECT_MANAGEMENT_TITLE,
    SECTION_CRM_TITLE,
    SECTION_DOCUMENT_TITLE,
    SECTION_ACCOUNT_MANAGEMENT_TITLE,

    OPT_ACCOUNT_OWNER,
    OPT_BILLING_MANAGEMENT,
    OPT_THEME,
    OPT_CREATE_NEW_PROJECT,
    OPT_GLOBAL_PROJECT_SETTINGS,
    OPT_PUBLIC_DOCUMENTS,

    ERROR_CAN_NOT_DELETE_SYSTEM_ROLE,
    ERROR_ONLY_OWNER_CAN_ASSIGN_OWNER_ROLE
}
