package com.mycollab.module.user.accountsettings.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object RoleTableFieldDef {
    @JvmField
    val selected = TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH)

    @JvmField
    val rolename = TableViewField(GenericI18Enum.FORM_NAME, "rolename", WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val isDefault = TableViewField(RoleI18nEnum.FORM_IS_DEFAULT, "isdefault", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val description = TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", WebUIConstants.TABLE_EX_LABEL_WIDTH)
}