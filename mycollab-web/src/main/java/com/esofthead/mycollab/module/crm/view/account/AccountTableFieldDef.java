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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public interface AccountTableFieldDef {

	public static TableViewField selected = new TableViewField(null,
			"selected", UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField action = new TableViewField(null, "id",
			UIConstants.TABLE_ACTION_CONTROL_WIDTH);

	public static TableViewField accountname = new TableViewField(
			AccountI18nEnum.FORM_ACCOUNT_NAME, "accountname",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField city = new TableViewField(
			AccountI18nEnum.FORM_BILLING_CITY, "city",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField phoneoffice = new TableViewField(
			AccountI18nEnum.FORM_OFFICE_PHONE, "phoneoffice",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField email = new TableViewField(
			AccountI18nEnum.FORM_EMAIL, "email", UIConstants.TABLE_EMAIL_WIDTH);

	public static TableViewField assignUser = new TableViewField(
			GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField website = new TableViewField(
			AccountI18nEnum.FORM_WEBSITE, "website",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField type = new TableViewField(
			AccountI18nEnum.FORM_TYPE, "type", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField ownership = new TableViewField(
			AccountI18nEnum.FORM_OWNERSHIP, "ownership",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField fax = new TableViewField(
			AccountI18nEnum.FORM_FAX, "fax", UIConstants.TABLE_M_LABEL_WIDTH);

}
