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

import static com.esofthead.mycollab.common.TooltipBuilder.TdUtil.buildCellLink;
import static com.esofthead.mycollab.common.TooltipBuilder.TdUtil.buildCellName;
import static com.esofthead.mycollab.common.TooltipBuilder.TdUtil.buildCellValue;
import static com.esofthead.mycollab.core.utils.StringUtils.trimHtmlTags;

import java.util.Locale;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.TooltipBuilder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
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
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.i18n.StandupI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectTooltipGenerator {
	private static Logger log = LoggerFactory
			.getLogger(ProjectTooltipGenerator.class);

	private static String generateTolltipNull(Locale locale) {
		Div div = new Div();
		Table table = new Table();
		table.setStyle("padding-left:10px;  color: #5a5a5a; font-size:11px;");

		Tr trRow1 = new Tr();
		trRow1.appendChild(new Td().setStyle(
				"vertical-align: top; text-align: left;").appendText(
				LocalizationHelper.getMessage(locale,
						GenericI18Enum.TOOLTIP_NO_ITEM_EXISTED)));

		table.appendChild(trRow1);
		div.appendChild(table);

		return div.write();
	}

	public static String generateToolTipTask(Locale locale, SimpleTask task,
			String siteURL, String timeZone) {
		if (task == null) {
			return generateTolltipNull(locale);
		}
		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(task.getTaskname());

			Tr trRow1 = new Tr();
			Td cell11 = TooltipBuilder.TdUtil.buildCellName(LocalizationHelper
					.getMessage(locale, TaskI18nEnum.FORM_START_DATE));
			String startdate = DateTimeUtils.converToStringWithUserTimeZone(
					task.getStartdate(), timeZone);
			Td cell12 = buildCellValue(startdate);
			Td cell13 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_ACTUAL_START_DATE));
			String actualStartDate = DateTimeUtils
					.converToStringWithUserTimeZone(task.getActualstartdate(),
							timeZone);
			Td cell14 = buildCellValue(actualStartDate);
			trRow1.appendChild(cell11, cell12, cell13, cell14);
			tooltipManager.appendRow(trRow1);

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_END_DATE));
			String endDate = DateTimeUtils.converToStringWithUserTimeZone(
					task.getEnddate(), timeZone);
			Td cell22 = buildCellValue(endDate);
			Td cell23 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_ACTUAL_END_DATE));
			String actualEndDate = DateTimeUtils
					.converToStringWithUserTimeZone(task.getActualenddate(),
							timeZone);
			Td cell24 = buildCellValue(actualEndDate);
			trRow2.appendChild(cell21, cell22, cell23, cell24);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_DEADLINE));
			String deadline = DateTimeUtils.converToStringWithUserTimeZone(
					task.getDeadline(), timeZone);
			Td cell32 = buildCellValue(deadline);
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_PRIORITY));
			Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale,
					TaskPriority.class, task.getPriority()));
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			Tr trRow4 = new Tr();
			Td cell41 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_ASSIGNEE));
			String assignUserLink = (task.getAssignuser() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL, task.getAssignuser())
					: "";
			String assignUserAvatarLink = SiteConfiguration.getAvatarLink(
					task.getAssignUserAvatarId(), 16);
			Td cell42 = buildCellLink(assignUserLink, assignUserAvatarLink,
					task.getAssignUserFullName());
			Td cell43 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_TASKGROUP));
			String taskgroupLink = (task.getTaskListName() != null) ? ProjectLinkGenerator
					.generateTaskGroupPreviewFullLink(siteURL,
							task.getProjectid(), task.getTasklistid()) : "";
			Td cell44 = buildCellLink(taskgroupLink, task.getTaskListName());
			trRow4.appendChild(cell41, cell42, cell43, cell44);
			tooltipManager.appendRow(trRow4);

			Tr trRow5 = new Tr();
			Td cell51 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_PERCENTAGE_COMPLETE));
			Td cell52 = buildCellValue(task.getPercentagecomplete());
			trRow5.appendChild(cell51, cell52);
			tooltipManager.appendRow(trRow5);

			Tr trRow6 = new Tr();
			Td cell61 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskI18nEnum.FORM_NOTES));
			Td cell62 = buildCellValue(trimHtmlTags(task.getNotes()));
			cell62.setAttribute("colspan", "3");
			trRow6.appendChild(cell61, cell62);
			tooltipManager.appendRow(trRow6);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project-task tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipBug(Locale locale, SimpleBug bug,
			String siteURL, String timeZone) {
		if (bug == null) {
			return generateTolltipNull(locale);
		}

		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(bug.getSummary());

			Tr trRow1 = new Tr();
			Td cell11 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell12 = buildCellValue(trimHtmlTags(bug.getDescription()));
			cell12.setAttribute("colspan", "3");
			trRow1.appendChild(cell11, cell12);
			tooltipManager.appendRow(trRow1);

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_ENVIRONMENT));
			Td cell22 = buildCellValue(trimHtmlTags(bug.getEnvironment()));
			cell22.setAttribute("colspan", "3");
			trRow2.appendChild(cell21, cell22);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_STATUS));
			Td cell32 = buildCellValue(LocalizationHelper.getMessage(locale,
					BugStatus.class, bug.getStatus()));
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_PRIORITY));
			Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale,
					BugPriority.class, bug.getPriority()));
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			Tr trRow4 = new Tr();
			Td cell41 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_SEVERITY));
			Td cell42 = buildCellValue(LocalizationHelper.getMessage(locale,
					BugSeverity.class, bug.getSeverity()));
			Td cell43 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_RESOLUTION));
			Td cell44 = buildCellValue(LocalizationHelper.getMessage(locale,
					BugResolution.class, bug.getResolution()));
			trRow4.appendChild(cell41, cell42, cell43, cell44);
			tooltipManager.appendRow(trRow4);

			Tr trRow5 = new Tr();
			Td cell51 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_DUE_DATE));
			String dueDate = DateTimeUtils.converToStringWithUserTimeZone(
					bug.getDuedate(), timeZone);
			Td cell52 = buildCellValue(dueDate);
			Td cell53 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_CREATED_TIME));
			String createdTime = DateTimeUtils.converToStringWithUserTimeZone(
					bug.getCreatedtime(), timeZone);
			Td cell54 = buildCellValue(createdTime);
			trRow5.appendChild(cell51, cell52, cell53, cell54);
			tooltipManager.appendRow(trRow5);

			// Assignee

			Tr trRow6 = new Tr();
			Td cell61 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_LOG_BY));
			String logbyUserLink = (bug.getLogby() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL, bug.getLogby()) : "";
			String logbyAvatarLink = SiteConfiguration.getAvatarLink(
					bug.getLoguserAvatarId(), 16);
			Td cell62 = buildCellLink(logbyUserLink, logbyAvatarLink,
					bug.getLoguserFullName());
			Td cell63 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_ASSIGNEE));
			String assignUserLink = (bug.getAssignuser() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL, bug.getAssignuser())
					: "";
			String assignUserAvatarLink = SiteConfiguration.getAvatarLink(
					bug.getAssignUserAvatarId(), 16);
			Td cell64 = buildCellLink(assignUserLink, assignUserAvatarLink,
					bug.getAssignuserFullName());
			trRow6.appendChild(cell61, cell62, cell63, cell64);
			tooltipManager.appendRow(trRow6);

			Tr trRow7 = new Tr();
			Td cell71 = buildCellName(LocalizationHelper.getMessage(locale,
					BugI18nEnum.FORM_PHASE));
			String phaseLink = (bug.getMilestoneid() != null) ? ProjectLinkGenerator
					.generateMilestonePreviewFullLink(siteURL,
							bug.getProjectid(), bug.getMilestoneid()) : "";
			Td cell72 = buildCellLink(phaseLink, bug.getMilestoneName());
			cell72.setAttribute("colspan", "3");
			trRow7.appendChild(cell71, cell72);
			tooltipManager.appendRow(trRow7);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project-bug tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipRisk(Locale locale, SimpleRisk risk,
			String siteURL, String timeZone) {
		if (risk == null)
			return generateTolltipNull(locale);
		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(risk.getRiskname());

			Tr trRow5 = new Tr();
			Td cell51 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell52 = buildCellValue(trimHtmlTags(risk.getDescription()));
			cell52.setAttribute("colspan", "3");
			trRow5.appendChild(cell51, cell52);
			tooltipManager.appendRow(trRow5);

			Tr trRow1 = new Tr();
			Td cell11 = buildCellName(LocalizationHelper.getMessage(locale,
					RiskI18nEnum.FORM_RAISED_BY));
			String raisedUserLink = (risk.getRaisedbyuser() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL,
							risk.getRaisedbyuser()) : "";
			String raisedUserAvatarLink = SiteConfiguration.getAvatarLink(
					risk.getRaisedByUserAvatarId(), 16);
			Td cell12 = buildCellLink(raisedUserLink, raisedUserAvatarLink,
					risk.getRaisedByUserFullName());
			Td cell13 = buildCellName(LocalizationHelper.getMessage(locale,
					RiskI18nEnum.FORM_CONSEQUENCE));
			Td cell14 = buildCellValue(risk.getConsequence());
			trRow1.appendChild(cell11, cell12, cell13, cell14);
			tooltipManager.appendRow(trRow1);

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_ASSIGNEE));
			String assignUserLink = (risk.getAssigntouser() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL,
							risk.getAssigntouser()) : "";
			String assignUserAvatarLink = SiteConfiguration.getAvatarLink(
					risk.getAssignToUserAvatarId(), 16);
			Td cell22 = buildCellLink(assignUserLink, assignUserAvatarLink,
					risk.getAssignedToUserFullName());
			Td cell23 = buildCellName(LocalizationHelper.getMessage(locale,
					RiskI18nEnum.FORM_PROBABILITY));
			Td cell24 = buildCellValue(risk.getProbalitity());
			trRow2.appendChild(cell21, cell22, cell23, cell24);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					RiskI18nEnum.FORM_DATE_DUE));
			String datedue = DateTimeUtils.converToStringWithUserTimeZone(
					risk.getDatedue(), timeZone);
			Td cell32 = buildCellValue(datedue);
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					RiskI18nEnum.FORM_RATING));
			Td cell34 = buildCellValue(risk.getLevel());
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			Tr trRow4 = new Tr();
			Td cell41 = buildCellName(LocalizationHelper.getMessage(locale,
					RiskI18nEnum.FORM_STATUS));
			Td cell42 = buildCellValue(LocalizationHelper.getMessage(locale,
					StatusI18nEnum.class, risk.getStatus()));
			trRow4.appendChild(cell41, cell42);
			tooltipManager.appendRow(trRow4);

			Tr trRow6 = new Tr();
			Td cell61 = buildCellName(LocalizationHelper.getMessage(locale,
					RiskI18nEnum.FORM_RESPONSE));
			Td cell62 = buildCellValue(trimHtmlTags(risk.getResponse()));
			cell62.setAttribute("colspan", "3");
			trRow6.appendChild(cell61, cell62);
			tooltipManager.appendRow(trRow6);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for Risk in TooptipGeneratorServlet",
					e);
			return null;
		}
	}

	public static String generateToolTipProblem(Locale locale,
			SimpleProblem problem, String siteURL, String timeZone) {
		if (problem == null)
			return generateTolltipNull(locale);
		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(problem.getIssuename());

			Tr trRow5 = new Tr();
			Td cell51 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell52 = buildCellValue(trimHtmlTags(problem.getDescription()));
			cell52.setAttribute("colspan", "3");
			trRow5.appendChild(cell51, cell52);
			tooltipManager.appendRow(trRow5);

			Tr trRow1 = new Tr();
			Td cell11 = buildCellName(LocalizationHelper.getMessage(locale,
					ProblemI18nEnum.FORM_RAISED_BY));
			String raisedByUserLink = (problem.getRaisedbyuser() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL,
							problem.getRaisedbyuser()) : "";
			String raisedByUserAvatarLink = SiteConfiguration.getAvatarLink(
					problem.getRaisedByUserAvatarId(), 16);
			Td cell12 = buildCellLink(raisedByUserLink, raisedByUserAvatarLink,
					problem.getRaisedByUserFullName());
			Td cell13 = buildCellName(LocalizationHelper.getMessage(locale,
					ProblemI18nEnum.FORM_IMPACT));
			Td cell14 = buildCellValue(problem.getImpact());
			trRow1.appendChild(cell11, cell12, cell13, cell14);
			tooltipManager.appendRow(trRow1);

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_ASSIGNEE));
			String assignUserLink = (problem.getAssigntouser() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL,
							problem.getAssigntouser()) : "";
			String assignUserAvatarLink = SiteConfiguration.getAvatarLink(
					problem.getAssignUserAvatarId(), 16);
			Td cell22 = buildCellLink(assignUserLink, assignUserAvatarLink,
					problem.getAssignedUserFullName());
			Td cell23 = buildCellName(LocalizationHelper.getMessage(locale,
					ProblemI18nEnum.FORM_PRIORITY));
			Td cell24 = buildCellValue(problem.getPriority());
			trRow2.appendChild(cell21, cell22, cell23, cell24);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					ProblemI18nEnum.FORM_DATE_DUE));
			String datedue = DateTimeUtils.converToStringWithUserTimeZone(
					problem.getDatedue(), timeZone);
			Td cell32 = buildCellValue(datedue);
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					ProblemI18nEnum.FORM_RATING));
			Td cell34 = buildCellValue(problem.getLevel());
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			Tr trRow4 = new Tr();
			Td cell41 = buildCellName(LocalizationHelper.getMessage(locale,
					ProblemI18nEnum.FORM_STATUS));
			Td cell42 = buildCellValue(LocalizationHelper.getMessage(locale,
					StatusI18nEnum.class, problem.getStatus()));
			trRow4.appendChild(cell41, cell42);
			tooltipManager.appendRow(trRow4);

			Tr trRow6 = new Tr();
			Td cell61 = buildCellName(LocalizationHelper.getMessage(locale,
					ProblemI18nEnum.FORM_RESOLUTION));
			Td cell62 = buildCellValue(trimHtmlTags(problem.getResolution()));
			cell62.setAttribute("colspan", "3");
			trRow6.appendChild(cell61, cell62);
			tooltipManager.appendRow(trRow6);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error(
					"Error while generator tooltip for Problem in TooltipGenertor Servlet",
					e);
			return null;
		}
	}

	public static String generateToolTipVersion(Locale locale, Version version,
			String siteURL, String timeZone) {
		if (version == null)
			return generateTolltipNull(locale);
		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(version.getVersionname());

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell22 = buildCellValue(trimHtmlTags(version.getDescription()));
			cell22.setAttribute("colspan", "3");
			trRow2.appendChild(cell21, cell22);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					VersionI18nEnum.FORM_DUE_DATE));
			String duedate = DateTimeUtils.converToStringWithUserTimeZone(
					version.getDuedate(), timeZone);
			Td cell32 = buildCellValue(duedate);
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					VersionI18nEnum.FORM_STATUS));
			Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale,
					StatusI18nEnum.class, version.getStatus()));
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error("Error while generate tooltip for Version", e);
			return null;
		}
	}

	public static String generateToolTipComponent(Locale locale,
			SimpleComponent component, String siteURL, String timeZone) {
		if (component == null)
			return generateTolltipNull(locale);

		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(component.getComponentname());

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell22 = buildCellValue(trimHtmlTags(component.getDescription()));
			cell22.setAttribute("colspan", "3");
			trRow2.appendChild(cell21, cell22);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					ComponentI18nEnum.FORM_LEAD));
			String leadLink = (component.getUserlead() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL,
							component.getUserlead()) : "";
			String leadAvatarLink = SiteConfiguration.getAvatarLink(
					component.getUserLeadAvatarId(), 16);
			Td cell32 = buildCellLink(leadLink, leadAvatarLink,
					component.getUserLeadFullName());
			trRow3.appendChild(cell31, cell32);
			tooltipManager.appendRow(trRow3);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error("Error while generate tooltip for Component", e);
			return null;
		}
	}

	public static String generateToolTipTaskList(Locale locale,
			SimpleTaskList taskList, String siteURL, String timeZone) {

		if (taskList == null)
			return generateTolltipNull(locale);
		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(taskList.getName());

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell22 = buildCellValue(trimHtmlTags(taskList.getDescription()));
			cell22.setAttribute("colspan", "3");
			trRow2.appendChild(cell21, cell22);
			tooltipManager.appendRow(trRow2);

			// Assignee
			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_ASSIGNEE));
			String ownerLink = (taskList.getOwner() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL, taskList.getOwner())
					: "";
			String ownerAvatarLink = SiteConfiguration.getAvatarLink(
					taskList.getOwnerAvatarId(), 16);
			Td cell32 = buildCellLink(ownerLink, ownerAvatarLink,
					taskList.getOwnerFullName());
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					TaskGroupI18nEnum.FORM_PHASE_FIELD));
			String milestoneLink = (taskList.getMilestoneid() != null) ? ProjectLinkGenerator
					.generateMilestonePreviewFullLink(siteURL,
							taskList.getProjectid(), taskList.getId()) : "";
			Td cell34 = buildCellLink(milestoneLink,
					taskList.getMilestoneName());
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error("Error while generate tooltip for TaskGroup", e);
			return null;
		}
	}

	public static String generateToolTipProject(Locale locale,
			SimpleProject project, String siteURL, String timeZone) {
		if (project == null)
			return generateTolltipNull(locale);

		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(project.getName());

			Tr trRow1 = new Tr();
			Td cell11 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_HOME_PAGE));
			String homepageLink = (project.getHomepage() != null) ? project
					.getHomepage() : "";
			Td cell12 = buildCellLink(homepageLink, project.getHomepage());
			Td cell13 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_STATUS));
			Td cell14 = buildCellValue(project.getProjectstatus());
			trRow1.appendChild(cell11, cell12, cell13, cell14);

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_PLAN_START_DATE));
			String planStartDate = DateTimeUtils
					.converToStringWithUserTimeZone(project.getPlanstartdate(),
							timeZone);
			Td cell22 = buildCellValue(planStartDate);
			Td cell23 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_CURRENCY));
			String currency = (project.getCurrency() != null) ? StringUtils
					.getStringFieldValue(project.getCurrency().getSymbol())
					: "";
			Td cell24 = buildCellValue(currency);
			trRow2.appendChild(cell21, cell22, cell23, cell24);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_PLAN_END_DATE));
			String planEndDate = DateTimeUtils.converToStringWithUserTimeZone(
					project.getPlanenddate(), timeZone);
			Td cell32 = buildCellValue(planEndDate);
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_BILLING_RATE));
			Td cell34 = buildCellValue(project.getDefaultbillingrate());
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			Tr trRow4 = new Tr();
			Td cell41 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_ACTUAL_START_DATE));
			String actualStartDate = DateTimeUtils
					.converToStringWithUserTimeZone(
							project.getActualstartdate(), timeZone);
			Td cell42 = buildCellValue(actualStartDate);
			Td cell43 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_TARGET_BUDGET));
			Td cell44 = buildCellValue(project.getTargetbudget());
			trRow4.appendChild(cell41, cell42, cell43, cell44);
			tooltipManager.appendRow(trRow4);

			Tr trRow5 = new Tr();
			Td cell51 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_ACTUAL_END_DATE));
			String actualEndDate = DateTimeUtils
					.converToStringWithUserTimeZone(project.getActualenddate(),
							timeZone);
			Td cell52 = buildCellValue(actualEndDate);
			Td cell53 = buildCellName(LocalizationHelper.getMessage(locale,
					ProjectI18nEnum.FORM_ACTUAL_BUDGET));
			Td cell54 = buildCellValue(project.getActualbudget());
			trRow5.appendChild(cell51, cell52, cell53, cell54);
			tooltipManager.appendRow(trRow5);

			Tr trRow6 = new Tr();
			Td cell61 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell62 = buildCellValue(trimHtmlTags(project.getDescription()));
			cell62.setAttribute("colspan", "3");
			trRow6.appendChild(cell61, cell62);
			tooltipManager.appendRow(trRow6);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipMilestone(Locale locale,
			SimpleMilestone milestone, String siteURL, String timeZone) {
		if (milestone == null)
			return generateTolltipNull(locale);

		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(milestone.getName());

			Tr trRow2 = new Tr();
			Td cell21 = buildCellName(LocalizationHelper.getMessage(locale,
					MilestoneI18nEnum.FORM_START_DATE_FIELD));
			String startDate = DateTimeUtils.converToStringWithUserTimeZone(
					milestone.getStartdate(), timeZone);
			Td cell22 = buildCellValue(startDate);
			Td cell23 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_ASSIGNEE));
			String assignUserLink = (milestone.getOwner() != null) ? AccountLinkGenerator
					.generatePreviewFullUserLink(siteURL, milestone.getOwner())
					: "";
			String assignUserAvatarLink = SiteConfiguration.getAvatarLink(
					milestone.getOwnerAvatarId(), 16);
			Td cell24 = buildCellLink(assignUserLink, assignUserAvatarLink,
					milestone.getOwnerFullName());
			trRow2.appendChild(cell21, cell22, cell23, cell24);
			tooltipManager.appendRow(trRow2);

			Tr trRow3 = new Tr();
			Td cell31 = buildCellName(LocalizationHelper.getMessage(locale,
					MilestoneI18nEnum.FORM_END_DATE_FIELD));
			String endDate = DateTimeUtils.converToStringWithUserTimeZone(
					milestone.getEnddate(), timeZone);
			Td cell32 = buildCellValue(endDate);
			Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
					MilestoneI18nEnum.FORM_STATUS_FIELD));
			Td cell34 = buildCellValue(LocalizationHelper.getMessage(locale,
					MilestoneStatus.class, milestone.getStatus()));
			trRow3.appendChild(cell31, cell32, cell33, cell34);
			tooltipManager.appendRow(trRow3);

			Tr trRow4 = new Tr();
			Td cell41 = buildCellName(LocalizationHelper.getMessage(locale,
					MilestoneI18nEnum.FORM_TASK_FIELD));
			Td cell42 = buildCellValue(milestone.getNumTasks());
			Td cell43 = buildCellName(LocalizationHelper.getMessage(locale,
					MilestoneI18nEnum.FORM_BUG_FIELD));
			Td cell44 = buildCellValue(milestone.getNumBugs());
			trRow4.appendChild(cell41, cell42, cell43, cell44);
			tooltipManager.appendRow(trRow4);

			Tr trRow6 = new Tr();
			Td cell61 = buildCellName(LocalizationHelper.getMessage(locale,
					GenericI18Enum.FORM_DESCRIPTION));
			Td cell62 = buildCellValue(trimHtmlTags(milestone.getDescription()));
			cell62.setAttribute("colspan", "3");
			trRow6.appendChild(cell61, cell62);
			tooltipManager.appendRow(trRow6);

			return tooltipManager.create().write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipStandUp(Locale locale,
			SimpleStandupReport standup, String siteURL, String timeZone) {
		if (standup == null)
			return generateTolltipNull(locale);

		try {

			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 name = new H3();
			name.appendText(Jsoup.parse(
					DateTimeUtils.converToStringWithUserTimeZone(
							standup.getCreatedtime(), timeZone)).html());
			div.appendChild(name);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 165px; vertical-align: top; text-align: right;")
							.appendText(
									LocalizationHelper.getMessage(locale,
											StandupI18nEnum.STANDUP_LASTDAY)))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(standup.getWhatlastday()));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 165px;vertical-align: top; text-align: right;")
							.appendText(
									LocalizationHelper.getMessage(locale,
											StandupI18nEnum.STANDUP_TODAY)))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(standup.getWhattoday()));
			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 165px;vertical-align: top; text-align: right;")
							.appendText(
									LocalizationHelper.getMessage(locale,
											StandupI18nEnum.STANDUP_ISSUE)))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(standup.getWhatproblem()));

			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			div.appendChild(table);

			return div.write();

		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipMessage(Locale locale,
			SimpleMessage message, String siteURL, String timeZone) {
		if (message == null)
			return generateTolltipNull(locale);

		try {
			TooltipBuilder tooltipManager = new TooltipBuilder();
			tooltipManager.setTitle(message.getTitle());

			Tr trRow2 = new Tr();
			Td cell21 = new Td()
					.setStyle(
							"vertical-align: top; text-align: left;word-wrap: break-word; white-space: normal;vertical-align: top;")
					.appendText(
							StringUtils.trim(message.getMessage(), 500, true));

			trRow2.appendChild(cell21);
			tooltipManager.appendRow(trRow2);

			return tooltipManager.create().write();

		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project tooltip",
					e);
			return null;
		}
	}
}
