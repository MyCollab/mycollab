/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    override fun toString(value: String, displayAsHtml: Boolean, msgIfBlank: String): String {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank
        }

        try {
            val userService = AppContextUtil.getSpringBean(UserService::class.java)
            val user = userService.findUserByUserNameInAccount(value, AppUI.accountId)
            if (user != null) {
                return if (displayAsHtml) {
                    val userAvatar = Img("", AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                            .getAvatarPath(user.avatarid, 16)).setCSSClass(UIConstants.CIRCLE_BOX)
                    val link = A().setId("tag" + TOOLTIP_ID).appendText(StringUtils.trim(user.displayName, 30, true))
                    link.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(user.username))
                    link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
                    DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, link).write()
                } else user.displayName!!
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
