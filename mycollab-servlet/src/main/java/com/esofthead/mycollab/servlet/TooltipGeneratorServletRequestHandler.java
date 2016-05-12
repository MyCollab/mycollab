/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.servlet;

import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.service.*;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.service.*;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.module.user.CommonTooltipGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.AppContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = "/tooltip/*", name = "tooltipGeneratorServlet")
public class TooltipGeneratorServletRequestHandler extends GenericHttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TooltipGeneratorServletRequestHandler.class);

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String type = request.getParameter("type");
            String typeId = request.getParameter("typeId");
            Integer sAccountId = Integer.parseInt(request.getParameter("sAccountId"));
            String siteURL = request.getParameter("siteURL");
            String timeZoneId = request.getParameter("timeZone");
            TimeZone timeZone = TimezoneMapper.getTimezone(timeZoneId);
            String username = request.getParameter("username");
            String localeParam = request.getParameter("locale");
            Locale locale = LocalizationHelper.getLocaleInstance(localeParam);

            String html = "";
            if (ProjectTypeConstants.PROJECT.equals(type)) {
                ProjectService service = AppContextUtil.getSpringBean(ProjectService.class);
                SimpleProject project = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipProject(locale, project, siteURL, timeZone);
            } else if (ProjectTypeConstants.MESSAGE.equals(type)) {
                MessageService service = AppContextUtil.getSpringBean(MessageService.class);
                SimpleMessage message = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipMessage(locale, message, siteURL, timeZone);
            } else if (ProjectTypeConstants.MILESTONE.equals(type)) {
                MilestoneService service = AppContextUtil.getSpringBean(MilestoneService.class);
                SimpleMilestone mileStone = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipMilestone(locale, mileStone, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.BUG.equals(type)) {
                BugService service = AppContextUtil.getSpringBean(BugService.class);
                SimpleBug bug = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipBug(locale, bug, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.TASK.equals(type)) {
                ProjectTaskService service = AppContextUtil.getSpringBean(ProjectTaskService.class);
                SimpleTask task = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipTask(locale, task, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.RISK.equals(type)) {
                RiskService service = AppContextUtil.getSpringBean(RiskService.class);
                SimpleRisk risk = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipRisk(locale, risk, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
                VersionService service = AppContextUtil.getSpringBean(VersionService.class);
                SimpleVersion version = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipVersion(locale, version, siteURL, timeZone);
            } else if (ProjectTypeConstants.BUG_COMPONENT.equals(type)) {
                ComponentService service = AppContextUtil.getSpringBean(ComponentService.class);
                SimpleComponent component = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipComponent(locale, component, siteURL, timeZone);
            } else if (ProjectTypeConstants.PAGE.equals(type)) {
                ProjectPageService pageService = AppContextUtil.getSpringBean(ProjectPageService.class);
                Page page = pageService.getPage(typeId, username);
                html = ProjectTooltipGenerator.generateToolTipPage(locale, page, siteURL, timeZone);
            } else if (ProjectTypeConstants.STANDUP.equals(type)) {
                StandupReportService service = AppContextUtil.getSpringBean(StandupReportService.class);
                SimpleStandupReport standup = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipStandUp(locale, standup, siteURL, timeZone);
            } else if (CrmTypeConstants.ACCOUNT.equals(type)) {
                AccountService service = AppContextUtil.getSpringBean(AccountService.class);
                SimpleAccount account = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipAccount(locale, account, siteURL);
            } else if (CrmTypeConstants.CONTACT.equals(type)) {
                ContactService service = AppContextUtil.getSpringBean(ContactService.class);
                SimpleContact contact = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipContact(locale, contact, siteURL, timeZone);
            } else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
                CampaignService service = AppContextUtil.getSpringBean(CampaignService.class);
                SimpleCampaign account = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipCampaign(locale, account, siteURL, timeZone);
            } else if (CrmTypeConstants.LEAD.equals(type)) {
                LeadService service = AppContextUtil.getSpringBean(LeadService.class);
                SimpleLead lead = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipLead(locale, lead, siteURL, timeZone);
            } else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
                OpportunityService service = AppContextUtil.getSpringBean(OpportunityService.class);
                SimpleOpportunity opportunity = service.findById(
                        Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipOpportunity(locale, opportunity, siteURL, timeZone);
            } else if (CrmTypeConstants.CASE.equals(type)) {
                CaseService service = AppContextUtil.getSpringBean(CaseService.class);
                SimpleCase cases = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipCases(locale, cases, siteURL, timeZone);
            } else if (CrmTypeConstants.MEETING.equals(type)) {
                MeetingService service = AppContextUtil.getSpringBean(MeetingService.class);
                SimpleMeeting meeting = service.findById(
                        Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipMeeting(locale, meeting, siteURL, timeZone);
            } else if (CrmTypeConstants.CALL.equals(type)) {
                CallService service = AppContextUtil.getSpringBean(CallService.class);
                SimpleCall call = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipCall(locale, call, siteURL, timeZone);
            } else if (CrmTypeConstants.TASK.equals(type)) {
                TaskService service = AppContextUtil.getSpringBean(TaskService.class);
                com.esofthead.mycollab.module.crm.domain.SimpleTask crmTask = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipCrmTask(locale,
                        crmTask, siteURL, timeZone);
            } else if ("User".equals(type)) {
                UserService service = AppContextUtil.getSpringBean(UserService.class);
                SimpleUser user = service.findUserByUserNameInAccount(username, sAccountId);
                html = CommonTooltipGenerator.generateTooltipUser(locale, user, siteURL, timeZone);
            } else {
                LOG.error("Can not generate tooltip for item has type " + type);
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(html);
        } catch (Exception e) {
            LOG.error("Error while get html tooltip attachForm TooltipGeneratorServletRequestHandler", e);
            String html = null;
            PrintWriter out = response.getWriter();
            out.println(html);
        }
    }
}
