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
import com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM
import com.mycollab.common.UrlEncodeDecoder
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
    fun generateProjectFullLink(projectId: Int): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateProjectLink(projectId)}"""

    @JvmStatic
    fun generateComponentPreviewFullLink(projectId: Int, componentId: Int): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateBugComponentPreviewLink(projectId, componentId)}"""

    @JvmStatic
    fun generateBugVersionPreviewFullLink(projectId: Int, versionId: Int): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateBugVersionPreviewLink(projectId, versionId)}"""

    @JvmStatic
    fun generateRolePreviewFullLink(projectId: Int, roleId: Int): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateRolePreviewLink(projectId, roleId)}"""

    @JvmStatic
    fun generateProjectMemberFullLink(projectId: Int, memberName: String): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateProjectMemberLink(projectId, memberName)}"""

    @JvmStatic
    fun generateProjectMemberHtmlLink(projectId: Int, username: String, displayName: String, avatarId: String,
                                      isDisplayTooltip: Boolean): String {
        val userAvatar = Img("", AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                .getAvatarPath(avatarId, 16)).setCSSClass(UIConstants.CIRCLE_BOX)
        val link = A().setId("tag" + TOOLTIP_ID).setHref(generateProjectMemberFullLink(projectId,
                username)).appendText(StringUtils.trim(displayName, 30, true))
        return if (isDisplayTooltip) {
            link.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(username))
            link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
            DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, link).write()
        } else {
            DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, link).write()
        }
    }

    @JvmStatic
    fun generateProjectMemberHtmlLink(projectId: Int, username: String, isDisplayTooltip: Boolean): String? {
        val projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService::class.java)
        val member = projectMemberService.findMemberByUsername(username, projectId, AppUI.accountId)
        return if (member != null) {
            generateProjectMemberHtmlLink(projectId, member.username, member.displayName, member
                    .memberAvatarId, isDisplayTooltip)
        } else null
    }

    @JvmStatic
    fun generateBugPreviewFullLink(bugKey: Int?, prjShortName: String): String {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateBugPreviewLink(bugKey, prjShortName)
    }

    @JvmStatic
    fun generateMessagePreviewFullLink(projectId: Int?, messageId: Int?): String {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateMessagePreviewLink(projectId!!, messageId!!)
    }

    @JvmStatic
    fun generateTaskPreviewFullLink(taskKey: Int?, prjShortName: String): String {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateTaskPreviewLink(taskKey, prjShortName)
    }

    @JvmStatic
    fun generateMilestonePreviewFullLink(projectId: Int?, milestoneId: Int?): String {
        return URL_PREFIX_PARAM + ProjectLinkGenerator.generateMilestonePreviewLink(projectId!!, milestoneId!!)
    }

    @JvmStatic
    fun generateClientPreviewFullLink(clientId: Int): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateClientPreviewLink(clientId)}"""

    @JvmStatic
    fun generatePageFolderFullLink(projectId: Int, folderPath: String): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generatePagesLink(projectId, folderPath)}"""

    @JvmStatic
    fun generatePageFullLink(projectId: Int, pagePath: String): String =
            """$URL_PREFIX_PARAM${ProjectLinkGenerator.generatePageRead(projectId, pagePath)}"""

    @JvmStatic
    fun generateStandupDashboardLink(): String = """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateStandupDashboardLink()}"""

    @JvmStatic
    fun generateHoursWeeklyReportLink(): String = """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateHoursWeeklyReportLink()}"""

    @JvmStatic
    fun generateTimesheetReportLink(): String = """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateTimesheetReportLink()}"""

    @JvmStatic
    fun generateUsersWorkloadReportLink(): String = """$URL_PREFIX_PARAM${ProjectLinkGenerator.generateUsersWorkloadReportLink()}"""

    @JvmStatic
    fun generateProjectItemHtmlLinkAndTooltip(prjShortName: String, projectId: Int, summary: String, type: String, typeId: String): String {
        val image = Text(ProjectAssetsManager.getAsset(type).html)
        val link = A().setId("tag" + TOOLTIP_ID)
        link.setHref(ProjectLinkGenerator.generateProjectItemLink(prjShortName, projectId, type, typeId))
                .appendChild(Text(summary))
        link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(type, typeId))
        link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
        val div = DivLessFormatter().appendChild(image, DivLessFormatter.EMPTY_SPACE, link)
        return div.write()
    }
}
