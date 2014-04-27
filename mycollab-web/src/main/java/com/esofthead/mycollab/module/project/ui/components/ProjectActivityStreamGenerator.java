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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.utils.AuditLogShowHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectActivityStreamGenerator {
	private static AuditLogShowHandler bugHandler = new BugAuditLogShowHandler();
	private static AuditLogShowHandler taskHandler = new TaskAuditLogShowHandler();
	private static AuditLogShowHandler taskListHandler = new TaskListAuditLogShowHandler();
	private static AuditLogShowHandler milestoneHandler = new MilestoneAuditLogShowHandler();
	private static AuditLogShowHandler riskHandler = new RiskAuditLogShowHandler();
	private static AuditLogShowHandler problemHandler = new ProblemAuditLogShowHandler();
	private static AuditLogShowHandler componentHandler = new ComponentAuditLogShowHandler();
	private static AuditLogShowHandler versionHandler = new VersionAuditLogShowHandler();

	private static AuditLogShowHandler defaultHandler = new AuditLogShowHandler();

	private static AuditLogShowHandler getShowHandler(String type) {
		if (ProjectTypeConstants.BUG.equals(type)) {
			return bugHandler;
		} else if (ProjectTypeConstants.TASK.equals(type)) {
			return taskHandler;
		} else if (ProjectTypeConstants.TASK_LIST.equals(type)) {
			return taskListHandler;
		} else if (ProjectTypeConstants.MILESTONE.equals(type)) {
			return milestoneHandler;
		} else if (ProjectTypeConstants.RISK.equals(type)) {
			return riskHandler;
		} else if (ProjectTypeConstants.PROBLEM.equals(type)) {
			return problemHandler;
		} else if (ProjectTypeConstants.BUG_COMPONENT.equals(type)) {
			return componentHandler;
		} else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
			return versionHandler;
		} else {
			return defaultHandler;
		}
	}

	public static String generatorDetailChangeOfActivity(
			SimpleActivityStream activityStream) {

		if (activityStream.getAssoAuditLog() != null) {
			AuditLogShowHandler auditLogHandler = getShowHandler(activityStream
					.getType());
			return auditLogHandler.generateChangeSet(activityStream
					.getAssoAuditLog());
		} else {
			return "";
		}
	}

	private static class BugAuditLogShowHandler extends AuditLogShowHandler {
		public BugAuditLogShowHandler() {
			this.generateFieldDisplayHandler("description", "Description");
			this.generateFieldDisplayHandler("environment", "Environment");
			this.generateFieldDisplayHandler("detail", "Detail");
			this.generateFieldDisplayHandler("summary", "Summary");
			this.generateFieldDisplayHandler("severity", "Severity");
			this.generateFieldDisplayHandler("priority", "Priority");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("duedate", "Due Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("resolution", "Resolution");
		}
	}

	private static class TaskAuditLogShowHandler extends AuditLogShowHandler {
		public TaskAuditLogShowHandler() {
			this.generateFieldDisplayHandler("taskname", "Name");
			this.generateFieldDisplayHandler("percentagecomplete",
					"Percentage Complete");
			this.generateFieldDisplayHandler("startdate", "Start Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("enddate", "End Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("priority", "Priority");
			this.generateFieldDisplayHandler("duration", "Duration");
			this.generateFieldDisplayHandler("isestimated", "Is Estimated");
			this.generateFieldDisplayHandler("deadline", "Deadline",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("actualstartdate",
					"Actual Start Date", AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("actualenddate",
					"Actual End Date", AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("assignUserFullName",
					LocalizationHelper
							.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		}
	}

	private static class TaskListAuditLogShowHandler extends
			AuditLogShowHandler {
		public TaskListAuditLogShowHandler() {
			this.generateFieldDisplayHandler("name", "Name");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("description", "Description");
			this.generateFieldDisplayHandler("milestoneName", "Milestone");
			this.generateFieldDisplayHandler("ownerFullName",
					LocalizationHelper
							.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		}
	}

	private static class MilestoneAuditLogShowHandler extends
			AuditLogShowHandler {
		public MilestoneAuditLogShowHandler() {
			this.generateFieldDisplayHandler("name", "Name");
			this.generateFieldDisplayHandler("startdate", "Start Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("enddate", "End Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("description", "Description");
			this.generateFieldDisplayHandler("ownerFullName",
					LocalizationHelper
							.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		}
	}

	private static class RiskAuditLogShowHandler extends AuditLogShowHandler {
		public RiskAuditLogShowHandler() {
			this.generateFieldDisplayHandler("riskname", "Name");
			this.generateFieldDisplayHandler("consequence", "Consequence");
			this.generateFieldDisplayHandler("probalitity", "Probability");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("dateraised", "Raised Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("datedue", "Due Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("response", "Response");
			this.generateFieldDisplayHandler("resolution", "Resolution");
			this.generateFieldDisplayHandler("source", "Source");
			this.generateFieldDisplayHandler("description", "Description");
			this.generateFieldDisplayHandler("raisedByUserFullName",
					"Raised By");
			this.generateFieldDisplayHandler("assignedToUserFullName",
					LocalizationHelper
							.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		}
	}

	private static class ProblemAuditLogShowHandler extends AuditLogShowHandler {
		public ProblemAuditLogShowHandler() {
			this.generateFieldDisplayHandler("issuename", "Name");
			this.generateFieldDisplayHandler("impact", "Impact");
			this.generateFieldDisplayHandler("priority", "Priority");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("dateraised", "Raised Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("datedue", "Due Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("actualstartdate",
					"Actual Start Date", AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("actualenddate",
					"Actual End Date", AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("resolution", "Resolution");
			this.generateFieldDisplayHandler("state", "State");
			this.generateFieldDisplayHandler("problemsource", "Source");
			this.generateFieldDisplayHandler("description", "Description");
			this.generateFieldDisplayHandler("raisedByUserFullName",
					"Raised By");
			this.generateFieldDisplayHandler("assignedUserFullName",
					LocalizationHelper
							.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		}
	}

	private static class ComponentAuditLogShowHandler extends
			AuditLogShowHandler {
		public ComponentAuditLogShowHandler() {
			this.generateFieldDisplayHandler("componentname", "Name");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("description", "Description");
			this.generateFieldDisplayHandler("userLeadFullName", "Lead");
		}
	}

	private static class VersionAuditLogShowHandler extends AuditLogShowHandler {
		public VersionAuditLogShowHandler() {
			this.generateFieldDisplayHandler("duedate", "Due Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("versionname", "Name");
			this.generateFieldDisplayHandler("description", "Description");
		}
	}
}
