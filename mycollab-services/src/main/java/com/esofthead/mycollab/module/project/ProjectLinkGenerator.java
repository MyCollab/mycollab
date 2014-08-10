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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectLinkGenerator {

	public static String generateProjectLink(int projectId) {
		return "project/dashboard/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId });
	}

	public static String generateProjectFullLink(String siteUrl,
			Integer projectId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateProjectLink(projectId);
	}

	public static String generateTaskGroupPreviewLink(Integer projectId,
			Integer taskgroupId) {
		return "project/task/taskgroup/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						taskgroupId });
	}

	public static String generateTaskGroupPreviewFullLink(String siteUrl,
			Integer projectId, Integer taskgroupId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateTaskGroupPreviewLink(projectId, taskgroupId);
	}

	public static String generateTaskDashboardLink(Integer projectId) {
		return "project/task/dashboard/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateTaskPreviewLink(Integer projectId,
			Integer taskId) {
		return "project/task/task/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, taskId });
	}

	public static String generateTaskPreviewFullLink(String siteUrl,
			Integer projectId, Integer taskId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateTaskPreviewLink(projectId, taskId);
	}

	public static String generateMilestonesLink(int projectId) {
		return "project/milestone/list/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateMilestonePreviewLink(int projectId,
			int milestoneId) {
		return "project/milestone/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						milestoneId });
	}

	public static String generateMilestonePreviewFullLink(String siteUrl,
			int projectId, int milestoneId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateMilestonePreviewLink(projectId, milestoneId);
	}

	public static String generatePagesLink(int projectId) {
		return "project/page/list/" + UrlEncodeDecoder.encode(projectId);
	}
	
	public static String generatePageAdd(int projectId) {
		return "project/page/add/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateProblemsLink(Integer projectId) {
		return "project/problem/list/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateProblemPreviewLink(Integer projectId,
			Integer problemId) {
		return "project/problem/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						problemId });
	}

	public static String generateProblemPreviewFullLink(String siteUrl,
			Integer projectId, Integer problemId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateProblemPreviewLink(projectId, problemId);
	}

	public static String generateProjectMemberFullLink(String siteUrl,
			int projectId, String memberName) {
		if (memberName == null) {
			return "";
		}
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ "project/user/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						memberName });
	}

	public static String generateRisksLink(Integer projectId) {
		return "project/risk/list/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateRiskPreviewLink(Integer projectId,
			Integer riskId) {
		return "project/risk/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, riskId });
	}

	public static String generateRiskPreviewFullLink(String siteUrl,
			Integer projectId, Integer riskId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateRiskPreviewLink(projectId, riskId);
	}

	public static String generateRiskEditLink(Integer projectId, Integer riskId) {
		return "project/risk/edit/"
				+ UrlEncodeDecoder.encode(projectId + "/" + riskId);
	}

	public static String generateRiskAddLink(Integer projectId) {
		return "project/risk/add/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateMessagesLink(Integer projectId) {
		return "project/message/list/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateMessagePreviewLink(Integer projectId,
			Integer messageId) {
		return "project/message/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						messageId });
	}

	public static String generateMessagePreviewFullLink(String siteUrl,
			Integer projectId, Integer messageId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateMessagePreviewLink(projectId, messageId);
	}

	public static String generateBugComponentPreviewLink(Integer projectId,
			Integer componentId) {
		return "project/bug/component/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						componentId });
	}

	public static String generateBugComponentPreviewFullLink(String siteUrl,
			Integer projectId, Integer componentId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateBugComponentPreviewLink(projectId, componentId);
	}

	public static String generateBugVersionPreviewLink(Integer projectId,
			Integer versionId) {
		return "project/bug/version/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						versionId });
	}

	public static String generateBugVersionPreviewFullLink(String siteUrl,
			Integer projectId, Integer versionId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateBugVersionPreviewLink(projectId, versionId);
	}

	public static String generateBugDashboardLink(int projectId) {
		return "project/bug/dashboard/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateBugPreviewLink(int projectId, int bugId) {
		return "project/bug/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, bugId });
	}

	public static String generateBugPreviewFullLink(String siteUrl,
			int projectId, int bugId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ generateBugPreviewLink(projectId, bugId);
	}

	public static String generateFileDashboardLink(int projectId) {
		return "project/file/dashboard/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateTimeReportLink(int projectId) {
		return "project/time/list/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateRolePreviewLink(int projectId, int roleId) {
		return "project/role/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, roleId });
	}

	public static String generateRolePreviewFullLink(String siteUrl,
			int projectId, int roleId) {
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ "project/role/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, roleId });
	}

	public static String generateStandUpPreviewLink(int projectId, int reportId) {
		return "project/standup/list/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId });
	}

	public static String generateTimeTrackingPreviewLink(int projectId,
			int timeId) {
		return "project/time/list/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, timeId });
	}

	public static String generateStandupDashboardLink(int projectId) {
		return "project/standup/list/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateUsersLink(int projectId) {
		return "project/user/list/" + UrlEncodeDecoder.encode(projectId);
	}

	public static String generateDenyInvitationParams(String inviteeEmail,
			int sAccountId, int projectId, String inviteUserEmail,
			String inviteUsername) {
		return UrlEncodeDecoder.encode(inviteeEmail + "/" + sAccountId + "/"
				+ projectId + "/" + inviteUserEmail + "/" + inviteUsername);
	}

	public static String generateAcceptInvitationParams(String inviteeEmail,
			int sAccountId, int projectId, int projectRoleId,
			String inviteUserEmail, String inviteUsername, Date currentDate) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String formatDate = format.format(currentDate);
		return UrlEncodeDecoder.encode(inviteeEmail + "/" + sAccountId + "/"
				+ projectId + "/" + projectRoleId + "/" + inviteUserEmail + "/"
				+ inviteUsername + "/" + formatDate);
	}
}
