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
package com.esofthead.mycollab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.ui.components.CrmTooltipGenerator;
import com.esofthead.mycollab.common.ui.components.ProjectTooltipGenerator;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.i18n.LocaleUtils;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CallService;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.service.TaskService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProblemService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.module.project.service.StandupReportService;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("tooltipGeneratorServlet")
public class AnnotatedTooltipGeneratorServletRequestHandler extends
		GenericServletRequestHandler {
	private static Logger log = LoggerFactory
			.getLogger(AnnotatedTooltipGeneratorServletRequestHandler.class);

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String type = request.getParameter("type");
			Integer typeid = (request.getParameter("typeId") != null) ? Integer
					.parseInt(request.getParameter("typeId")) : 0;
			Integer sAccountId = (request.getParameter("sAccountId") != null) ? Integer
					.parseInt(request.getParameter("sAccountId")) : 0;
			String siteURL = request.getParameter("siteURL");
			String timeZone = request.getParameter("timeZone");
			String username = request.getParameter("username");
			String localeParam = request.getParameter("locale");
			Locale locale = LocaleUtils.toLocale(localeParam);

			String html = "";
			if (ProjectTypeConstants.PROJECT.equals(type)) {
				ProjectService service = ApplicationContextUtil
						.getSpringBean(ProjectService.class);
				SimpleProject project = service.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipProject(locale,
						project, siteURL, timeZone);
			} else if (ProjectTypeConstants.MESSAGE.equals(type)) {
				MessageService service = ApplicationContextUtil
						.getSpringBean(MessageService.class);
				SimpleMessage message = service.findMessageById(typeid,
						sAccountId);
				html = ProjectTooltipGenerator.generateToolTipMessage(locale,
						message, siteURL, timeZone);
			} else if (ProjectTypeConstants.MILESTONE.equals(type)) {
				MilestoneService service = ApplicationContextUtil
						.getSpringBean(MilestoneService.class);
				SimpleMilestone mileStone = service
						.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipMilestone(locale,
						mileStone, siteURL, timeZone);
			} else if (ProjectTypeConstants.TASK_LIST.equals(type)) {
				ProjectTaskListService service = ApplicationContextUtil
						.getSpringBean(ProjectTaskListService.class);
				SimpleTaskList taskList = service.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipTaskList(locale,
						taskList, siteURL, timeZone);
			} else if (ProjectTypeConstants.BUG.equals(type)) {
				BugService service = ApplicationContextUtil
						.getSpringBean(BugService.class);
				SimpleBug bug = service.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipBug(locale, bug,
						siteURL, timeZone);
			} else if (ProjectTypeConstants.TASK.equals(type)) {
				ProjectTaskService service = ApplicationContextUtil
						.getSpringBean(ProjectTaskService.class);
				SimpleTask task = service.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipTask(locale,
						task, siteURL, timeZone);
			} else if (ProjectTypeConstants.RISK.equals(type)) {
				RiskService service = ApplicationContextUtil
						.getSpringBean(RiskService.class);
				SimpleRisk risk = service.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipRisk(locale,
						risk, siteURL, timeZone);
			} else if (ProjectTypeConstants.PROBLEM.equals(type)) {
				ProblemService service = ApplicationContextUtil
						.getSpringBean(ProblemService.class);
				SimpleProblem problem = service.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipProblem(locale,
						problem, siteURL, timeZone);
			} else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
				VersionService service = ApplicationContextUtil
						.getSpringBean(VersionService.class);
				SimpleVersion version = service.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipVersion(locale,
						version, siteURL, timeZone);
			} else if (ProjectTypeConstants.BUG_COMPONENT.equals(type)) {
				ComponentService service = ApplicationContextUtil
						.getSpringBean(ComponentService.class);
				SimpleComponent component = service
						.findById(typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipComponent(locale,
						component, siteURL, timeZone);
			} else if (ProjectTypeConstants.STANDUP.equals(type)) {
				StandupReportService service = ApplicationContextUtil
						.getSpringBean(StandupReportService.class);
				SimpleStandupReport standup = service.findStandupReportById(
						typeid, sAccountId);
				html = ProjectTooltipGenerator.generateToolTipStandUp(locale,
						standup, siteURL, timeZone);
			} else if ("Account".equals(type)) {
				AccountService service = ApplicationContextUtil
						.getSpringBean(AccountService.class);
				SimpleAccount account = service.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateToolTipAccount(locale,
						account, siteURL);
			} else if ("Contact".equals(type)) {
				ContactService service = ApplicationContextUtil
						.getSpringBean(ContactService.class);
				SimpleContact contact = service.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateToolTipContact(locale,
						contact, siteURL, timeZone);
			} else if ("Campaign".equals(type)) {
				CampaignService service = ApplicationContextUtil
						.getSpringBean(CampaignService.class);
				SimpleCampaign account = service.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateTooltipCampaign(locale,
						account, siteURL, timeZone);
			} else if ("Lead".equals(type)) {
				LeadService service = ApplicationContextUtil
						.getSpringBean(LeadService.class);
				SimpleLead lead = service.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateTooltipLead(locale, lead,
						siteURL, timeZone);
			} else if ("Opportunity".equals(type)) {
				OpportunityService service = ApplicationContextUtil
						.getSpringBean(OpportunityService.class);
				SimpleOpportunity opportunity = service.findById(typeid,
						sAccountId);
				html = CrmTooltipGenerator.generateTooltipOpportunity(locale,
						opportunity, siteURL, timeZone);
			} else if ("Case".equals(type)) {
				CaseService service = ApplicationContextUtil
						.getSpringBean(CaseService.class);
				SimpleCase cases = service.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateTooltipCases(locale, cases,
						siteURL, timeZone);
			} else if ("Meeting".equals(type)) {
				MeetingService service = ApplicationContextUtil
						.getSpringBean(MeetingService.class);
				SimpleMeeting meeting = service.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateToolTipMeeting(locale,
						meeting, siteURL, timeZone);
			} else if ("Call".equals(type)) {
				CallService service = ApplicationContextUtil
						.getSpringBean(CallService.class);
				SimpleCall call = service.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateToolTipCall(locale, call,
						siteURL, timeZone);
			} else if ("CRMTask".equals(type)) {
				TaskService service = ApplicationContextUtil
						.getSpringBean(TaskService.class);
				com.esofthead.mycollab.module.crm.domain.SimpleTask crmTask = service
						.findById(typeid, sAccountId);
				html = CrmTooltipGenerator.generateToolTipCrmTask(locale,
						crmTask, siteURL, timeZone);
			} else if ("User".equals(type)) {
				UserService service = ApplicationContextUtil
						.getSpringBean(UserService.class);
				SimpleUser user = service.findUserByUserNameInAccount(username,
						sAccountId);
				html = generateTooltipUser(user, siteURL, timeZone);
			} else {
				log.error("Can not generate tooltip for item has type " + type);
			}

			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(html);
			return;
		} catch (Exception e) {
			log.error(
					"Error while get html tooltip attachForm AnnotatedTooltipGeneratorServletRequestHandler",
					e);
			String html = null;
			PrintWriter out = response.getWriter();
			out.println(html);
			return;
		}
	}

	private String generateTooltipUser(SimpleUser user, String siteURL,
			String timeZone) {
		try {
			if (user == null)
				return null;
			Div div = new Div();
			H3 userFullName = new H3()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			userFullName.setStyle("padding-left:10px;").appendText(
					user.getDisplayName());
			div.appendChild(userFullName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :380px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Email:")).appendChild(
					new Td().setStyle("vertical-align: top;").appendChild(
							new A().setHref("mailto:" + user.getEmail())
									.appendText(
											StringUtils
													.getStringFieldValue(user
															.getEmail()))));

			Td trRow1_value = new Td().setStyle(
					"width:150px;text-align: right; vertical-align: top;")
					.appendChild(
							new Img("", UserAvatarControlFactory.getAvatarLink(
									user.getAvatarid(), 100)));
			trRow1_value.setAttribute("rowspan", "5");
			trRow1.appendChild(new Td().setStyle(
					"width: 0px; vertical-align: top; text-align: right;")
					.appendChild(trRow1_value));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Time:")).appendChild(
					new Td().setStyle("vertical-align: top;").appendText(
							TimezoneMapper.getTimezone(user.getTimezone())
									.getDisplayName()));
			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Country:"))
					.appendChild(
							new Td().setStyle("vertical-align: top;")
									.appendText(
											StringUtils
													.getStringFieldValue(user
															.getCountry())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Phone:")).appendChild(
					new Td().setStyle("vertical-align: top;")
							.appendText(
									StringUtils.getStringFieldValue(user
											.getWorkphone())));

			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Last access time:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils.getStringDateFromNow(user
													.getLastaccessedtime())));
			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error(
					"Error while generate tooltip for servlet project-task tooltip",
					e);
			return null;
		}
	}
}
