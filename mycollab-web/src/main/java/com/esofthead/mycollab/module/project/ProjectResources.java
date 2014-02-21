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

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.module.project.view.bug.BugPriorityStatusConstants;
import com.esofthead.mycollab.module.project.view.bug.BugSeverityConstants;
import com.esofthead.mycollab.module.project.view.milestone.MilestoneStatusConstant;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.server.Resource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectResources {

	private static final Map<String, String> resourceLinks;

	private static final Map<String, Resource> resources;

	static {
		resourceLinks = new HashMap<String, String>();
		resourceLinks.put(ProjectContants.PROJECT, MyCollabResource
				.newResourceLink("icons/16/project/project.png"));
		resourceLinks.put(ProjectContants.MESSAGE, MyCollabResource
				.newResourceLink("icons/16/project/message.png"));
		resourceLinks.put(ProjectContants.MILESTONE, MyCollabResource
				.newResourceLink("icons/16/project/milestone.png"));
		resourceLinks.put(ProjectContants.PROBLEM, MyCollabResource
				.newResourceLink("icons/16/project/problem.png"));
		resourceLinks.put(ProjectContants.RISK,
				MyCollabResource.newResourceLink("icons/16/project/risk.png"));
		resourceLinks.put(ProjectContants.TASK,
				MyCollabResource.newResourceLink("icons/16/project/task.png"));
		resourceLinks.put(ProjectContants.TASK_LIST, MyCollabResource
				.newResourceLink("icons/16/project/task_group.png"));
		resourceLinks.put(ProjectContants.BUG,
				MyCollabResource.newResourceLink("icons/16/project/bug.png"));
		resourceLinks.put(ProjectContants.BUG_COMPONENT, MyCollabResource
				.newResourceLink("icons/16/project/component.png"));
		resourceLinks.put(ProjectContants.BUG_VERSION, MyCollabResource
				.newResourceLink("icons/16/project/version.png"));
		resourceLinks.put(ProjectContants.STANDUP, MyCollabResource
				.newResourceLink("icons/16/project/standup.png"));

		resources = new HashMap<String, Resource>();
		resources.put(ProjectContants.PROJECT,
				MyCollabResource.newResource("icons/16/project/project.png"));
		resources.put(ProjectContants.MESSAGE,
				MyCollabResource.newResource("icons/16/project/message.png"));
		resources.put(ProjectContants.MILESTONE,
				MyCollabResource.newResource("icons/16/project/milestone.png"));
		resources.put(ProjectContants.PROBLEM,
				MyCollabResource.newResource("icons/16/project/problem.png"));
		resources.put(ProjectContants.RISK,
				MyCollabResource.newResource("icons/16/project/risk.png"));
		resources.put(ProjectContants.TASK,
				MyCollabResource.newResource("icons/16/project/task.png"));
		resources
				.put(ProjectContants.TASK_LIST, MyCollabResource
						.newResource("icons/16/project/task_group.png"));
		resources.put(ProjectContants.BUG,
				MyCollabResource.newResource("icons/16/project/bug.png"));
		resources.put(ProjectContants.BUG_COMPONENT,
				MyCollabResource.newResource("icons/16/project/component.png"));
		resources.put(ProjectContants.BUG_VERSION,
				MyCollabResource.newResource("icons/16/project/version.png"));
		resources.put(ProjectContants.STANDUP,
				MyCollabResource.newResource("icons/16/project/standup.png"));
	}

	public static String getResourceLink(String type) {
		return resourceLinks.get(type);
	}

	public static Resource getResource(String type) {
		return resources.get(type);
	}

	public static Resource getIconResource12ByPhase(String phase) {
		Resource iconseverity = MyCollabResource
				.newResource(MilestoneStatusConstant.INPROGRESS_IMG_12);

		if (MilestoneStatusConstant.IN_PROGRESS.equals(phase)) {
			iconseverity = MyCollabResource
					.newResource(MilestoneStatusConstant.INPROGRESS_IMG_12);
		} else if (MilestoneStatusConstant.FUTURE.equals(phase)) {
			iconseverity = MyCollabResource
					.newResource(MilestoneStatusConstant.FUTURE_IMG_12);
		} else if (MilestoneStatusConstant.CLOSED.equals(phase)) {
			iconseverity = MyCollabResource
					.newResource(MilestoneStatusConstant.CLOSED_IMG_12);
		}

		return iconseverity;
	}

	public static Resource getIconResource12BySeverity(String severity) {
		Resource iconseverity = MyCollabResource
				.newResource(BugSeverityConstants.MINOR_IMG_12);
		
		if (BugSeverityConstants.CRITICAL.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.CRITICAL_IMG_12);
		} else if (BugSeverityConstants.MAJOR.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.MAJOR_IMG_12);
		} else if (BugSeverityConstants.MINOR.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.MINOR_IMG_12);
		} else if (BugSeverityConstants.TRIVIAL.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.TRIVIAL_IMG_12);
		}
		return iconseverity;
	}

	public static Resource getIconResource16BySeverity(String severity) {
		Resource iconseverity = MyCollabResource
				.newResource(BugSeverityConstants.MINOR_IMG_16);
		if (BugSeverityConstants.CRITICAL.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.CRITICAL_IMG_16);
		} else if (BugSeverityConstants.MAJOR.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.MAJOR_IMG_16);
		} else if (BugSeverityConstants.MINOR.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.MINOR_IMG_16);
		} else if (BugSeverityConstants.TRIVIAL.equals(severity)) {
			iconseverity = MyCollabResource
					.newResource(BugSeverityConstants.TRIVIAL_IMG_16);
		}
		return iconseverity;
	}

	public static Resource getIconResource12ByPriority(String priority) {
		Resource iconPriority = MyCollabResource
				.newResource(BugPriorityStatusConstants.PRIORITY_MAJOR_IMG_12);

		if (BugPriorityStatusConstants.PRIORITY_BLOCKER.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_BLOCKER_IMG_12);
		} else if (BugPriorityStatusConstants.PRIORITY_CRITICAL
				.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_CRITICAL_IMG_12);
		} else if (BugPriorityStatusConstants.PRIORITY_MAJOR.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_MAJOR_IMG_12);
		} else if (BugPriorityStatusConstants.PRIORITY_MINOR.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_MINOR_IMG_12);
		} else if (BugPriorityStatusConstants.PRIORITY_TRIVIAL.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_TRIVIAL_IMG_12);
		}
		return iconPriority;
	}

	public static Resource getIconResource16ByPriority(String priority) {
		Resource iconPriority = MyCollabResource
				.newResource(BugPriorityStatusConstants.PRIORITY_MAJOR_IMG_16);
		if (BugPriorityStatusConstants.PRIORITY_BLOCKER.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_BLOCKER_IMG_16);
		} else if (BugPriorityStatusConstants.PRIORITY_CRITICAL
				.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_CRITICAL_IMG_16);
		} else if (BugPriorityStatusConstants.PRIORITY_MAJOR.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_MAJOR_IMG_16);
		} else if (BugPriorityStatusConstants.PRIORITY_MINOR.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_MINOR_IMG_16);
		} else if (BugPriorityStatusConstants.PRIORITY_TRIVIAL.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(BugPriorityStatusConstants.PRIORITY_TRIVIAL_IMG_16);
		}
		return iconPriority;
	}
}
