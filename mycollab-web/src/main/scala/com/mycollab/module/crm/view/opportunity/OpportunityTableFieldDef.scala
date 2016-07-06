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
package com.mycollab.module.crm.view.opportunity

import com.mycollab.vaadin.web.ui.UIConstants
import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.OpportunityI18nEnum

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object OpportunityTableFieldDef {
  val selected = new TableViewField(null, "selected", UIConstants.TABLE_CONTROL_WIDTH)
  val action = new TableViewField(null, "id", UIConstants.TABLE_ACTION_CONTROL_WIDTH)
  val opportunityName = new TableViewField(GenericI18Enum.FORM_NAME, "opportunityname", UIConstants.TABLE_X_LABEL_WIDTH)
  val currency = new TableViewField(GenericI18Enum.FORM_CURRENCY, "currency", UIConstants.TABLE_S_LABEL_WIDTH)
  val amount = new TableViewField(OpportunityI18nEnum.FORM_AMOUNT, "amount", UIConstants.TABLE_S_LABEL_WIDTH)
  val probability = new TableViewField(OpportunityI18nEnum.FORM_PROBABILITY, "probability", UIConstants.TABLE_X_LABEL_WIDTH)
  val accountName = new TableViewField(OpportunityI18nEnum.FORM_ACCOUNT_NAME, "accountName", UIConstants.TABLE_X_LABEL_WIDTH)
  val expectedCloseDate = new TableViewField(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE, "expectedcloseddate", UIConstants.TABLE_DATE_TIME_WIDTH)
  val `type` = new TableViewField(GenericI18Enum.FORM_TYPE, "type", UIConstants.TABLE_X_LABEL_WIDTH)
  val leadSource = new TableViewField(OpportunityI18nEnum.FORM_SOURCE, "source", UIConstants.TABLE_X_LABEL_WIDTH)
  val campaignName = new TableViewField(OpportunityI18nEnum.FORM_CAMPAIGN_NAME, "campaignName", UIConstants.TABLE_X_LABEL_WIDTH)
  val assignUser = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
  val saleStage = new TableViewField(OpportunityI18nEnum.FORM_SALE_STAGE, "salesstage", UIConstants.TABLE_X_LABEL_WIDTH)
}
