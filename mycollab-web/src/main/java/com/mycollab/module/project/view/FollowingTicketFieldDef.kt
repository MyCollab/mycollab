package com.mycollab.module.project.view

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.FollowerI18nEnum
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object FollowingTicketFieldDef {
    @JvmField
    val summary = TableViewField(FollowerI18nEnum.FORM_SUMMARY, "name",
            WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val project = TableViewField(FollowerI18nEnum.FORM_PROJECT_NAME, "projectName",
            (WebUIConstants.TABLE_EX_LABEL_WIDTH * 1.5).toInt())

    @JvmField
    val assignee = TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUser",
            WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val createdDate = TableViewField(FollowerI18nEnum.OPT_FOLLOWER_CREATE_DATE,
            "monitorDate", WebUIConstants.TABLE_DATE_WIDTH)
}
