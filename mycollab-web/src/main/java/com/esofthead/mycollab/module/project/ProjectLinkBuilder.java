/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Text;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectLinkBuilder {

	public static String generateProjectFullLink(Integer projectId,
			String prefixParam) {
		if (projectId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + prefixParam + "project/dashboard/"
				+ UrlEncodeDecoder.encode(projectId);
	}

	public static String generateProjectMemberFullLink(int projectId,
			String memberName) {
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ "project/user/preview/"
				+ UrlEncodeDecoder.encode(projectId + "/" + memberName);
	}

	public static String generateProjectMemberHtmlLink(String username,
			int projectId) {
		ProjectMemberService projectMemberService = ApplicationContextUtil
				.getSpringBean(ProjectMemberService.class);
		SimpleProjectMember member = projectMemberService.findMemberByUsername(
				username, projectId, AppContext.getAccountId());
		if (member != null) {
			A link = new A();
			link.setHref(generateProjectMemberFullLink(projectId,
					member.getUsername()));
			Text text = new Text(member.getDisplayName());
			link.appendChild(text);
			return link.write();
		} else {
			return null;
		}
	}

	public static String generateBugPreviewFullLink(Integer projectId,
			Integer bugId) {
		if (projectId == null || bugId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateBugPreviewLink(projectId, bugId);
	}

	public static String generateMessagePreviewFullLink(Integer projectId,
			Integer messageId, String prefixParam) {
		if (projectId == null || messageId == null) {
			return "";
		}
		return AppContext.getSiteUrl()
				+ prefixParam
				+ ProjectLinkUtils.generateMessagePreviewLink(projectId,
						messageId);
	}

	public static String generateRiskPreviewFullLink(Integer projectId,
			Integer riskId) {
		if (projectId == null || riskId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ "project/risk/preview/"
				+ UrlEncodeDecoder.encode(projectId + "/" + riskId);
	}

	public static String generateTaskPreviewFullLink(Integer projectId,
			Integer taskId) {
		if (projectId == null || taskId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateTaskPreviewLink(projectId, taskId);
	}

	public static String generateTaskGroupPreviewFullLink(Integer projectId,
			Integer taskgroupId) {
		if (projectId == null || taskgroupId == null) {
			return "";
		}
		return AppContext.getSiteUrl()
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateTaskGroupPreviewLink(projectId,
						taskgroupId);
	}
	
	public static String generateTaskGroupHtmlLink(int taskgroupId) {
		ProjectTaskListService taskListService = ApplicationContextUtil
				.getSpringBean(ProjectTaskListService.class);
		SimpleTaskList taskList = taskListService.findById(taskgroupId,
				AppContext.getAccountId());
		if (taskList != null) {
			A link = new A();
			link.setHref(generateTaskGroupPreviewFullLink(
					taskList.getProjectid(), taskList.getId()));
			Text text = new Text(taskList.getName());
			link.appendChild(text);
			return link.write();
		} else {
			return null;
		}
	}

	public static String generateMilestonePreviewFullLink(Integer projectId,
			Integer milestoneId) {
		if (projectId == null || milestoneId == null) {
			return "";
		}
		return AppContext.getSiteUrl()
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ ProjectLinkUtils.generateMilestonePreviewLink(projectId,
						milestoneId);
	}
	
	public static String generateMilestoneHtmlLink(int milestoneId) {
		MilestoneService milestoneService = ApplicationContextUtil
				.getSpringBean(MilestoneService.class);
		SimpleMilestone milestone = milestoneService.findById(milestoneId,
				AppContext.getAccountId());
		if (milestone != null) {
			A link = new A();
			link.setHref(generateMilestonePreviewFullLink(
					milestone.getProjectid(), milestone.getId()));
			Text text = new Text(milestone.getName());
			link.appendChild(text);
			return link.write();
		} else {
			return null;
		}
	}

	public static String generateProjectItemLink(int projectId, String type,
			int typeid) {
		String result = "";

		if (ProjectTypeConstants.PROJECT.equals(type)) {
		} else if (ProjectTypeConstants.MESSAGE.equals(type)) {
			result = ProjectLinkUtils.generateMessagePreviewLink(projectId,
					typeid);
		} else if (ProjectTypeConstants.MILESTONE.equals(type)) {
			result = ProjectLinkUtils.generateMilestonePreviewLink(projectId,
					typeid);
		} else if (ProjectTypeConstants.PROBLEM.equals(type)) {
			result = ProjectLinkUtils.generateProblemPreviewLink(projectId,
					typeid);
		} else if (ProjectTypeConstants.RISK.equals(type)) {
			result = ProjectLinkUtils.generateRiskPreview(projectId, typeid);
		} else if (ProjectTypeConstants.TASK.equals(type)) {
			result = ProjectLinkUtils
					.generateTaskPreviewLink(projectId, typeid);
		} else if (ProjectTypeConstants.TASK_LIST.equals(type)) {
			result = ProjectLinkUtils.generateTaskGroupPreviewLink(projectId,
					typeid);
		} else if (ProjectTypeConstants.BUG.equals(type)) {
			result = ProjectLinkUtils.generateBugPreviewLink(projectId, typeid);
		} else if (ProjectTypeConstants.BUG_COMPONENT.equals(type)) {
			result = ProjectLinkUtils.generateBugComponentPreviewLink(
					projectId, typeid);
		} else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
			result = ProjectLinkUtils.generateBugVersionPreviewLink(projectId,
					typeid);
		} else if (ProjectTypeConstants.STANDUP.equals(type)) {
			result = ProjectLinkUtils.generateStandUpPreviewLink(projectId,
					typeid);
		}

		return "#" + result;
	}
}
