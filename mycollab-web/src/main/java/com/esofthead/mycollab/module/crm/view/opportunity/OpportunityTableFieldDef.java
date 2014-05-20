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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.module.crm.localization.OpportunityI18nEnum;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public interface OpportunityTableFieldDef {
	public static TableViewField selected = new TableViewField(null,
			"selected", UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField action = new TableViewField(null, "id",
			UIConstants.TABLE_ACTION_CONTROL_WIDTH);

	public static TableViewField opportunityName = new TableViewField(
			OpportunityI18nEnum.FORM_NAME, "opportunityname",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField currency = new TableViewField(
			OpportunityI18nEnum.FORM_CURRENCY, "currency",
			UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField amount = new TableViewField(
			OpportunityI18nEnum.FORM_AMOUNT, "amount",
			UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField probability = new TableViewField(
			OpportunityI18nEnum.FORM_PROBABILITY, "probability",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField accountName = new TableViewField(
			OpportunityI18nEnum.FORM_ACCOUNT_NAME, "accountName",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField expectedCloseDate = new TableViewField(
			OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE, "expectedcloseddate",
			UIConstants.TABLE_DATE_TIME_WIDTH);

	public static TableViewField type = new TableViewField(
			OpportunityI18nEnum.FORM_TYPE, "type",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField leadSource = new TableViewField(
			OpportunityI18nEnum.FORM_SOURCE, "source",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField campaignName = new TableViewField(
			OpportunityI18nEnum.FORM_CAMPAIGN_NAME, "campaignName",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField assignUser = new TableViewField(
			OpportunityI18nEnum.FORM_ASSIGN_USER, "assignUserFullName",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField saleStage = new TableViewField(
			OpportunityI18nEnum.FORM_SALE_STAGE, "salesstage",
			UIConstants.TABLE_X_LABEL_WIDTH);
}
