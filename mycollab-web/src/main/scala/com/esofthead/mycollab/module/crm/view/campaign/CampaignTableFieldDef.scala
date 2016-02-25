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
package com.esofthead.mycollab.module.crm.view.campaign

import com.esofthead.mycollab.common.TableViewField
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum
import com.esofthead.mycollab.vaadin.web.ui.UIConstants

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object CampaignTableFieldDef {
  val selected = new TableViewField(null, "selected", UIConstants.TABLE_CONTROL_WIDTH)
  val action = new TableViewField(null, "id", UIConstants.TABLE_ACTION_CONTROL_WIDTH)
  val actualcost = new TableViewField(CampaignI18nEnum.FORM_ACTUAL_COST, "actualcost", UIConstants.TABLE_M_LABEL_WIDTH)
  val budget = new TableViewField(CampaignI18nEnum.FORM_BUDGET, "budget", UIConstants.TABLE_M_LABEL_WIDTH)
  val campaignname = new TableViewField(CampaignI18nEnum.FORM_CAMPAIGN_NAME, "campaignname", UIConstants.TABLE_X_LABEL_WIDTH)
  val status = new TableViewField(CampaignI18nEnum.FORM_STATUS, "status", UIConstants.TABLE_M_LABEL_WIDTH)
  val `type` = new TableViewField(CampaignI18nEnum.FORM_TYPE, "type", UIConstants.TABLE_S_LABEL_WIDTH)
  val expectedCost = new TableViewField(CampaignI18nEnum.FORM_EXPECTED_COST, "expectedcost", UIConstants.TABLE_M_LABEL_WIDTH)
  val expectedRevenue = new TableViewField(CampaignI18nEnum.FORM_EXPECTED_REVENUE, "expectedrevenue", UIConstants.TABLE_M_LABEL_WIDTH)
  val endDate = new TableViewField(CampaignI18nEnum.FORM_END_DATE, "enddate", UIConstants.TABLE_DATE_WIDTH)
  val startDate = new TableViewField(CampaignI18nEnum.FORM_START_DATE, "startdate", UIConstants.TABLE_DATE_WIDTH)
  val assignUser = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
}
