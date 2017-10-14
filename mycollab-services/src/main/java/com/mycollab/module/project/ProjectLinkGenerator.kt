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

import com.mycollab.common.GenericLinkUtils
import com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM
import com.mycollab.common.UrlEncodeDecoder
import com.mycollab.core.DebugException
import com.mycollab.core.utils.StringUtils

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object ProjectLinkGenerator {

    @JvmStatic
    fun generateProjectLink(projectId: Int): String =
            "project/dashboard/${GenericLinkUtils.encodeParam(projectId)}"

    @JvmStatic
    fun generateProjectFullLink(siteUrl: String, projectId: Int): String =
            "$siteUrl$URL_PREFIX_PARAM${generateProjectLink(projectId)}"

    @JvmStatic
    fun generateTicketDashboardLink(projectId: Int): String =
            "project/ticket/dashboard/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateTaskPreviewLink(taskKey: Int?, prjShortName: String): String =
            "project/task/preview/$prjShortName-$taskKey"

    @JvmStatic
    fun generateTaskPreviewFullLink(siteUrl: String, taskKey: Int?, prjShortName: String): String =
            """$siteUrl$URL_PREFIX_PARAM${generateTaskPreviewLink(taskKey, prjShortName)}"""

    @JvmStatic
    fun generateTaskEditLink(taskKey: Int?, prjShortName: String): String =
            "project/task/edit/$prjShortName-$taskKey"

    @JvmStatic
    fun generateMilestonesLink(projectId: Int): String =
            """project/milestone/list/${UrlEncodeDecoder.encode(projectId)}"""

    @JvmStatic
    fun generateMilestonePreviewLink(projectId: Int, milestoneId: Int): String =
            "project/milestone/preview/${GenericLinkUtils.encodeParam(projectId, milestoneId)}"

    @JvmStatic
    fun generateMilestonePreviewFullLink(siteUrl: String, projectId: Int, milestoneId: Int): String =
            "$siteUrl$URL_PREFIX_PARAM${generateMilestonePreviewLink(projectId, milestoneId)}"

    @JvmStatic
    fun generateClientPreviewLink(accountId: Int): String =
            """project/client/preview/${UrlEncodeDecoder.encode(accountId)}"""

    @JvmStatic
    fun generateClientPreviewFullLink(siteUrl: String, accountId: Int): String =
            """$siteUrl${URL_PREFIX_PARAM}project/client/preview/${UrlEncodeDecoder.encode(accountId)}"""

    @JvmStatic
    fun generatePagesLink(projectId: Int, folderPath: String): String =
            """project/page/list/${GenericLinkUtils.encodeParam(projectId, folderPath)}"""

    @JvmStatic
    fun generatePageAdd(projectId: Int, pagePath: String): String =
            """project/page/add/${GenericLinkUtils.encodeParam(projectId, pagePath)}"""

    @JvmStatic
    fun generatePageRead(projectId: Int, pagePath: String): String =
            """project/page/preview/${GenericLinkUtils.encodeParam(projectId, pagePath)}"""

    @JvmStatic
    fun generatePageEdit(projectId: Int, pagePath: String): String =
            """project/page/edit/${GenericLinkUtils.encodeParam(projectId, pagePath)}"""

    @JvmStatic
    fun generateProjectMemberFullLink(siteUrl: String, projectId: Int, memberName: String?): String {
        return if (memberName == null) {
            ""
        } else """$siteUrl${URL_PREFIX_PARAM}project/user/preview/${GenericLinkUtils.encodeParam(projectId, memberName)}"""
    }

    @JvmStatic
    fun generateProjectMemberLink(projectId: Int?, memberName: String?): String =
            """project/user/preview/${GenericLinkUtils.encodeParam(projectId, memberName)}"""

    @JvmStatic
    fun generateRiskPreviewLink(projectId: Int, riskId: Int?): String =
            """project/risk/preview/${GenericLinkUtils.encodeParam(projectId, riskId)}"""

    @JvmStatic
    fun generateRiskPreviewFullLink(siteUrl: String, projectId: Int, riskId: Int): String {
        return "$siteUrl$URL_PREFIX_PARAM${generateRiskPreviewLink(projectId, riskId)}"
    }

    @JvmStatic
    fun generateRiskEditLink(projectId: Int, riskId: Int): String =
            """project/risk/edit/${UrlEncodeDecoder.encode("""${projectId.toString()}/$riskId""")}"""

    @JvmStatic
    fun generateRiskAddLink(projectId: Int): String =
            "project/risk/add/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateMessageAddLink(projectId: Int): String = """project/message/add/${UrlEncodeDecoder.encode(projectId)}"""

    @JvmStatic
    fun generateMessagesLink(projectId: Int): String = """project/message/list/${UrlEncodeDecoder.encode(projectId)}"""

    @JvmStatic
    fun generateMessagePreviewLink(projectId: Int, messageId: Int): String =
            """project/message/preview/${GenericLinkUtils.encodeParam(projectId, messageId)}"""

    @JvmStatic
    fun generateMessagePreviewFullLink(siteUrl: String, projectId: Int, messageId: Int): String =
            """$siteUrl$URL_PREFIX_PARAM${generateMessagePreviewLink(projectId, messageId)}"""

    @JvmStatic
    fun generateBugComponentPreviewLink(projectId: Int, componentId: Int): String =
            """project/component/preview/${GenericLinkUtils.encodeParam(projectId, componentId)}"""

    @JvmStatic
    fun generateBugComponentPreviewFullLink(siteUrl: String, projectId: Int, componentId: Int): String =
            """$siteUrl$URL_PREFIX_PARAM${generateBugComponentPreviewLink(projectId, componentId)}"""

    @JvmStatic
    fun generateBugVersionPreviewLink(projectId: Int, versionId: Int): String =
            """project/version/preview/${GenericLinkUtils.encodeParam(projectId, versionId)}"""

    @JvmStatic
    fun generateBugVersionPreviewFullLink(siteUrl: String, projectId: Int, versionId: Int): String =
            """$siteUrl$URL_PREFIX_PARAM${generateBugVersionPreviewLink(projectId, versionId)}"""

    @JvmStatic
    fun generateBugPreviewLink(bugKey: Int?, prjShortName: String): String =
            "project/bug/preview/$prjShortName-$bugKey"

    @JvmStatic
    fun generateBugEditLink(bugKey: Int?, prjShortName: String): String = "project/bug/edit/$prjShortName-$bugKey"

    @JvmStatic
    fun generateBugsLink(projectId: Int): String = """project/bug/list/${UrlEncodeDecoder.encode(projectId)}"""

    @JvmStatic
    fun generateBugPreviewFullLink(siteUrl: String, bugKey: Int?, prjShortName: String): String =
            "$siteUrl$URL_PREFIX_PARAM${generateBugPreviewLink(bugKey, prjShortName)}"

    @JvmStatic
    fun generateFileDashboardLink(projectId: Int): String =
            "project/file/dashboard/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateTimeReportLink(projectId: Int): String =
            "project/time/list/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateInvoiceListLink(projectId: Int): String =
            "project/invoice/list/${UrlEncodeDecoder.encode(projectId)}"


    @JvmStatic
    fun generateRolePreviewLink(projectId: Int, roleId: Int?): String =
            "project/role/preview/${GenericLinkUtils.encodeParam(projectId, roleId)}"

    @JvmStatic
    fun generateRolePreviewFullLink(siteUrl: String, projectId: Int, roleId: Int?): String =
            "$siteUrl$URL_PREFIX_PARAM${generateRolePreviewLink(projectId, roleId)}"

    @JvmStatic
    fun generateStandupDashboardLink(): String = "project/reports/standup/list/"

    @JvmStatic
    fun generateHoursWeeklyReportLink(): String = "project/reports/weeklytiming/"

    @JvmStatic
    fun generateTimesheetReportLink(): String = "project/reports/timesheet/"

    @JvmStatic
    fun generateUsersWorkloadReportLink(): String = "project/reports/usersworkload/"

    @JvmStatic
    fun generateUsersLink(projectId: Int): String =
            "project/user/list/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateProjectSettingLink(projectId: Int?): String =
            "project/setting/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateProjectSettingFullLink(siteUrl: String, projectId: Int): String =
            "$siteUrl$URL_PREFIX_PARAM${generateProjectSettingLink(projectId)}"

    @JvmStatic
    fun generateProjectItemLink(prjShortName: String, projectId: Int, type: String, typeId: String?): String {
        var result = ""

        if (typeId == null || StringUtils.isBlank(typeId) || "null" == typeId) {
            return ""
        }

        try {
            when (type) {
                ProjectTypeConstants.PROJECT -> {
                }
                ProjectTypeConstants.MESSAGE -> result = generateMessagePreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.MILESTONE -> result = generateMilestonePreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.RISK -> result = generateRiskPreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.TASK -> result = generateTaskPreviewLink(Integer.parseInt(typeId), prjShortName)
                ProjectTypeConstants.BUG -> result = generateBugPreviewLink(Integer.parseInt(typeId), prjShortName)
                ProjectTypeConstants.BUG_COMPONENT -> result = generateBugComponentPreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.BUG_VERSION -> result = generateBugVersionPreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.PAGE -> result = generatePageRead(projectId, typeId)
            }
        } catch (e: Exception) {
            throw DebugException("Error generate tooltip $projectId---$type---$typeId", e)
        }

        return "#$result"
    }
}
