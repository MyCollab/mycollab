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

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.configuration.MyCollabAssets;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectResources {

	private static final Map<String, String> resourceLinks;

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
		resourceLinks.put(ProjectTypeConstants.PROJECT,
				MyCollabAssets.newResourceLink("icons/16/project/project.png"));
		resourceLinks.put(ProjectTypeConstants.MESSAGE,
				MyCollabAssets.newResourceLink("icons/16/project/message.png"));
		resourceLinks.put(ProjectTypeConstants.MILESTONE, MyCollabAssets
				.newResourceLink("icons/16/project/milestone.png"));
		resourceLinks.put(ProjectTypeConstants.PROBLEM,
				MyCollabAssets.newResourceLink("icons/16/project/problem.png"));
		resourceLinks.put(ProjectTypeConstants.RISK,
				MyCollabAssets.newResourceLink("icons/16/project/risk.png"));
		resourceLinks.put(ProjectTypeConstants.TASK,
				MyCollabAssets.newResourceLink("icons/16/project/task.png"));
		resourceLinks.put(ProjectTypeConstants.TASK_LIST, MyCollabAssets
				.newResourceLink("icons/16/project/task_group.png"));
		resourceLinks.put(ProjectTypeConstants.BUG,
				MyCollabAssets.newResourceLink("icons/16/project/bug.png"));
		resourceLinks.put(ProjectTypeConstants.BUG_COMPONENT, MyCollabAssets
				.newResourceLink("icons/16/project/component.png"));
		resourceLinks.put(ProjectTypeConstants.BUG_VERSION,
				MyCollabAssets.newResourceLink("icons/16/project/version.png"));
		resourceLinks.put(ProjectTypeConstants.STANDUP,
				MyCollabAssets.newResourceLink("icons/16/project/standup.png"));
	}

	public static String getResourceLink(String type) {
		return resourceLinks.get(type);
	}

	public static String getIconResourceLink12ByPhase(String phase) {
		String iconseverity = MyCollabAssets
				.newResourceLink(MilestoneStatusConstant.INPROGRESS_IMG_12);

		if (MilestoneStatusConstant.IN_PROGRESS.equals(phase)) {
			iconseverity = MyCollabAssets
					.newResourceLink(MilestoneStatusConstant.INPROGRESS_IMG_12);
		} else if (MilestoneStatusConstant.FUTURE.equals(phase)) {
			iconseverity = MyCollabAssets
					.newResourceLink(MilestoneStatusConstant.FUTURE_IMG_12);
		} else if (MilestoneStatusConstant.CLOSED.equals(phase)) {
			iconseverity = MyCollabAssets
					.newResourceLink(MilestoneStatusConstant.CLOSED_IMG_12);
		}

		return iconseverity;
	}

	public static String getIconResourceLink12ByBugSeverity(String severity) {
		String iconseverity = MyCollabAssets
				.newResourceLink(BugSeverityConstants.MINOR_IMG_12);

		if (BugSeverityConstants.CRITICAL.equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(BugSeverityConstants.CRITICAL_IMG_12);
		} else if (BugSeverityConstants.MAJOR.equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(BugSeverityConstants.MAJOR_IMG_12);
		} else if (BugSeverityConstants.MINOR.equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(BugSeverityConstants.MINOR_IMG_12);
		} else if (BugSeverityConstants.TRIVIAL.equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(BugSeverityConstants.TRIVIAL_IMG_12);
		}
		return iconseverity;
	}

	public static String getIconResourceLink12ByBugPriority(String priority) {
		String iconPriority = MyCollabAssets
				.newResourceLink(B_PRIORITY_MAJOR_IMG_12);

		if (BugPriorityStatusConstants.BLOCKER.equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_BLOCKER_IMG_12);
		} else if (BugPriorityStatusConstants.CRITICAL.equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_CRITICAL_IMG_12);
		} else if (BugPriorityStatusConstants.MAJOR.equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_MAJOR_IMG_12);
		} else if (BugPriorityStatusConstants.MINOR.equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_MINOR_IMG_12);
		} else if (BugPriorityStatusConstants.TRIVIAL.equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_TRIVIAL_IMG_12);
		}
		return iconPriority;
	}

	public static String getIconResourceLink12ByTaskPriority(String priority) {
		String iconPriority = MyCollabAssets
				.newResourceLink(T_PRIORITY_HIGHT_IMG);

		if (TaskPriorityStatusContants.PRIORITY_URGENT.equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(T_PRIORITY_URGENT_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_HIGHT.equals(priority)) {
			iconPriority = MyCollabAssets.newResourceLink(T_PRIORITY_HIGHT_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_MEDIUM
				.endsWith(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(T_PRIORITY_MEDIUM_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_NONE.equals(priority)) {
			iconPriority = MyCollabAssets.newResourceLink(T_PRIORITY_NONE_IMG);
		}
		return iconPriority;
	}
}
