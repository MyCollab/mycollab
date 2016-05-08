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

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
@BaseName("localization/project-invoice")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum InvoiceI18nEnum {
    BUTTON_NEW_INVOICE,
    FORM_NOID_FIELD,
    FORM_NOID_FIELD_HELP,
    FORM_CLIENT_FIELD,
    FORM_ISSUE_DATE_FIELD,
    FORM_STATUS_HELP,
    FORM_TYPE,
    FORM_TYPE_HELP,
    FORM_CONTACT_PERSON,
    FORM_AMOUNT,
    FORM_NOTE,
    FIX_PRICE,
    TIME_MATERIAL
}
