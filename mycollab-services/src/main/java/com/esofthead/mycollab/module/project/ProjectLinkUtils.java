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

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectLinkUtils {

	public static String generateTaskGroupPreviewLink(Integer projectId,
			Integer taskgroupId) {
		return "project/task/taskgroup/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						taskgroupId });
	}

	public static String generateTaskPreviewLink(Integer projectId,
			Integer taskId) {
		return "project/task/task/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, taskId });
	}

	public static String generateMilestonePreviewLink(int projectId,
			int milestoneId) {
		return "project/milestone/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						milestoneId });
	}

	public static String generateProblemPreviewLink(Integer projectId,
			Integer problemId) {
		return "project/problem/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						problemId });
	}

	public static String generateRiskPreview(Integer projectId, Integer riskId) {
		return "project/risk/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, riskId });
	}

	public static String generateMessagePreviewLink(Integer projectId,
			Integer messageId) {
		if (messageId == null) {
			return "";
		}
		return "project/message/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						messageId });
	}

	public static String generateBugComponentPreviewLink(Integer projectId,
			Integer componentId) {
		if (componentId == null) {
			return "";
		}
		return "project/bug/component/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						componentId });
	}

	public static String generateBugVersionPreviewLink(Integer projectId,
			Integer versionId) {
		if (versionId == null) {
			return "";
		}
		return "project/bug/version/preview/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId,
						versionId });
	}

	public static String generateProjectLink(int projectId) {
		return "project/dashboard/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId });
	}

	public static String generateBugPreviewLink(int projectId, int bugId) {
		return "project/bug/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, bugId });
	}

	public static String generateRolePreviewLink(int projectId, int roleId) {
		return "project/role/preview/"
				+ GenericLinkUtils
						.encodeParam(new Object[] { projectId, roleId });
	}

	public static String generateStandUpPreviewLink(int projectId, int reportId) {
		return "project/standup/list/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId });
	}
	
	public static String generateTimeTrackingPreviewLink(int projectId, int timeId) {
		return "project/time/list/"
				+ GenericLinkUtils.encodeParam(new Object[] { projectId, timeId  });
	}
}
