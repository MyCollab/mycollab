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
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
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
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
			this.generateFieldDisplayHandler("environment",
					BugI18nEnum.FORM_ENVIRONMENT);
			this.generateFieldDisplayHandler("summary",
					BugI18nEnum.FORM_SUMMARY);
			this.generateFieldDisplayHandler("severity",
					BugI18nEnum.FORM_SEVERITY, new I18nHistoryFieldFormat(
							BugSeverity.class));
			this.generateFieldDisplayHandler("priority",
					BugI18nEnum.FORM_PRIORITY, new I18nHistoryFieldFormat(
							BugPriority.class));
			this.generateFieldDisplayHandler("status", BugI18nEnum.FORM_STATUS,
					new I18nHistoryFieldFormat(BugStatus.class));
			this.generateFieldDisplayHandler("duedate",
					BugI18nEnum.FORM_DUE_DATE, AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("resolution",
					BugI18nEnum.FORM_RESOLUTION, new I18nHistoryFieldFormat(
							BugResolution.class));
		}
	}

	private static class TaskAuditLogShowHandler extends AuditLogShowHandler {
		public TaskAuditLogShowHandler() {
			this.generateFieldDisplayHandler("taskname",
					TaskI18nEnum.FORM_TASK_NAME);
			this.generateFieldDisplayHandler("percentagecomplete",
					TaskI18nEnum.FORM_PERCENTAGE_COMPLETE);
			this.generateFieldDisplayHandler("startdate",
					TaskI18nEnum.FORM_START_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("enddate",
					TaskI18nEnum.FORM_END_DATE, AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("priority",
					TaskI18nEnum.FORM_PRIORITY, new I18nHistoryFieldFormat(
							TaskPriority.class));
			this.generateFieldDisplayHandler("isestimated",
					TaskI18nEnum.FORM_IS_ESTIMATED);
			this.generateFieldDisplayHandler("deadline",
					TaskI18nEnum.FORM_DEADLINE, AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("actualstartdate",
					TaskI18nEnum.FORM_ACTUAL_START_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("actualenddate",
					TaskI18nEnum.FORM_ACTUAL_END_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("assignUserFullName",
					GenericI18Enum.FORM_ASSIGNEE);
		}
	}

	private static class TaskListAuditLogShowHandler extends
			AuditLogShowHandler {
		public TaskListAuditLogShowHandler() {
			this.generateFieldDisplayHandler("name",
					TaskGroupI18nEnum.FORM_NAME_FIELD);
			this.generateFieldDisplayHandler("status",
					TaskGroupI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
			this.generateFieldDisplayHandler("milestoneName",
					TaskGroupI18nEnum.FORM_PHASE_FIELD);
			this.generateFieldDisplayHandler("ownerFullName",
					GenericI18Enum.FORM_ASSIGNEE);
		}
	}

	private static class MilestoneAuditLogShowHandler extends
			AuditLogShowHandler {
		public MilestoneAuditLogShowHandler() {
			this.generateFieldDisplayHandler("name",
					MilestoneI18nEnum.FORM_NAME_FIELD);
			this.generateFieldDisplayHandler("startdate",
					MilestoneI18nEnum.FORM_START_DATE_FIELD,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("enddate",
					MilestoneI18nEnum.FORM_END_DATE_FIELD,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status",
					MilestoneI18nEnum.FORM_STATUS_FIELD,
					new I18nHistoryFieldFormat(MilestoneStatus.class));
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
			this.generateFieldDisplayHandler("ownerFullName",
					GenericI18Enum.FORM_ASSIGNEE);
		}
	}

	private static class RiskAuditLogShowHandler extends AuditLogShowHandler {
		public RiskAuditLogShowHandler() {
			this.generateFieldDisplayHandler("riskname", RiskI18nEnum.FORM_NAME);
			this.generateFieldDisplayHandler("consequence",
					RiskI18nEnum.FORM_CONSEQUENCE);
			this.generateFieldDisplayHandler("probalitity",
					RiskI18nEnum.FORM_PROBABILITY);
			this.generateFieldDisplayHandler("status", RiskI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("datedue",
					RiskI18nEnum.FORM_DATE_DUE, AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("response",
					RiskI18nEnum.FORM_RESPONSE);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
			this.generateFieldDisplayHandler("raisedByUserFullName",
					RiskI18nEnum.FORM_RAISED_BY);
			this.generateFieldDisplayHandler("assignedToUserFullName",
					GenericI18Enum.FORM_ASSIGNEE);
		}
	}

	private static class ProblemAuditLogShowHandler extends AuditLogShowHandler {
		public ProblemAuditLogShowHandler() {
			this.generateFieldDisplayHandler("issuename",
					ProblemI18nEnum.FORM_NAME);
			this.generateFieldDisplayHandler("impact",
					ProblemI18nEnum.FORM_IMPACT);
			this.generateFieldDisplayHandler("priority",
					ProblemI18nEnum.FORM_PRIORITY);
			this.generateFieldDisplayHandler("status",
					ProblemI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("datedue",
					ProblemI18nEnum.FORM_DATE_DUE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("resolution",
					ProblemI18nEnum.FORM_RESOLUTION);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
			this.generateFieldDisplayHandler("raisedByUserFullName",
					ProblemI18nEnum.FORM_RAISED_BY);
			this.generateFieldDisplayHandler("assignedUserFullName",
					GenericI18Enum.FORM_ASSIGNEE);
		}
	}

	private static class ComponentAuditLogShowHandler extends
			AuditLogShowHandler {
		public ComponentAuditLogShowHandler() {
			this.generateFieldDisplayHandler("componentname",
					ComponentI18nEnum.FORM_NAME);
			this.generateFieldDisplayHandler("status",
					ComponentI18nEnum.FORM_STATUS, new I18nHistoryFieldFormat(
							StatusI18nEnum.class));
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
			this.generateFieldDisplayHandler("userLeadFullName",
					ComponentI18nEnum.FORM_LEAD);
		}
	}

	private static class VersionAuditLogShowHandler extends AuditLogShowHandler {
		public VersionAuditLogShowHandler() {
			this.generateFieldDisplayHandler("duedate",
					VersionI18nEnum.FORM_DUE_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status",
					VersionI18nEnum.FORM_STATUS, new I18nHistoryFieldFormat(
							StatusI18nEnum.class));
			this.generateFieldDisplayHandler("versionname",
					VersionI18nEnum.FORM_NAME);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
		}
	}
}
