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
package com.mycollab.module.crm.fielddef

import com.mycollab.common.GridFieldMeta
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.AccountI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object AccountTableFieldDef {
    @JvmField
    val selected = GridFieldMeta(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH)

    @JvmField
    val action = GridFieldMeta(null, "id", WebUIConstants.TABLE_ACTION_CONTROL_WIDTH)

    @JvmField
    val accountname = GridFieldMeta(AccountI18nEnum.FORM_ACCOUNT_NAME, "accountname", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val city = GridFieldMeta(AccountI18nEnum.FORM_BILLING_CITY, "city", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val phoneoffice = GridFieldMeta(AccountI18nEnum.FORM_OFFICE_PHONE, "phoneoffice", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val email = GridFieldMeta(GenericI18Enum.FORM_EMAIL, "email", WebUIConstants.TABLE_EMAIL_WIDTH)

    @JvmField
    val assignUser = GridFieldMeta(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val website = GridFieldMeta(AccountI18nEnum.FORM_WEBSITE, "website", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val type = GridFieldMeta(GenericI18Enum.FORM_TYPE, "type", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val ownership = GridFieldMeta(AccountI18nEnum.FORM_OWNERSHIP, "ownership", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val fax = GridFieldMeta(AccountI18nEnum.FORM_FAX, "fax", WebUIConstants.TABLE_M_LABEL_WIDTH)
}