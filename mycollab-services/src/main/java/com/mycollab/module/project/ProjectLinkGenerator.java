/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project;

import com.mycollab.common.GenericLinkUtils;
import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.core.DebugException;
import com.mycollab.core.utils.StringUtils;

import static com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectLinkGenerator {

    public static String generateProjectLink(Integer projectId) {
        return "project/dashboard/" + GenericLinkUtils.encodeParam(projectId);
    }

    public static String generateProjectFullLink(String siteUrl, Integer projectId) {
        return siteUrl + URL_PREFIX_PARAM + generateProjectLink(projectId);
    }

    public static String generateTicketDashboardLink(Integer projectId) {
        return "project/ticket/dashboard/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateTaskPreviewLink(Integer taskkey, String prjShortName) {
        return String.format("project/task/preview/%s-%d", prjShortName, taskkey);
    }

    public static String generateTaskPreviewFullLink(String siteUrl, Integer taskKey, String prjShortName) {
        return siteUrl + URL_PREFIX_PARAM + generateTaskPreviewLink(taskKey, prjShortName);
    }

    public static String generateTaskEditLink(Integer taskKey, String prjShortName) {
        return String.format("project/task/edit/%s-%d", prjShortName, taskKey);
    }

    public static String generateMilestonesLink(Integer projectId) {
        return "project/milestone/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateMilestonePreviewLink(Integer projectId, Integer milestoneId) {
        return "project/milestone/preview/" + GenericLinkUtils.encodeParam(projectId, milestoneId);
    }

    public static String generateMilestonePreviewFullLink(String siteUrl, Integer projectId, Integer milestoneId) {
        return siteUrl + URL_PREFIX_PARAM + generateMilestonePreviewLink(projectId, milestoneId);
    }

    public static String generateClientPreviewLink(Integer accountId) {
        return "project/client/preview/" + UrlEncodeDecoder.encode(accountId);
    }

    public static String generateClientPreviewFullLink(String siteUrl, Integer accountId) {
        return siteUrl + URL_PREFIX_PARAM + "project/client/preview/" + UrlEncodeDecoder.encode(accountId);
    }

    public static String generatePagesLink(Integer projectId, String folderPath) {
        return "project/page/list/" + GenericLinkUtils.encodeParam(projectId, folderPath);
    }

    public static String generatePageAdd(Integer projectId, String pagePath) {
        return "project/page/add/" + GenericLinkUtils.encodeParam(projectId, pagePath);
    }

    public static String generatePageRead(Integer projectId, String pagePath) {
        return "project/page/preview/" + GenericLinkUtils.encodeParam(projectId, pagePath);
    }

    public static String generatePageEdit(Integer projectId, String pagePath) {
        return "project/page/edit/" + GenericLinkUtils.encodeParam(projectId, pagePath);
    }

    public static String generateProjectMemberFullLink(String siteUrl, Integer projectId, String memberName) {
        if (memberName == null) {
            return "";
        }
        return siteUrl + URL_PREFIX_PARAM + "project/user/preview/" + GenericLinkUtils.encodeParam(projectId, memberName);
    }

    public static String generateProjectMemberLink(Integer projectId, String memberName) {
        return "project/user/preview/" + GenericLinkUtils.encodeParam(projectId, memberName);
    }

    public static String generateRisksLink(Integer projectId) {
        return "project/risk/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateRiskPreviewLink(Integer projectId, Integer riskId) {
        return "project/risk/preview/" + GenericLinkUtils.encodeParam(projectId, riskId);
    }

    public static String generateRiskPreviewFullLink(String siteUrl, Integer projectId, Integer riskId) {
        return siteUrl + URL_PREFIX_PARAM + generateRiskPreviewLink(projectId, riskId);
    }

    public static String generateRiskEditLink(Integer projectId, Integer riskId) {
        return "project/risk/edit/" + UrlEncodeDecoder.encode(projectId + "/" + riskId);
    }

    public static String generateRiskAddLink(Integer projectId) {
        return "project/risk/add/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateMessageAddLink(Integer projectId) {
        return "project/message/add/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateMessagesLink(Integer projectId) {
        return "project/message/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateMessagePreviewLink(Integer projectId, Integer messageId) {
        return "project/message/preview/" + GenericLinkUtils.encodeParam(projectId, messageId);
    }

    public static String generateMessagePreviewFullLink(String siteUrl, Integer projectId, Integer messageId) {
        return siteUrl + URL_PREFIX_PARAM + generateMessagePreviewLink(projectId, messageId);
    }

    public static String generateBugComponentPreviewLink(Integer projectId, Integer componentId) {
        return "project/component/preview/" + GenericLinkUtils.encodeParam(projectId, componentId);
    }

    public static String generateBugComponentPreviewFullLink(String siteUrl, Integer projectId, Integer componentId) {
        return siteUrl + URL_PREFIX_PARAM + generateBugComponentPreviewLink(projectId, componentId);
    }

    public static String generateBugVersionPreviewLink(Integer projectId, Integer versionId) {
        return "project/version/preview/" + GenericLinkUtils.encodeParam(projectId, versionId);
    }

    public static String generateBugVersionPreviewFullLink(String siteUrl, Integer projectId, Integer versionId) {
        return siteUrl + URL_PREFIX_PARAM + generateBugVersionPreviewLink(projectId, versionId);
    }

    public static String generateBugPreviewLink(Integer bugKey, String prjShortname) {
        return String.format("project/bug/preview/%s-%d", prjShortname, bugKey);
    }

    public static String generateBugEditLink(Integer bugkey, String prjShortname) {
        return String.format("project/bug/edit/%s-%d", prjShortname, bugkey);
    }

    public static String generateBugsLink(Integer projectId) {
        return "project/bug/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateBugPreviewFullLink(String siteUrl, Integer bugKey, String prjShortname) {
        return siteUrl + URL_PREFIX_PARAM + generateBugPreviewLink(bugKey, prjShortname);
    }

    public static String generateFileDashboardLink(Integer projectId) {
        return "project/file/dashboard/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateTimeReportLink(Integer projectId) {
        return "project/time/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateInvoiceListLink(Integer projectId) {
        return "project/invoice/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateRolePreviewLink(Integer projectId, Integer roleId) {
        return "project/role/preview/" + GenericLinkUtils.encodeParam(projectId, roleId);
    }

    public static String generateRolePreviewFullLink(String siteUrl, Integer projectId, Integer roleId) {
        return siteUrl + URL_PREFIX_PARAM + generateRolePreviewLink(projectId, roleId);
    }

    public static String generateStandupDashboardLink() {
        return "project/reports/standup/list/";
    }

    public static String generateHoursWeeklyReportLink() {
        return "project/reports/weeklytiming/";
    }

    public static String generateTimesheetReportLink() {
        return "project/reports/timesheet/";
    }

    public static String generateUsersWorkloadReportLink() {
        return "project/reports/usersworkload/";
    }

    public static String generateUsersLink(Integer projectId) {
        return "project/user/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateProjectSettingLink(Integer projectId) {
        return "project/setting/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateProjectSettingFullLink(String siteUrl, Integer projectId) {
        return siteUrl + URL_PREFIX_PARAM + generateProjectSettingLink(projectId);
    }

    public static String generateProjectItemLink(String prjShortName, Integer projectId, String type, String typeId) {
        String result = "";

        if (typeId == null || StringUtils.isBlank(typeId) || "null".equals(typeId)) {
            return "";
        }

        try {
            if (ProjectTypeConstants.PROJECT.equals(type)) {
            } else if (ProjectTypeConstants.MESSAGE.equals(type)) {
                result = generateMessagePreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.MILESTONE.equals(type)) {
                result = generateMilestonePreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.RISK.equals(type)) {
                result = generateRiskPreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.TASK.equals(type)) {
                result = generateTaskPreviewLink(Integer.parseInt(typeId), prjShortName);
            } else if (ProjectTypeConstants.BUG.equals(type)) {
                result = generateBugPreviewLink(Integer.parseInt(typeId), prjShortName);
            } else if (ProjectTypeConstants.BUG_COMPONENT.equals(type)) {
                result = generateBugComponentPreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
                result = generateBugVersionPreviewLink(projectId, Integer.parseInt(typeId));
            } else if (ProjectTypeConstants.PAGE.equals(type)) {
                result = generatePageRead(projectId, typeId);
            }
        } catch (Exception e) {
            throw new DebugException(String.format("Error generate tooltip%d---%s---%s", projectId, type, typeId), e);
        }

        return "#" + result;
    }
}
