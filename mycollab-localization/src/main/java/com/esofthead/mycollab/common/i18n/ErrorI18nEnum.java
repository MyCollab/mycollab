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
package com.esofthead.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-webexception")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ErrorI18nEnum {
    SUB_DOMAIN_IS_NOT_EXISTED,
    NOT_SUPPORT_SENDING_EMAIL_TO_ALL_USERS,
    ERROR_USER_IS_NOT_EXISTED,
    EXISTING_DOMAIN_REGISTER_ERROR,
    FIELD_MUST_NOT_NULL,
    NO_ACCESS_PERMISSION
}
