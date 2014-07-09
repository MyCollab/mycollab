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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.vaadin.ui.UIConstants;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public interface LeadTableFieldDef {
	public static TableViewField selected = new TableViewField(null,
			"selected", UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField action = new TableViewField(null, "id",
			UIConstants.TABLE_ACTION_CONTROL_WIDTH);

	public static TableViewField name = new TableViewField(
			LeadI18nEnum.FORM_NAME, "leadName", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField title = new TableViewField(
			LeadI18nEnum.FORM_TITLE, "title", UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField department = new TableViewField(
			LeadI18nEnum.FORM_DEPARTMENT, "department",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField accountName = new TableViewField(
			LeadI18nEnum.FORM_ACCOUNT_NAME, "accountname",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField leadSource = new TableViewField(
			LeadI18nEnum.FORM_LEAD_SOURCE, "leadsourcedesc",
			UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField industry = new TableViewField(
			LeadI18nEnum.FORM_INDUSTRY, "industry",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField email = new TableViewField(
			LeadI18nEnum.FORM_EMAIL, "email", UIConstants.TABLE_EMAIL_WIDTH);

	public static TableViewField phoneoffice = new TableViewField(
			LeadI18nEnum.FORM_OFFICE_PHONE, "officephone",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField mobile = new TableViewField(
			LeadI18nEnum.FORM_MOBILE, "mobile", UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField fax = new TableViewField(
			LeadI18nEnum.FORM_FAX, "fax", UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField status = new TableViewField(
			LeadI18nEnum.FORM_STATUS, "status", UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField website = new TableViewField(
			LeadI18nEnum.FORM_WEBSITE, "website",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField assignedUser = new TableViewField(
			GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName",
			UIConstants.TABLE_X_LABEL_WIDTH);

}
