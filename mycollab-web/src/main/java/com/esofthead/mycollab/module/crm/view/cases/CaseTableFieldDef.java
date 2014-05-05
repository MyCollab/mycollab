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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.crm.localization.CaseI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

public interface CaseTableFieldDef {
	public static TableViewField selected = new TableViewField("", "selected",
			UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField action = new TableViewField("", "id");

	public static TableViewField priority = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_PRIORITY),
			"priority", UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField status = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_STATUS), "status",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField account = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_ACCOUNT),
			"accountName", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField origin = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_ORIGIN), "origin",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField phone = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_PHONE),
			"phonenumber", UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField type = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_TYPE), "type",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField reason = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_REASON), "reason",
			UIConstants.TABLE_EX_LABEL_WIDTH);

	public static TableViewField subject = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_SUBJECT),
			"subject", UIConstants.TABLE_EX_LABEL_WIDTH);

	public static TableViewField email = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_EMAIL), "email",
			UIConstants.TABLE_EMAIL_WIDTH);

	public static TableViewField assignUser = new TableViewField(
			AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
			"assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField createdTime = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_CREATED_TIME),
			"createdtime", UIConstants.TABLE_DATE_TIME_WIDTH);

	public static TableViewField lastUpdatedTime = new TableViewField(
			AppContext.getMessage(CaseI18nEnum.FORM_LAST_UPDATED_TIME),
			"lastupdatedtime", UIConstants.TABLE_DATE_TIME_WIDTH);
}
