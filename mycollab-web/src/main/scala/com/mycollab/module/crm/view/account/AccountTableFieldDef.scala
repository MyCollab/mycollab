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
package com.mycollab.module.crm.view.account

import com.mycollab.vaadin.web.ui.UIConstants
import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.AccountI18nEnum

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object AccountTableFieldDef {
  val selected = new TableViewField(null, "selected", UIConstants.TABLE_CONTROL_WIDTH)
  val action = new TableViewField(null, "id", UIConstants.TABLE_ACTION_CONTROL_WIDTH)
  val accountname = new TableViewField(AccountI18nEnum.FORM_ACCOUNT_NAME, "accountname", UIConstants.TABLE_X_LABEL_WIDTH)
  val city = new TableViewField(AccountI18nEnum.FORM_BILLING_CITY, "city", UIConstants.TABLE_X_LABEL_WIDTH)
  val phoneoffice = new TableViewField(AccountI18nEnum.FORM_OFFICE_PHONE, "phoneoffice", UIConstants.TABLE_M_LABEL_WIDTH)
  val email = new TableViewField(AccountI18nEnum.FORM_EMAIL, "email", UIConstants.TABLE_EMAIL_WIDTH)
  val assignUser = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
  val website = new TableViewField(AccountI18nEnum.FORM_WEBSITE, "website", UIConstants.TABLE_X_LABEL_WIDTH)
  val `type` = new TableViewField(GenericI18Enum.FORM_TYPE, "type", UIConstants.TABLE_X_LABEL_WIDTH)
  val ownership = new TableViewField(AccountI18nEnum.FORM_OWNERSHIP, "ownership", UIConstants.TABLE_X_LABEL_WIDTH)
  val fax = new TableViewField(AccountI18nEnum.FORM_FAX, "fax", UIConstants.TABLE_M_LABEL_WIDTH)
}
