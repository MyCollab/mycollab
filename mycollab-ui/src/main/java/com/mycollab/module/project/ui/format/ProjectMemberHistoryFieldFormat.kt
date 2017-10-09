package com.mycollab.module.project.ui.format

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Img
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.DivLessFormatter
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.module.user.service.UserService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.TooltipHelper
import com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID
import com.mycollab.vaadin.ui.UIConstants
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class ProjectMemberHistoryFieldFormat : HistoryFieldFormat {

    override fun toString(value: String): String = toString(value, true, "")

    override fun toString(value: String, displayAsHtml: Boolean?, msgIfBlank: String): String {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank
        }

        try {
            val userService = AppContextUtil.getSpringBean(UserService::class.java)
            val user = userService.findUserByUserNameInAccount(value, AppUI.accountId)
            if (user != null) {
                return if (displayAsHtml!!) {
                    val userAvatar = Img("", AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                            .getAvatarPath(user.avatarid, 16)).setCSSClass(UIConstants.CIRCLE_BOX)
                    val link = A().setId("tag" + TOOLTIP_ID).appendText(StringUtils.trim(user.displayName, 30, true))
                    link.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(user.username))
                    link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
                    DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, link).write()
                } else user.displayName
            }
        } catch (e: Exception) {
            LOG.error("Error", e)
        }

        return value
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ProjectMemberHistoryFieldFormat::class.java)
    }
}
