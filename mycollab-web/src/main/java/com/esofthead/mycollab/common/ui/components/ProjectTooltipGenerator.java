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
package com.esofthead.mycollab.common.ui.components;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Img;
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

	public static String generateTolltipNull() {
		Div div = new Div();
		Table table = new Table();
		table.setStyle("padding-left:10px;  color: #5a5a5a; font-size:11px;");

		Tr trRow1 = new Tr();
		trRow1.appendChild(new Td().setStyle(
				"vertical-align: top; text-align: left;").appendText(
				"The item is not existed"));

		table.appendChild(trRow1);
		div.appendChild(table);

		return div.write();
	}

	public static String generateToolTipTask(SimpleTask task, String siteURL,
			String timeZone) {
		try {
			if (task == null) {
				return generateTolltipNull();
			}
			String beforeStr = "<html><head><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"></head><body>";
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 taksName = new H3();
			taksName.appendText(Jsoup.parse(task.getTaskname()).html());
			div.appendChild(taksName);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Start Date:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(
									task.getStartdate(), timeZone)));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Actual Start Date:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(
									task.getActualstartdate(), timeZone)));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("End Date:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(task.getEnddate(),
									timeZone)));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Actual End Date:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(
									task.getActualenddate(), timeZone)));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Deadline:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(task.getDeadline(),
									timeZone)));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Priority:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(task
															.getPriority())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(task.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	task.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					task.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(task
																			.getAssignUserFullName()))));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("TaskGroup:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(task.getTaskListName() != null) ? siteURL
															+ "#"
															+ ProjectLinkUtils
																	.generateTaskGroupPreviewLink(
																			task.getProjectid(),
																			task.getTasklistid())
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(task
																			.getTaskListName()))));

			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Complete(%):"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(task
													.getPercentagecomplete())));
			Tr trRow6 = new Tr();
			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(task.getNotes()));
			trRow6_value.setAttribute("colspan", "3");
			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Notes:")).appendChild(trRow6_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			table.appendChild(trRow6);
			div.appendChild(table);
			String afterStr = "</body></html>";
			return beforeStr + div.write() + afterStr;
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project-task tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipBug(SimpleBug bug, String siteURL,
			String timeZone) {
		try {
			if (bug == null) {
				return generateTolltipNull();
			}
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 bugSummary = new H3();
			bugSummary.appendText(Jsoup.parse(bug.getSummary()).html());
			div.appendChild(bugSummary);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			Td trRow1_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(bug
									.getDescription()));
			trRow1_value.setAttribute("colspan", "3");
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow1_value);

			Tr trRow2 = new Tr();
			Td trRow2_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(bug
									.getEnvironment()));
			trRow2_value.setAttribute("colspan", "3");
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Environment:")).appendChild(
					trRow2_value);

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(bug
													.getStatus())));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Priority:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(bug
													.getPriority())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Severity:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(bug
													.getSeverity())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Resolution:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(bug
													.getResolution())));

			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Due Date:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(bug.getDuedate(),
									timeZone)));
			trRow5.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Created Time:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(
									bug.getCreatedtime(), timeZone)));

			// Assignee

			Tr trRow6 = new Tr();
			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Logged by:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(bug.getLogby() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	(bug.getLogby() != null) ? bug
																			.getLogby()
																			: "")
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					bug.getLoguserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.trimHtmlTags(bug
																			.getLoguserFullName()))));
			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(bug.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	(bug.getAssignuser() != null) ? bug
																			.getAssignuser()
																			: "")
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					bug.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(bug
																			.getAssignuserFullName()))));

			Tr trRow7 = new Tr();
			Td trRow7_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendChild(
							new A().setHref(
									(bug.getMilestoneid() != null) ? siteURL
											+ "#"
											+ ProjectLinkUtils
													.generateMilestonePreviewLink(
															bug.getProjectid(),
															bug.getMilestoneid())
											: "").appendText(
									StringUtils.getStringFieldValue(bug
											.getMilestoneName())));

			trRow7_value.setAttribute("colspan", "3");
			trRow7.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Phase name:")).appendChild(
					trRow7_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			table.appendChild(trRow6);
			table.appendChild(trRow7);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project-bug tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipRisk(SimpleRisk risk, String siteURL,
			String timeZone) {
		try {
			if (risk == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 riskName = new H3();
			riskName.appendText(risk.getRiskname());
			div.appendChild(riskName);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");

			Tr trRow5 = new Tr();
			Td trRow5_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(risk
									.getDescription()));
			trRow5_value.setAttribute("colspan", "3");

			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow5_value);

			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Raised by:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(risk.getRaisedbyuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	risk.getRaisedbyuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					risk.getRaisedByUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(risk
																			.getRaisedByUserFullName()))));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("Consequence:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(risk
															.getConsequence())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(risk.getAssigntouser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	risk.getAssigntouser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					risk.getAssignToUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(risk
																			.getAssignedToUserFullName()))));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Probability:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(risk
															.getProbalitity())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Date due:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															risk.getDatedue(),
															timeZone)));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Rating:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(risk
															.getLevel())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(risk
															.getStatus())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Related to:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(""));

			Tr trRow6 = new Tr();
			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(risk
									.getResponse()));
			trRow6_value.setAttribute("colspan", "3");

			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Response:")).appendChild(trRow6_value);

			table.appendChild(trRow5);
			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow6);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for Risk in TooptipGeneratorServlet",
					e);
			return null;
		}
	}

	public static String generateToolTipProblem(SimpleProblem problem,
			String siteURL, String timeZone) {
		try {
			if (problem == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 problemName = new H3();
			problemName.appendText(problem.getIssuename());
			div.appendChild(problemName);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");

			Tr trRow5 = new Tr();
			Td trRow5_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(problem
									.getDescription()));
			trRow5_value.setAttribute("colspan", "3");

			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow5_value);

			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Raised by:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(problem.getRaisedbyuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	problem.getRaisedbyuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					problem.getRaisedByUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(problem
																			.getRaisedByUserFullName()))));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("Impact:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(problem
															.getImpact())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(problem.getAssigntouser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	problem.getAssigntouser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					problem.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(problem
																			.getAssignedUserFullName()))));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Priority:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(problem
															.getPriority())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Date due:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															problem.getDatedue(),
															timeZone)));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Rating:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(problem
															.getLevel())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(problem
															.getStatus())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Related to:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(""));

			Tr trRow6 = new Tr();
			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(problem
									.getResolution()));
			trRow6_value.setAttribute("colspan", "3");

			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Resolution:")).appendChild(
					trRow6_value);

			table.appendChild(trRow5);
			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow6);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error(
					"Error while generator tooltip for Problem in TooltipGenertor Servlet",
					e);
			return null;
		}
	}

	public static String generateToolTipVersion(SimpleVersion version,
			String siteURL, String timeZone) {
		try {
			if (version == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 versionName = new H3();
			versionName
					.appendText(Jsoup.parse(version.getVersionname()).text());
			div.appendChild(versionName);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :300px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Version Name:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(version
															.getVersionname())));

			Tr trRow2 = new Tr();

			Td trRow2_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(version
									.getDescription()));
			trRow2_value.setAttribute("colspan", "3");
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow2_value);
			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Due Date:")).appendChild(
					new Td().appendText(DateTimeUtils
							.converToStringWithUserTimeZone(
									version.getDuedate(), timeZone)));

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate tooltip for Version", e);
			return null;
		}
	}

	public static String generateToolTipComponent(SimpleComponent component,
			String siteURL, String timeZone) {
		try {
			if (component == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 componentName = new H3();
			componentName.appendText(Jsoup.parse(component.getComponentname())
					.text());
			div.appendChild(componentName);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :300px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Component Name:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(component
															.getComponentname())));

			Tr trRow2 = new Tr();
			Td trRow2_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(component
									.getDescription()));
			trRow2_value.setAttribute("colspan", "3");
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow2_value);
			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Lead:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(component.getUserlead() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	component
																			.getUserlead())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					component
																							.getUserLeadAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(component
																			.getUserLeadFullName()))));

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate tooltip for Component", e);
			return null;
		}
	}

	public static String generateToolTipTaskList(SimpleTaskList taskList,
			String siteURL, String timeZone) {
		try {
			if (taskList == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 bugSummary = new H3();
			bugSummary.appendText(Jsoup.parse(taskList.getName()).text());
			div.appendChild(bugSummary);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			Td trRow1_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringFieldValue(taskList.getName()));
			trRow1_value.setAttribute("colspan", "3");
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("TaskGroup:"))
					.appendChild(trRow1_value);

			Tr trRow2 = new Tr();
			Td trRow2_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(taskList
									.getDescription()));
			trRow2_value.setAttribute("colspan", "3");
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow2_value);
			// Assignee
			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(taskList.getOwner() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	(taskList
																			.getOwner() != null) ? taskList
																			.getOwner()
																			: "")
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					taskList.getOwnerAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(taskList
																			.getOwnerFullName()))));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Related Milestone:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													siteURL
															+ "#"
															+ ProjectLinkUtils
																	.generateMilestonePreviewLink(
																			taskList.getProjectid(),
																			taskList.getId()))
													.appendText(
															StringUtils
																	.getStringFieldValue(taskList
																			.getMilestoneName()))));

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate tooltip for TaskGroup", e);
			return null;
		}
	}

	public static String generateToolTipProject(SimpleProject project,
			String siteURL, String timeZone) {
		try {
			if (project == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 taksName = new H3();
			taksName.appendText(Jsoup.parse(project.getName()).html());
			div.appendChild(taksName);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Home Page:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(project.getHomepage() != null) ? project
															.getHomepage() : "")
													.appendText(
															StringUtils
																	.getStringFieldValue(project
																			.getHomepage()))));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(project
															.getProjectstatus())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Plan Start Date:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															project.getPlanstartdate(),
															timeZone)));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Currency:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											(project.getCurrency() != null) ? StringUtils
													.getStringFieldValue(project
															.getCurrency()
															.getSymbol())
													: ""));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Plan End Date:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															project.getPlanenddate(),
															timeZone)));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Rate:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(project
															.getDefaultbillingrate())));
			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Actual Start Date:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															project.getActualstartdate(),
															timeZone)));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Target Budget:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(project
															.getTargetbudget())));
			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Actual End Date:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															project.getActualenddate(),
															timeZone)));
			trRow5.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Actual Budget:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(project
															.getActualbudget())));
			Tr trRow6 = new Tr();
			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(project
									.getDescription()));
			trRow6_value.setAttribute("colspan", "3");
			trRow6.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow6_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			table.appendChild(trRow6);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipMilestone(SimpleMilestone milestone,
			String siteURL, String timeZone) {
		try {
			if (milestone == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 milestoneName = new H3();
			milestoneName.appendText(Jsoup.parse(milestone.getName()).html());
			div.appendChild(milestoneName);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("Start Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															milestone
																	.getStartdate(),
															timeZone)));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(milestone.getOwner() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	(milestone
																			.getOwner() != null) ? milestone
																			.getOwner()
																			: "")
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					milestone
																							.getOwnerAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(milestone
																			.getOwnerFullName()))));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("End Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															milestone
																	.getEnddate(),
															timeZone)));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(milestone
															.getStatus())));
			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("Tasks:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(milestone
															.getNumTasks())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Bugs:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(milestone
															.getNumBugs())));
			Tr trRow6 = new Tr();
			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(milestone
									.getDescription()));
			trRow6_value.setAttribute("colspan", "3");
			trRow6.appendChild(
					new Td().setStyle(
							"width: 80px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow6_value);

			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow6);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project tooltip",
					e);
			return null;
		}
	}

	public static String generateToolTipStandUp(SimpleStandupReport standup,
			String siteURL, String timeZone) {
		try {
			if (standup == null)
				return generateTolltipNull();
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
							.appendText("What I did in the last day/week:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(standup.getWhatlastday()));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 165px;vertical-align: top; text-align: right;")
							.appendText("What I will do today/week:"))
					.appendChild(
							new Td().setStyle(
									"break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(standup.getWhattoday()));
			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 165px;vertical-align: top; text-align: right;")
							.appendText("Do you have roadblocks?:"))
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

	public static String generateToolTipMessage(SimpleMessage message,
			String siteURL, String timeZone) {
		try {
			if (message == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 name = new H3();
			name.appendText(Jsoup.parse(message.getTitle()).html());
			div.appendChild(name);

			Table table = new Table();
			table.setStyle("padding-left:10px; width :500px;  color: #5a5a5a; font-size:11px;white-space: nowrap;	");

			Tr trRow2 = new Tr();
			trRow2.appendChild(new Td()
					.setStyle(
							"vertical-align: top; text-align: left;word-wrap: break-word; white-space: normal;vertical-align: top;")
					.appendText(
							StringUtils.trim(message.getMessage(), 500, true)));

			table.appendChild(trRow2);
			div.appendChild(table);

			return div.write();

		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project tooltip",
					e);
			return null;
		}
	}

}
