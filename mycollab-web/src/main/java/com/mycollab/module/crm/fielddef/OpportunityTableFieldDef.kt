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