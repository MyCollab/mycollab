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
package com.mycollab.servlet;

import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.*;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.service.*;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.module.user.AdminTypeConstants;
import com.mycollab.module.user.CommonTooltipGenerator;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.service.*;
import com.mycollab.spring.AppContextUtil;
import com.google.common.base.MoreObjects;
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
            TimeZone timeZone = TimezoneVal.valueOf(timeZoneId);
            String username = request.getParameter("username");
            String localeParam = request.getParameter("locale");
            Locale locale = LocalizationHelper.getLocaleInstance(localeParam);
            String dateFormat = MoreObjects.firstNonNull(request.getParameter("dateFormat"), "MM/dd/yyyy");

            String html = "";
            if (ProjectTypeConstants.PROJECT.equals(type)) {
                ProjectService service = AppContextUtil.getSpringBean(ProjectService.class);
                SimpleProject project = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipProject(locale, dateFormat, project, siteURL, timeZone);
            } else if (ProjectTypeConstants.MESSAGE.equals(type)) {
                MessageService service = AppContextUtil.getSpringBean(MessageService.class);
                SimpleMessage message = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipMessage(locale, message, siteURL, timeZone);
            } else if (ProjectTypeConstants.MILESTONE.equals(type)) {
                MilestoneService service = AppContextUtil.getSpringBean(MilestoneService.class);
                SimpleMilestone mileStone = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipMilestone(locale, dateFormat, mileStone, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.BUG.equals(type)) {
                BugService service = AppContextUtil.getSpringBean(BugService.class);
                SimpleBug bug = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipBug(locale, dateFormat, bug, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.TASK.equals(type)) {
                ProjectTaskService service = AppContextUtil.getSpringBean(ProjectTaskService.class);
                SimpleTask task = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipTask(locale, dateFormat, task, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.RISK.equals(type)) {
                RiskService service = AppContextUtil.getSpringBean(RiskService.class);
                SimpleRisk risk = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipRisk(locale, dateFormat, risk, siteURL, timeZone, false);
            } else if (ProjectTypeConstants.BUG_VERSION.equals(type)) {
                VersionService service = AppContextUtil.getSpringBean(VersionService.class);
                SimpleVersion version = service.findById(Integer.parseInt(typeId), sAccountId);
                html = ProjectTooltipGenerator.generateToolTipVersion(locale, dateFormat, version, siteURL, timeZone);
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
                html = ProjectTooltipGenerator.generateToolTipStandUp(locale, dateFormat, standup, siteURL, timeZone);
            } else if (CrmTypeConstants.ACCOUNT.equals(type)) {
                AccountService service = AppContextUtil.getSpringBean(AccountService.class);
                SimpleAccount account = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipAccount(locale, account, siteURL);
            } else if (CrmTypeConstants.CONTACT.equals(type)) {
                ContactService service = AppContextUtil.getSpringBean(ContactService.class);
                SimpleContact contact = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipContact(locale, dateFormat, contact, siteURL, timeZone);
            } else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
                CampaignService service = AppContextUtil.getSpringBean(CampaignService.class);
                SimpleCampaign account = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipCampaign(locale, dateFormat, account, siteURL, timeZone);
            } else if (CrmTypeConstants.LEAD.equals(type)) {
                LeadService service = AppContextUtil.getSpringBean(LeadService.class);
                SimpleLead lead = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipLead(locale, lead, siteURL, timeZone);
            } else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
                OpportunityService service = AppContextUtil.getSpringBean(OpportunityService.class);
                SimpleOpportunity opportunity = service.findById(
                        Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipOpportunity(locale, dateFormat, opportunity, siteURL, timeZone);
            } else if (CrmTypeConstants.CASE.equals(type)) {
                CaseService service = AppContextUtil.getSpringBean(CaseService.class);
                SimpleCase cases = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateTooltipCases(locale, cases, siteURL, timeZone);
            } else if (CrmTypeConstants.MEETING.equals(type)) {
                MeetingService service = AppContextUtil.getSpringBean(MeetingService.class);
                SimpleMeeting meeting = service.findById(
                        Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipMeeting(locale, dateFormat, meeting, siteURL, timeZone);
            } else if (CrmTypeConstants.CALL.equals(type)) {
                CallService service = AppContextUtil.getSpringBean(CallService.class);
                SimpleCall call = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipCall(locale, dateFormat, call, siteURL, timeZone);
            } else if (CrmTypeConstants.TASK.equals(type)) {
                TaskService service = AppContextUtil.getSpringBean(TaskService.class);
                SimpleCrmTask crmTask = service.findById(Integer.parseInt(typeId), sAccountId);
                html = CrmTooltipGenerator.generateToolTipCrmTask(locale, dateFormat,
                        crmTask, siteURL, timeZone);
            } else if (AdminTypeConstants.USER.equals(type)) {
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
