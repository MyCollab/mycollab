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
package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectLinkGenerator {

    public static String generateProjectLink(Integer projectId) {
        return "project/dashboard/" + GenericLinkUtils.encodeParam(projectId);
    }

    public static String generateProjectFullLink(String siteUrl, Integer projectId) {
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateProjectLink(projectId);
    }

    public static String generateTaskDashboardLink(Integer projectId) {
        return "project/task/dashboard/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateTaskPreviewLink(Integer taskkey, String prjShortName) {
        return String.format("project/task/preview/%s-%d", prjShortName, taskkey);
    }

    public static String generateTaskPreviewFullLink(String siteUrl, Integer taskKey, String prjShortName) {
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateTaskPreviewLink(taskKey, prjShortName);
    }

    public static String generateTaskEditLink(Integer taskkey, String prjShortName) {
        return String.format("project/task/edit/%s-%d", prjShortName, taskkey);
    }

    public static String generateMilestonesLink(Integer projectId) {
        return "project/milestone/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateMilestonePreviewLink(Integer projectId, Integer milestoneId) {
        return "project/milestone/preview/" + GenericLinkUtils.encodeParam(projectId, milestoneId);
    }

    public static String generateMilestonePreviewFullLink(String siteUrl, Integer projectId, Integer milestoneId) {
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateMilestonePreviewLink(projectId, milestoneId);
    }

    public static String generateClientPreviewLink(Integer accountId) {
        return "project/client/preview/" + UrlEncodeDecoder.encode(accountId);
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

    public static String generateProblemsLink(Integer projectId) {
        return "project/problem/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateProjectMemberFullLink(String siteUrl, Integer projectId, String memberName) {
        if (memberName == null) {
            return "";
        }
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + "project/user/preview/" + GenericLinkUtils.encodeParam(projectId, memberName);
    }

    public static String generateRisksLink(Integer projectId) {
        return "project/risk/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateRiskPreviewLink(Integer projectId, Integer riskId) {
        return "project/risk/preview/" + GenericLinkUtils.encodeParam(projectId, riskId);
    }

    public static String generateRiskPreviewFullLink(String siteUrl, Integer projectId, Integer riskId) {
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateRiskPreviewLink(projectId, riskId);
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
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateMessagePreviewLink(projectId, messageId);
    }

    public static String generateBugComponentPreviewLink(Integer projectId, Integer componentId) {
        return "project/component/preview/" + GenericLinkUtils.encodeParam(projectId, componentId);
    }

    public static String generateBugComponentPreviewFullLink(String siteUrl, Integer projectId, Integer componentId) {
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateBugComponentPreviewLink(projectId, componentId);
    }

    public static String generateBugVersionPreviewLink(Integer projectId, Integer versionId) {
        return "project/version/preview/" + GenericLinkUtils.encodeParam(projectId, versionId);
    }

    public static String generateBugVersionPreviewFullLink(String siteUrl, Integer projectId, Integer versionId) {
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateBugVersionPreviewLink(projectId, versionId);
    }

    public static String generateBugPreviewLink(Integer bugKey, String prjShortname) {
        return String.format("project/bug/preview/%s-%d", prjShortname, bugKey);
    }

    public static String generateBugEditLink(Integer bugkey, String prjShortname) {
        return String.format("project/bug/edit/%s-%d", prjShortname, bugkey);
    }

    public static String generateBugPreviewFullLink(String siteUrl, Integer bugKey, String prjShortname) {
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + generateBugPreviewLink(bugKey, prjShortname);
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
        return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM + "project/role/preview/" + GenericLinkUtils.encodeParam(projectId, roleId);
    }

    public static String generateTimeTrackingPreviewLink(Integer projectId, Integer timeId) {
        return "project/time/list/" + GenericLinkUtils.encodeParam(projectId, timeId);
    }

    public static String generateStandUpPreviewLink(Integer projectId, Integer reportId) {
        return "project/standup/list/" + GenericLinkUtils.encodeParam(projectId);
    }

    public static String generateStandupDashboardLink(Integer projectId) {
        return "project/reports/standup/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateProjectCalendarLink(Integer projectId) {
        return "project/calendar/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateUsersLink(Integer projectId) {
        return "project/user/list/" + UrlEncodeDecoder.encode(projectId);
    }

    public static String generateDenyInvitationParams(String inviteeEmail, Integer sAccountId, Integer projectId,
                                                      String inviteUserEmail, String inviteUsername) {
        return UrlEncodeDecoder.encode(inviteeEmail + "/" + sAccountId + "/" + projectId + "/" + inviteUserEmail + "/" + inviteUsername);
    }

    public static String generateAcceptInvitationParams(String inviteeEmail, Integer sAccountId, Integer projectId,
                                                        Integer projectRoleId, String inviteUserEmail, String inviteUsername,
                                                        Date currentDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        String formatDate = format.format(currentDate);
        return UrlEncodeDecoder.encode(inviteeEmail + "/" + sAccountId + "/"
                + projectId + "/" + projectRoleId + "/" + inviteUserEmail + "/" + inviteUsername + "/" + formatDate);
    }
}
