package com.mycollab.module.project.ui.components

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
object GenericTaskTableFieldDef {
    @JvmField val name = TableViewField(GenericI18Enum.FORM_DESCRIPTION, "name",
            WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField val assignUser = TableViewField(GenericI18Enum.FORM_ASSIGNEE,
            "assignUser", WebUIConstants.TABLE_EX_LABEL_WIDTH)
}
