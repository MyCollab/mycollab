package com.mycollab.module.crm.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.AccountI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object AccountTableFieldDef {
    @JvmField
    val selected = TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH)

    @JvmField
    val action = TableViewField(null, "id", WebUIConstants.TABLE_ACTION_CONTROL_WIDTH)

    @JvmField
    val accountname = TableViewField(AccountI18nEnum.FORM_ACCOUNT_NAME, "accountname", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val city = TableViewField(AccountI18nEnum.FORM_BILLING_CITY, "city", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val phoneoffice = TableViewField(AccountI18nEnum.FORM_OFFICE_PHONE, "phoneoffice", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val email = TableViewField(GenericI18Enum.FORM_EMAIL, "email", WebUIConstants.TABLE_EMAIL_WIDTH)

    @JvmField
    val assignUser = TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val website = TableViewField(AccountI18nEnum.FORM_WEBSITE, "website", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val type = TableViewField(GenericI18Enum.FORM_TYPE, "type", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val ownership = TableViewField(AccountI18nEnum.FORM_OWNERSHIP, "ownership", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val fax = TableViewField(AccountI18nEnum.FORM_FAX, "fax", WebUIConstants.TABLE_M_LABEL_WIDTH)
}