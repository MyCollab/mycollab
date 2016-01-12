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
package com.esofthead.mycollab.module.crm.view.contact

import com.esofthead.mycollab.common.TableViewField
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum
import com.esofthead.mycollab.vaadin.web.ui.UIConstants

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
object ContactTableFieldDef {
    val selected: TableViewField = new TableViewField(null, "selected", UIConstants.TABLE_CONTROL_WIDTH)
    val action: TableViewField = new TableViewField(null, "id", UIConstants.TABLE_ACTION_CONTROL_WIDTH)
    val name: TableViewField = new TableViewField(ContactI18nEnum.FORM_NAME, "contactName", UIConstants.TABLE_X_LABEL_WIDTH)
    val account: TableViewField = new TableViewField(ContactI18nEnum.FORM_ACCOUNTS, "accountName", UIConstants.TABLE_EX_LABEL_WIDTH)
    val dicisionRole: TableViewField = new TableViewField(ContactI18nEnum.FORM_DECISION_ROLE, "decisionRole", UIConstants.TABLE_M_LABEL_WIDTH)
    val title: TableViewField = new TableViewField(ContactI18nEnum.FORM_TITLE, "title", UIConstants.TABLE_S_LABEL_WIDTH)
    val department: TableViewField = new TableViewField(ContactI18nEnum.FORM_DEPARTMENT, "department", UIConstants.TABLE_M_LABEL_WIDTH)
    val email: TableViewField = new TableViewField(ContactI18nEnum.FORM_EMAIL, "email", UIConstants.TABLE_EMAIL_WIDTH)
    val assistant: TableViewField = new TableViewField(ContactI18nEnum.FORM_ASSISTANT, "assistant", UIConstants.TABLE_X_LABEL_WIDTH)
    val assistantPhone: TableViewField = new TableViewField(ContactI18nEnum.FORM_ASSISTANT_PHONE, "assistantphone", UIConstants.TABLE_X_LABEL_WIDTH)
    val phoneOffice: TableViewField = new TableViewField(ContactI18nEnum.FORM_OFFICE_PHONE, "officephone", UIConstants.TABLE_M_LABEL_WIDTH)
    val mobile: TableViewField = new TableViewField(ContactI18nEnum.FORM_MOBILE, "mobile", UIConstants.TABLE_M_LABEL_WIDTH)
    val fax: TableViewField = new TableViewField(ContactI18nEnum.FORM_FAX, "fax", UIConstants.TABLE_M_LABEL_WIDTH)
    val birthday: TableViewField = new TableViewField(ContactI18nEnum.FORM_BIRTHDAY, "birthday", UIConstants.TABLE_DATE_WIDTH)
    val isCallable: TableViewField = new TableViewField(ContactI18nEnum.FORM_IS_CALLABLE, "iscallable", UIConstants.TABLE_S_LABEL_WIDTH)
    val assignUser: TableViewField = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
}
