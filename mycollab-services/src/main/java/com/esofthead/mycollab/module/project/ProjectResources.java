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
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;

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

	static final String M_STATUS_INPROGRESS_IMG_12 = "icons/12/project/phase_progress.png";
	static final String M_STATUS_CLOSED_IMG_12 = "icons/12/project/phase_closed.png";
	static final String M_STATUS_FUTURE_IMG_12 = "icons/12/project/phase_future.png";

	static final String M_STATUS_INPROGRESS_IMG_16 = "icons/16/project/phase_progress.png";
	static final String M_STATUS_CLOSED_IMG_16 = "icons/16/project/phase_closed.png";
	static final String M_STATUS_FUTURE_IMG_16 = "icons/16/project/phase_future.png";

	public static final String B_SEVERITY_CRITICAL_IMG_12 = "icons/12/severity_critical.png";
	public static final String B_SEVERITY_MAJOR_IMG_12 = "icons/12/severity_major.png";
	public static final String B_SEVERITY_MINOR_IMG_12 = "icons/12/severity_minor.png";
	public static final String B_SEVERITY_TRIVIAL_IMG_12 = "icons/12/severity_trivial.png";

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
		resourceLinks.put(ProjectTypeConstants.PAGE,
				MyCollabAssets.newResourceLink("icons/16/project/page.png"));
		resourceLinks.put(ProjectTypeConstants.STANDUP,
				MyCollabAssets.newResourceLink("icons/16/project/standup.png"));
	}

	public static String getResourceLink(String type) {
		return resourceLinks.get(type);
	}

	public static String getIconResource12LinkOfPhaseStatus(String phase) {
		String iconseverity = MyCollabAssets
				.newResourceLink(M_STATUS_INPROGRESS_IMG_12);
		MilestoneStatus status = MilestoneStatus.valueOf(phase);

		if (MilestoneStatus.InProgress.equals(status)) {
			iconseverity = MyCollabAssets
					.newResourceLink(M_STATUS_INPROGRESS_IMG_12);
		} else if (MilestoneStatus.Future.equals(status)) {
			iconseverity = MyCollabAssets
					.newResourceLink(M_STATUS_FUTURE_IMG_12);
		} else if (MilestoneStatus.Closed.equals(status)) {
			iconseverity = MyCollabAssets
					.newResourceLink(M_STATUS_CLOSED_IMG_12);
		}

		return iconseverity;
	}

	public static String getIconResource16LinkOfPhaseStatus(String phase) {
		String iconseverity = MyCollabAssets
				.newResourceLink(M_STATUS_INPROGRESS_IMG_16);
		MilestoneStatus status = MilestoneStatus.valueOf(phase);

		if (MilestoneStatus.InProgress.equals(status)) {
			iconseverity = MyCollabAssets
					.newResourceLink(M_STATUS_INPROGRESS_IMG_16);
		} else if (MilestoneStatus.Future.equals(status)) {
			iconseverity = MyCollabAssets
					.newResourceLink(M_STATUS_FUTURE_IMG_16);
		} else if (MilestoneStatus.Closed.equals(status)) {
			iconseverity = MyCollabAssets
					.newResourceLink(M_STATUS_CLOSED_IMG_16);
		}

		return iconseverity;
	}

	public static String getIconResourceLink12ByBugSeverity(String severity) {
		String iconseverity = MyCollabAssets
				.newResourceLink(B_SEVERITY_MINOR_IMG_12);

		if (BugSeverity.Critical.name().equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(B_SEVERITY_CRITICAL_IMG_12);
		} else if (BugSeverity.Major.name().equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(B_SEVERITY_MAJOR_IMG_12);
		} else if (BugSeverity.Minor.name().equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(B_SEVERITY_MINOR_IMG_12);
		} else if (BugSeverity.Trivial.name().equals(severity)) {
			iconseverity = MyCollabAssets
					.newResourceLink(B_SEVERITY_TRIVIAL_IMG_12);
		}
		return iconseverity;
	}

	public static String getIconResourceLink12ByBugPriority(String priority) {
		String iconPriority = MyCollabAssets
				.newResourceLink(B_PRIORITY_MAJOR_IMG_12);

		if (BugPriority.Blocker.name().equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_BLOCKER_IMG_12);
		} else if (BugPriority.Critical.name().equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_CRITICAL_IMG_12);
		} else if (BugPriority.Major.name().equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_MAJOR_IMG_12);
		} else if (BugPriority.Minor.name().equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_MINOR_IMG_12);
		} else if (BugPriority.Trivial.name().equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(B_PRIORITY_TRIVIAL_IMG_12);
		}
		return iconPriority;
	}

	public static String getIconResourceLink12ByTaskPriority(String priority) {
		String iconPriority = MyCollabAssets
				.newResourceLink(T_PRIORITY_HIGHT_IMG);

		if (TaskPriority.Urgent.name().equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(T_PRIORITY_URGENT_IMG);
		} else if (TaskPriority.High.name().equals(priority)) {
			iconPriority = MyCollabAssets.newResourceLink(T_PRIORITY_HIGHT_IMG);
		} else if (TaskPriority.Medium.name().equals(priority)) {
			iconPriority = MyCollabAssets
					.newResourceLink(T_PRIORITY_MEDIUM_IMG);
		} else if (TaskPriority.Low.name().equals(priority)) {
			iconPriority = MyCollabAssets.newResourceLink(T_PRIORITY_LOW_IMG);
		} else if (TaskPriority.None.equals(priority)) {
			iconPriority = MyCollabAssets.newResourceLink(T_PRIORITY_NONE_IMG);
		}
		return iconPriority;
	}
}
