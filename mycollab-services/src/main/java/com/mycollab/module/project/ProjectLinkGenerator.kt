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
            "${URL_PREFIX_PARAM}project/dashboard/${GenericLinkUtils.encodeParam(projectId)}"

    @JvmStatic
    fun generateProjectFullLink(siteUrl: String, projectId: Int): String =
            "$siteUrl${generateProjectLink(projectId)}"

    @JvmStatic
    fun generateTicketDashboardLink(projectId: Int): String =
            "${URL_PREFIX_PARAM}project/ticket/dashboard/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateTaskPreviewLink(taskKey: Int?, prjShortName: String): String =
            "${URL_PREFIX_PARAM}project/task/preview/$prjShortName-$taskKey"

    @JvmStatic
    fun generateTaskPreviewFullLink(siteUrl: String, taskKey: Int?, prjShortName: String): String =
            "$siteUrl${generateTaskPreviewLink(taskKey, prjShortName)}"

    @JvmStatic
    fun generateTaskEditLink(taskKey: Int?, prjShortName: String): String =
            "${URL_PREFIX_PARAM}project/task/edit/$prjShortName-$taskKey"

    @JvmStatic
    fun generateMilestonesLink(projectId: Int): String =
            "${URL_PREFIX_PARAM}project/milestone/list/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateMilestonePreviewLink(projectId: Int, milestoneId: Int): String =
            "${URL_PREFIX_PARAM}project/milestone/preview/${GenericLinkUtils.encodeParam(projectId, milestoneId)}"

    @JvmStatic
    fun generateMilestonePreviewFullLink(siteUrl: String, projectId: Int, milestoneId: Int): String =
            "$siteUrl${generateMilestonePreviewLink(projectId, milestoneId)}"

    @JvmStatic
    fun generateClientPreviewLink(accountId: Int): String =
            "${URL_PREFIX_PARAM}project/client/preview/${UrlEncodeDecoder.encode(accountId)}"

    @JvmStatic
    fun generateClientPreviewFullLink(siteUrl: String, accountId: Int): String =
            "$siteUrl${generateClientPreviewLink(accountId)}}"

    @JvmStatic
    fun generatePagesLink(projectId: Int, folderPath: String): String =
            "${URL_PREFIX_PARAM}project/page/list/${GenericLinkUtils.encodeParam(projectId, folderPath)}"

    @JvmStatic
    fun generatePageAdd(projectId: Int, pagePath: String): String =
            "${URL_PREFIX_PARAM}project/page/add/${GenericLinkUtils.encodeParam(projectId, pagePath)}"

    @JvmStatic
    fun generatePageRead(projectId: Int, pagePath: String): String =
            "${URL_PREFIX_PARAM}project/page/preview/${GenericLinkUtils.encodeParam(projectId, pagePath)}"

    @JvmStatic
    fun generatePageEdit(projectId: Int, pagePath: String): String =
            "${URL_PREFIX_PARAM}project/page/edit/${GenericLinkUtils.encodeParam(projectId, pagePath)}"

    @JvmStatic
    fun generateProjectMemberLink(projectId: Int, memberName: String?): String =
            "${URL_PREFIX_PARAM}project/user/preview/${GenericLinkUtils.encodeParam(projectId, memberName)}"

    @JvmStatic
    fun generateProjectMemberFullLink(siteUrl: String, projectId: Int, memberName: String?): String =
            memberName?: "$siteUrl${generateProjectMemberLink(projectId, memberName)}"

    @JvmStatic
    fun generateRiskPreviewLink(projectId: Int, riskId: Int?): String =
            "${URL_PREFIX_PARAM}project/risk/preview/${GenericLinkUtils.encodeParam(projectId, riskId)}"

    @JvmStatic
    fun generateRiskPreviewFullLink(siteUrl: String, projectId: Int, riskId: Int): String {
        return "$siteUrl${generateRiskPreviewLink(projectId, riskId)}"
    }

    @JvmStatic
    fun generateRiskEditLink(projectId: Int, riskId: Int): String =
            "${URL_PREFIX_PARAM}project/risk/edit/${UrlEncodeDecoder.encode("$projectId/$riskId")}"

    @JvmStatic
    fun generateRiskAddLink(projectId: Int): String =
            "${URL_PREFIX_PARAM}project/risk/add/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateMessageAddLink(projectId: Int): String = "${URL_PREFIX_PARAM}project/message/add/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateMessagesLink(projectId: Int): String = "${URL_PREFIX_PARAM}project/message/list/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateMessagePreviewLink(projectId: Int, messageId: Int): String =
            "${URL_PREFIX_PARAM}project/message/preview/${GenericLinkUtils.encodeParam(projectId, messageId)}"

    @JvmStatic
    fun generateMessagePreviewFullLink(siteUrl: String, projectId: Int, messageId: Int): String =
            "$siteUrl${generateMessagePreviewLink(projectId, messageId)}"

    @JvmStatic
    fun generateBugComponentPreviewLink(projectId: Int, componentId: Int): String =
            "${URL_PREFIX_PARAM}project/component/preview/${GenericLinkUtils.encodeParam(projectId, componentId)}"

    @JvmStatic
    fun generateBugComponentPreviewFullLink(siteUrl: String, projectId: Int, componentId: Int): String =
            "$siteUrl${generateBugComponentPreviewLink(projectId, componentId)}"

    @JvmStatic
    fun generateBugVersionPreviewLink(projectId: Int, versionId: Int): String =
            "${URL_PREFIX_PARAM}project/version/preview/${GenericLinkUtils.encodeParam(projectId, versionId)}"

    @JvmStatic
    fun generateBugVersionPreviewFullLink(siteUrl: String, projectId: Int, versionId: Int): String =
            "$siteUrl${generateBugVersionPreviewLink(projectId, versionId)}"

    @JvmStatic
    fun generateBugPreviewLink(bugKey: Int?, prjShortName: String): String =
            "${URL_PREFIX_PARAM}project/bug/preview/$prjShortName-$bugKey"

    @JvmStatic
    fun generateBugEditLink(bugKey: Int?, prjShortName: String): String = "${URL_PREFIX_PARAM}project/bug/edit/$prjShortName-$bugKey"

    @JvmStatic
    fun generateBugsLink(projectId: Int): String = "${URL_PREFIX_PARAM}project/bug/list/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateBugPreviewFullLink(siteUrl: String, bugKey: Int?, prjShortName: String): String =
            "$siteUrl${generateBugPreviewLink(bugKey, prjShortName)}"

    @JvmStatic
    fun generateFileDashboardLink(projectId: Int): String =
            "${URL_PREFIX_PARAM}project/file/dashboard/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateTimeReportLink(projectId: Int): String =
            "${URL_PREFIX_PARAM}project/time/list/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateInvoiceListLink(projectId: Int): String =
            "${URL_PREFIX_PARAM}project/invoice/list/${UrlEncodeDecoder.encode(projectId)}"


    @JvmStatic
    fun generateRolePreviewLink(projectId: Int, roleId: Int?): String =
            "${URL_PREFIX_PARAM}project/role/preview/${GenericLinkUtils.encodeParam(projectId, roleId)}"

    @JvmStatic
    fun generateRolePreviewFullLink(siteUrl: String, projectId: Int, roleId: Int?): String =
            "$siteUrl${generateRolePreviewLink(projectId, roleId)}"

    @JvmStatic
    fun generateStandupDashboardLink(): String = "${URL_PREFIX_PARAM}project/reports/standup/list/"

    @JvmStatic
    fun generateHoursWeeklyReportLink(): String = "${URL_PREFIX_PARAM}project/reports/weeklytiming/"

    @JvmStatic
    fun generateTimesheetReportLink(): String = "${URL_PREFIX_PARAM}project/reports/timesheet/"

    @JvmStatic
    fun generateUsersWorkloadReportLink(): String = "${URL_PREFIX_PARAM}project/reports/usersworkload/"

    @JvmStatic
    fun generateUsersLink(projectId: Int): String =
            "${URL_PREFIX_PARAM}project/user/list/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateProjectSettingLink(projectId: Int?): String =
            "${URL_PREFIX_PARAM}project/setting/${UrlEncodeDecoder.encode(projectId)}"

    @JvmStatic
    fun generateProjectSettingFullLink(siteUrl: String, projectId: Int): String =
            "$siteUrl${generateProjectSettingLink(projectId)}"

    @JvmStatic
    fun generateProjectItemLink(prjShortName: String, projectId: Int, type: String, typeId: String?): String {

        if (typeId == null || StringUtils.isBlank(typeId) || "null" == typeId) {
            return ""
        }

        try {
            return when (type) {
                ProjectTypeConstants.MESSAGE -> generateMessagePreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.MILESTONE -> generateMilestonePreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.RISK -> generateRiskPreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.TASK -> generateTaskPreviewLink(Integer.parseInt(typeId), prjShortName)
                ProjectTypeConstants.BUG -> generateBugPreviewLink(Integer.parseInt(typeId), prjShortName)
                ProjectTypeConstants.BUG_COMPONENT -> generateBugComponentPreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.BUG_VERSION -> generateBugVersionPreviewLink(projectId, Integer.parseInt(typeId))
                ProjectTypeConstants.PAGE -> generatePageRead(projectId, typeId)
                else -> ""
            }
        } catch (e: Exception) {
            throw DebugException("Error generate tooltip $projectId---$type---$typeId", e)
        }
    }
}
