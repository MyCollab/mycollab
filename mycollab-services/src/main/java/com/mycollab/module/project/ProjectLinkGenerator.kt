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

    @JvmStatic fun generateProjectLink(projectId: Int?): String {
        return "project/dashboard/" + GenericLinkUtils.encodeParam(projectId!!)
    }

    @JvmStatic fun generateProjectFullLink(siteUrl: String, projectId: Int?): String {
        return siteUrl + URL_PREFIX_PARAM + generateProjectLink(projectId)
    }

    @JvmStatic fun generateTicketDashboardLink(projectId: Int): String {
        return "project/ticket/dashboard/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateTaskPreviewLink(taskKey: Int?, prjShortName: String): String {
        return String.format("project/task/preview/%s-%d", prjShortName, taskKey)
    }

    @JvmStatic fun generateTaskPreviewFullLink(siteUrl: String, taskKey: Int?, prjShortName: String): String {
        return siteUrl + URL_PREFIX_PARAM + generateTaskPreviewLink(taskKey, prjShortName)
    }

    @JvmStatic fun generateTaskEditLink(taskKey: Int?, prjShortName: String): String {
        return String.format("project/task/edit/%s-%d", prjShortName, taskKey)
    }

    @JvmStatic fun generateMilestonesLink(projectId: Int): String {
        return "project/milestone/list/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateMilestonePreviewLink(projectId: Int, milestoneId: Int?): String {
        return "project/milestone/preview/" + GenericLinkUtils.encodeParam(projectId, milestoneId)
    }

    @JvmStatic fun generateMilestonePreviewFullLink(siteUrl: String, projectId: Int, milestoneId: Int?): String {
        return siteUrl + URL_PREFIX_PARAM + generateMilestonePreviewLink(projectId, milestoneId)
    }

    @JvmStatic fun generateClientPreviewLink(accountId: Int?): String {
        return "project/client/preview/" + UrlEncodeDecoder.encode(accountId)
    }

    @JvmStatic fun generateClientPreviewFullLink(siteUrl: String, accountId: Int): String {
        return siteUrl + URL_PREFIX_PARAM + "project/client/preview/" + UrlEncodeDecoder.encode(accountId)
    }

    @JvmStatic fun generatePagesLink(projectId: Int, folderPath: String): String {
        return "project/page/list/" + GenericLinkUtils.encodeParam(projectId, folderPath)
    }

    @JvmStatic fun generatePageAdd(projectId: Int, pagePath: String): String {
        return "project/page/add/" + GenericLinkUtils.encodeParam(projectId, pagePath)
    }

    @JvmStatic fun generatePageRead(projectId: Int, pagePath: String): String {
        return "project/page/preview/" + GenericLinkUtils.encodeParam(projectId, pagePath)
    }

    @JvmStatic fun generatePageEdit(projectId: Int, pagePath: String): String {
        return "project/page/edit/" + GenericLinkUtils.encodeParam(projectId, pagePath)
    }

    @JvmStatic fun generateProjectMemberFullLink(siteUrl: String, projectId: Int, memberName: String?): String {
        return if (memberName == null) {
            ""
        } else siteUrl + URL_PREFIX_PARAM + "project/user/preview/" + GenericLinkUtils.encodeParam(projectId, memberName)
    }

    @JvmStatic fun generateProjectMemberLink(projectId: Int?, memberName: String?): String {
        return "project/user/preview/" + GenericLinkUtils.encodeParam(projectId, memberName)
    }

    @JvmStatic fun generateRisksLink(projectId: Int): String {
        return "project/risk/list/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateRiskPreviewLink(projectId: Int, riskId: Int?): String {
        return "project/risk/preview/" + GenericLinkUtils.encodeParam(projectId, riskId)
    }

    @JvmStatic fun generateRiskPreviewFullLink(siteUrl: String, projectId: Int, riskId: Int?): String {
        return siteUrl + URL_PREFIX_PARAM + generateRiskPreviewLink(projectId, riskId)
    }

    @JvmStatic fun generateRiskEditLink(projectId: Int?, riskId: Int?): String {
        return "project/risk/edit/" + UrlEncodeDecoder.encode(projectId.toString() + "/" + riskId)
    }

    @JvmStatic fun generateRiskAddLink(projectId: Int): String {
        return "project/risk/add/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateMessageAddLink(projectId: Int): String {
        return "project/message/add/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateMessagesLink(projectId: Int): String {
        return "project/message/list/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateMessagePreviewLink(projectId: Int, messageId: Int?): String {
        return "project/message/preview/" + GenericLinkUtils.encodeParam(projectId, messageId)
    }

    @JvmStatic fun generateMessagePreviewFullLink(siteUrl: String, projectId: Int, messageId: Int?): String {
        return siteUrl + URL_PREFIX_PARAM + generateMessagePreviewLink(projectId, messageId)
    }

    @JvmStatic fun generateBugComponentPreviewLink(projectId: Int, componentId: Int?): String {
        return "project/component/preview/" + GenericLinkUtils.encodeParam(projectId, componentId)
    }

    @JvmStatic fun generateBugComponentPreviewFullLink(siteUrl: String, projectId: Int, componentId: Int?): String {
        return siteUrl + URL_PREFIX_PARAM + generateBugComponentPreviewLink(projectId, componentId)
    }

    @JvmStatic fun generateBugVersionPreviewLink(projectId: Int, versionId: Int): String {
        return "project/version/preview/" + GenericLinkUtils.encodeParam(projectId, versionId)
    }

    @JvmStatic  fun generateBugVersionPreviewFullLink(siteUrl: String, projectId: Int, versionId: Int): String {
        return siteUrl + URL_PREFIX_PARAM + generateBugVersionPreviewLink(projectId, versionId)
    }

    @JvmStatic fun generateBugPreviewLink(bugKey: Int?, prjShortName: String): String {
        return String.format("project/bug/preview/%s-%d", prjShortName, bugKey)
    }

    @JvmStatic fun generateBugEditLink(bugKey: Int?, prjShortName: String): String {
        return String.format("project/bug/edit/%s-%d", prjShortName, bugKey)
    }

    @JvmStatic fun generateBugsLink(projectId: Int): String {
        return "project/bug/list/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateBugPreviewFullLink(siteUrl: String, bugKey: Int?, prjShortName: String): String {
        return siteUrl + URL_PREFIX_PARAM + generateBugPreviewLink(bugKey, prjShortName)
    }

    @JvmStatic fun generateFileDashboardLink(projectId: Int): String {
        return "project/file/dashboard/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateTimeReportLink(projectId: Int): String {
        return "project/time/list/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateInvoiceListLink(projectId: Int): String {
        return "project/invoice/list/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateRolePreviewLink(projectId: Int, roleId: Int?): String {
        return "project/role/preview/" + GenericLinkUtils.encodeParam(projectId, roleId)
    }

    @JvmStatic fun generateRolePreviewFullLink(siteUrl: String, projectId: Int, roleId: Int?): String {
        return siteUrl + URL_PREFIX_PARAM + generateRolePreviewLink(projectId, roleId)
    }

    @JvmStatic fun generateStandupDashboardLink(): String {
        return "project/reports/standup/list/"
    }

    @JvmStatic fun generateHoursWeeklyReportLink(): String {
        return "project/reports/weeklytiming/"
    }

    @JvmStatic fun generateTimesheetReportLink(): String {
        return "project/reports/timesheet/"
    }

    @JvmStatic fun generateUsersWorkloadReportLink(): String {
        return "project/reports/usersworkload/"
    }

    @JvmStatic fun generateUsersLink(projectId: Int): String {
        return "project/user/list/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateProjectSettingLink(projectId: Int?): String {
        return "project/setting/" + UrlEncodeDecoder.encode(projectId)
    }

    @JvmStatic fun generateProjectSettingFullLink(siteUrl: String, projectId: Int?): String {
        return siteUrl + URL_PREFIX_PARAM + generateProjectSettingLink(projectId)
    }

    @JvmStatic fun generateProjectItemLink(prjShortName: String, projectId: Int, type: String, typeId: String?): String {
        var result = ""

        if (typeId == null || StringUtils.isBlank(typeId) || "null" == typeId) {
            return ""
        }

        try {
            when (type) {
                ProjectTypeConstants.PROJECT -> {}
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
            throw DebugException(String.format("Error generate tooltip%d---%s---%s", projectId, type, typeId), e)
        }

        return "#" + result
    }
}
