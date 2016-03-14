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
package com.esofthead.mycollab.module.crm.view.cases

import com.esofthead.mycollab.common.TableViewField
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum
import com.esofthead.mycollab.vaadin.web.ui.UIConstants

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object CaseTableFieldDef {
  val selected = new TableViewField(null, "selected", UIConstants.TABLE_CONTROL_WIDTH)
  val action = new TableViewField(null, "id", -1)
  val priority = new TableViewField(CaseI18nEnum.FORM_PRIORITY, "priority", UIConstants.TABLE_S_LABEL_WIDTH)
  val status = new TableViewField(CaseI18nEnum.FORM_STATUS, "status", UIConstants.TABLE_M_LABEL_WIDTH)
  val account = new TableViewField(CaseI18nEnum.FORM_ACCOUNT, "accountName", UIConstants.TABLE_X_LABEL_WIDTH)
  val origin = new TableViewField(CaseI18nEnum.FORM_ORIGIN, "origin", UIConstants.TABLE_M_LABEL_WIDTH)
  val phone = new TableViewField(CaseI18nEnum.FORM_PHONE, "phonenumber", UIConstants.TABLE_M_LABEL_WIDTH)
  val `type` = new TableViewField(CaseI18nEnum.FORM_TYPE, "type", UIConstants.TABLE_M_LABEL_WIDTH)
  val reason = new TableViewField(CaseI18nEnum.FORM_REASON, "reason", UIConstants.TABLE_EX_LABEL_WIDTH)
  val subject = new TableViewField(CaseI18nEnum.FORM_SUBJECT, "subject", UIConstants.TABLE_EX_LABEL_WIDTH)
  val email = new TableViewField(CaseI18nEnum.FORM_EMAIL, "email", UIConstants.TABLE_EMAIL_WIDTH)
  val assignUser = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
  val createdTime = new TableViewField(GenericI18Enum.FORM_CREATED_TIME, "createdtime", UIConstants.TABLE_DATE_TIME_WIDTH)
  val lastUpdatedTime = new TableViewField(GenericI18Enum.FORM_LAST_UPDATED_TIME, "lastupdatedtime", UIConstants.TABLE_DATE_TIME_WIDTH)
}
