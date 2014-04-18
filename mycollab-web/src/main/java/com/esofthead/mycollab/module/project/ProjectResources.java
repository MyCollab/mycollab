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

	public static final String T_PRIORITY_HIGHT_IMG = "icons/12/priority_high.png";
	public static final String T_PRIORITY_LOW_IMG = "icons/12/priority_low.png";
	public static final String T_PRIORITY_MEDIUM_IMG = "icons/12/priority_medium.png";
	public static final String T_PRIORITY_NONE_IMG = "icons/12/priority_none.png";
	public static final String T_PRIORITY_URGENT_IMG = "icons/12/priority_urgent.png";

	public static final String B_PRIORITY_BLOCKER_IMG_12 = "icons/12/priority_urgent.png";
	public static final String B_PRIORITY_CRITICAL_IMG_12 = "icons/12/priority_high.png";
	public static final String B_PRIORITY_MAJOR_IMG_12 = "icons/12/priority_medium.png";
	public static final String B_PRIORITY_MINOR_IMG_12 = "icons/12/priority_low.png";
	public static final String B_PRIORITY_TRIVIAL_IMG_12 = "icons/12/priority_none.png";

	public static final String PRIORITY_BLOCKER_IMG_16 = "icons/16/priority_urgent.png";
	public static final String PRIORITY_CRITICAL_IMG_16 = "icons/16/priority_high.png";
	public static final String PRIORITY_MAJOR_IMG_16 = "icons/16/priority_medium.png";
	public static final String PRIORITY_MINOR_IMG_16 = "icons/16/priority_low.png";
	public static final String PRIORITY_TRIVIAL_IMG_16 = "icons/16/priority_none.png";

	static {
		resourceLinks = new HashMap<String, String>();
		resourceLinks.put(ProjectTypeConstants.PROJECT, MyCollabResource
				.newResourceLink("icons/16/project/project.png"));
		resourceLinks.put(ProjectTypeConstants.MESSAGE, MyCollabResource
				.newResourceLink("icons/16/project/message.png"));
		resourceLinks.put(ProjectTypeConstants.MILESTONE, MyCollabResource
				.newResourceLink("icons/16/project/milestone.png"));
		resourceLinks.put(ProjectTypeConstants.PROBLEM, MyCollabResource
				.newResourceLink("icons/16/project/problem.png"));
		resourceLinks.put(ProjectTypeConstants.RISK,
				MyCollabResource.newResourceLink("icons/16/project/risk.png"));
		resourceLinks.put(ProjectTypeConstants.TASK,
				MyCollabResource.newResourceLink("icons/16/project/task.png"));
		resourceLinks.put(ProjectTypeConstants.TASK_LIST, MyCollabResource
				.newResourceLink("icons/16/project/task_group.png"));
		resourceLinks.put(ProjectTypeConstants.BUG,
				MyCollabResource.newResourceLink("icons/16/project/bug.png"));
		resourceLinks.put(ProjectTypeConstants.BUG_COMPONENT, MyCollabResource
				.newResourceLink("icons/16/project/component.png"));
		resourceLinks.put(ProjectTypeConstants.BUG_VERSION, MyCollabResource
				.newResourceLink("icons/16/project/version.png"));
		resourceLinks.put(ProjectTypeConstants.STANDUP, MyCollabResource
				.newResourceLink("icons/16/project/standup.png"));

		resources = new HashMap<String, Resource>();
		resources.put(ProjectTypeConstants.PROJECT,
				MyCollabResource.newResource("icons/16/project/project.png"));
		resources.put(ProjectTypeConstants.MESSAGE,
				MyCollabResource.newResource("icons/16/project/message.png"));
		resources.put(ProjectTypeConstants.MILESTONE,
				MyCollabResource.newResource("icons/16/project/milestone.png"));
		resources.put(ProjectTypeConstants.PROBLEM,
				MyCollabResource.newResource("icons/16/project/problem.png"));
		resources.put(ProjectTypeConstants.RISK,
				MyCollabResource.newResource("icons/16/project/risk.png"));
		resources.put(ProjectTypeConstants.TASK,
				MyCollabResource.newResource("icons/16/project/task.png"));
		resources
				.put(ProjectTypeConstants.TASK_LIST, MyCollabResource
						.newResource("icons/16/project/task_group.png"));
		resources.put(ProjectTypeConstants.BUG,
				MyCollabResource.newResource("icons/16/project/bug.png"));
		resources.put(ProjectTypeConstants.BUG_COMPONENT,
				MyCollabResource.newResource("icons/16/project/component.png"));
		resources.put(ProjectTypeConstants.BUG_VERSION,
				MyCollabResource.newResource("icons/16/project/version.png"));
		resources.put(ProjectTypeConstants.STANDUP,
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

	public static Resource getIconResource12ByBugSeverity(String severity) {
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

	public static Resource getIconResource16ByBugSeverity(String severity) {
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

	public static Resource getIconResource12ByBugPriority(String priority) {
		Resource iconPriority = MyCollabResource
				.newResource(B_PRIORITY_MAJOR_IMG_12);

		if (BugPriorityStatusConstants.BLOCKER.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(B_PRIORITY_BLOCKER_IMG_12);
		} else if (BugPriorityStatusConstants.CRITICAL.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(B_PRIORITY_CRITICAL_IMG_12);
		} else if (BugPriorityStatusConstants.MAJOR.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(B_PRIORITY_MAJOR_IMG_12);
		} else if (BugPriorityStatusConstants.MINOR.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(B_PRIORITY_MINOR_IMG_12);
		} else if (BugPriorityStatusConstants.TRIVIAL.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(B_PRIORITY_TRIVIAL_IMG_12);
		}
		return iconPriority;
	}

	public static Resource getIconResource16ByBugPriority(String priority) {
		Resource iconPriority = MyCollabResource
				.newResource(PRIORITY_MAJOR_IMG_16);
		if (BugPriorityStatusConstants.BLOCKER.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(PRIORITY_BLOCKER_IMG_16);
		} else if (BugPriorityStatusConstants.CRITICAL.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(PRIORITY_CRITICAL_IMG_16);
		} else if (BugPriorityStatusConstants.MAJOR.equals(priority)) {
			iconPriority = MyCollabResource.newResource(PRIORITY_MAJOR_IMG_16);
		} else if (BugPriorityStatusConstants.MINOR.equals(priority)) {
			iconPriority = MyCollabResource.newResource(PRIORITY_MINOR_IMG_16);
		} else if (BugPriorityStatusConstants.TRIVIAL.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(PRIORITY_TRIVIAL_IMG_16);
		}
		return iconPriority;
	}

	public static Resource getIconResource12ByTaskPriority(String priority) {
		Resource iconPriority = MyCollabResource
				.newResource(T_PRIORITY_HIGHT_IMG);

		if (TaskPriorityStatusContants.PRIORITY_URGENT.equals(priority)) {
			iconPriority = MyCollabResource.newResource(T_PRIORITY_URGENT_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_HIGHT.equals(priority)) {
			iconPriority = MyCollabResource.newResource(T_PRIORITY_HIGHT_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_MEDIUM
				.endsWith(priority)) {
			iconPriority = MyCollabResource.newResource(T_PRIORITY_MEDIUM_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_NONE.equals(priority)) {
			iconPriority = MyCollabResource.newResource(T_PRIORITY_NONE_IMG);
		}
		return iconPriority;
	}
}
