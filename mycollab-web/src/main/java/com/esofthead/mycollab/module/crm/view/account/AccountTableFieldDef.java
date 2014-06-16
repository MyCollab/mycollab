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

import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
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
			CrmCommonI18nEnum.TABLE_NAME_HEADER, "accountname",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField city = new TableViewField(
			CrmCommonI18nEnum.TABLE_CITY_HEADER, "city",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField phoneoffice = new TableViewField(
			CrmCommonI18nEnum.TABLE_OFFICE_PHONE_HEADER, "phoneoffice",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField email = new TableViewField(
			CrmCommonI18nEnum.TABLE_EMAIL_ADDRESS_HEADER, "email",
			UIConstants.TABLE_EMAIL_WIDTH);

	public static TableViewField assignUser = new TableViewField(
			CrmCommonI18nEnum.TABLE_ASSIGNED_USER_HEADER, "assignUserFullName",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField website = new TableViewField(
			CrmCommonI18nEnum.TABLE_WEBSITE_HEADER, "website",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField type = new TableViewField(
			AccountI18nEnum.FORM_TYPE, "type", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField ownership = new TableViewField(
			AccountI18nEnum.FORM_OWNERSHIP, "ownership",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField fax = new TableViewField(
			AccountI18nEnum.FORM_FAX, "fax", UIConstants.TABLE_M_LABEL_WIDTH);

}
