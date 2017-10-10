/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.OpportunityI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object OpportunityTableFieldDef {
    @JvmField
    val selected = TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH)

    @JvmField
    val action = TableViewField(null, "id", WebUIConstants.TABLE_ACTION_CONTROL_WIDTH)

    @JvmField
    val opportunityName = TableViewField(GenericI18Enum.FORM_NAME, "opportunityname", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val currency = TableViewField(GenericI18Enum.FORM_CURRENCY, "currency", WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val amount = TableViewField(OpportunityI18nEnum.FORM_AMOUNT, "amount", WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val probability = TableViewField(OpportunityI18nEnum.FORM_PROBABILITY, "probability", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val accountName = TableViewField(OpportunityI18nEnum.FORM_ACCOUNT_NAME, "accountName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val expectedCloseDate = TableViewField(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE, "expectedcloseddate", WebUIConstants.TABLE_DATE_TIME_WIDTH)

    @JvmField
    val `type` = TableViewField(GenericI18Enum.FORM_TYPE, "type", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val leadSource = TableViewField(OpportunityI18nEnum.FORM_SOURCE, "source", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val campaignName = TableViewField(OpportunityI18nEnum.FORM_CAMPAIGN_NAME, "campaignName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val assignUser = TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val saleStage = TableViewField(OpportunityI18nEnum.FORM_SALE_STAGE, "salesstage", WebUIConstants.TABLE_X_LABEL_WIDTH)
}