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
package com.mycollab.module.project

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Img
import com.hp.gagawa.java.elements.Text
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.DivLessFormatter
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.project.ui.ProjectAssetsManager
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.TooltipHelper
import com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID
import com.mycollab.vaadin.ui.UIConstants

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object ProjectLinkBuilder {

    @JvmStatic
    fun generateProjectMemberHtmlLink(projectId: Int, username: String?, displayName: String?, avatarId: String?,
                                      isDisplayTooltip: Boolean): String = if (username != null) {
        val userAvatar = Img("", AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                .getAvatarPath(avatarId, 16)).setCSSClass(UIConstants.CIRCLE_BOX)
        val link = A().setId("tag$TOOLTIP_ID").setHref(ProjectLinkGenerator.generateProjectMemberLink(projectId,
                username)).appendText(StringUtils.trim(displayName, 30, true))
        if (isDisplayTooltip) {
            link.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username))
            link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
            DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, link).write()
        } else {
            DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, link).write()
        }
    } else ""

    @JvmStatic
    fun generateProjectMemberHtmlLink(projectId: Int, username: String, isDisplayTooltip: Boolean): String? {
        val projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService::class.java)
        val member = projectMemberService.findMemberByUsername(username, projectId, AppUI.accountId)
        return if (member != null) {
            generateProjectMemberHtmlLink(projectId, member.username, member.displayName,
                    member.memberAvatarId, isDisplayTooltip)
        } else null
    }

    @JvmStatic
    fun generateProjectItemHtmlLinkAndTooltip(prjShortName: String, projectId: Int, summary: String, type: String, typeId: String): String {
        val image = Text(ProjectAssetsManager.getAsset(type).html)
        val link = A().setId("tag$TOOLTIP_ID")
        link.setHref(ProjectLinkGenerator.generateProjectItemLink(prjShortName, projectId, type, typeId))
                .appendChild(Text(summary))
        link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(type, typeId))
        link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
        val div = DivLessFormatter().appendChild(image, DivLessFormatter.EMPTY_SPACE, link)
        return div.write()
    }
}
