/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.servlet

import com.google.common.base.MoreObjects
import com.mycollab.core.utils.TimezoneVal
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.crm.CrmTooltipGenerator
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.service.*
import com.mycollab.module.project.ProjectTooltipGenerator
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.service.*
import com.mycollab.module.tracker.service.BugService
import com.mycollab.module.tracker.service.ComponentService
import com.mycollab.module.tracker.service.VersionService
import com.mycollab.module.user.AdminTypeConstants
import com.mycollab.module.user.CommonTooltipGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.spring.AppContextUtil
import org.slf4j.LoggerFactory
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = ["/tooltip/*"], name = "tooltipGeneratorServlet")
class TooltipGeneratorServletRequestHandler : GenericHttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        try {
            val type = request.getParameter("type")
            val typeId = request.getParameter("typeId")
            val sAccountId = Integer.parseInt(request.getParameter("sAccountId"))
            val siteURL = request.getParameter("siteURL")
            val timeZoneId = request.getParameter("timeZone")
            val timeZone = TimezoneVal.valueOf(timeZoneId)
            val username = request.getParameter("username")
            val localeParam = request.getParameter("locale")
            val locale = LocalizationHelper.getLocaleInstance(localeParam)
            val dateFormat = MoreObjects.firstNonNull(request.getParameter("dateFormat"), "MM/dd/yyyy")

            var html: String? = ""
            when (type) {
                ProjectTypeConstants.PROJECT -> {
                    val service = AppContextUtil.getSpringBean(ProjectService::class.java)
                    val project = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipProject(locale, dateFormat, project, siteURL, timeZone)
                }
                ProjectTypeConstants.MESSAGE -> {
                    val service = AppContextUtil.getSpringBean(MessageService::class.java)
                    val message = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipMessage(locale, message, siteURL, timeZone)
                }
                ProjectTypeConstants.MILESTONE -> {
                    val service = AppContextUtil.getSpringBean(MilestoneService::class.java)
                    val mileStone = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipMilestone(locale, dateFormat, mileStone, siteURL, timeZone, false)
                }
                ProjectTypeConstants.BUG -> {
                    val service = AppContextUtil.getSpringBean(BugService::class.java)
                    val bug = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipBug(locale, dateFormat, bug, siteURL, timeZone, false)
                }
                ProjectTypeConstants.TASK -> {
                    val service = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
                    val task = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipTask(locale, dateFormat, task, siteURL, timeZone, false)
                }
                ProjectTypeConstants.RISK -> {
                    val service = AppContextUtil.getSpringBean(RiskService::class.java)
                    val risk = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipRisk(locale, dateFormat, risk, siteURL, timeZone, false)
                }
                ProjectTypeConstants.BUG_VERSION -> {
                    val service = AppContextUtil.getSpringBean(VersionService::class.java)
                    val version = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipVersion(locale, dateFormat, version, siteURL, timeZone)
                }
                ProjectTypeConstants.BUG_COMPONENT -> {
                    val service = AppContextUtil.getSpringBean(ComponentService::class.java)
                    val component = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipComponent(locale, component, siteURL, timeZone)
                }
                ProjectTypeConstants.PAGE -> {
                    val pageService = AppContextUtil.getSpringBean(ProjectPageService::class.java)
                    val page = pageService.getPage(typeId, username)
                    html = ProjectTooltipGenerator.generateToolTipPage(locale, page, siteURL, timeZone)
                }
                ProjectTypeConstants.STANDUP -> {
                    val service = AppContextUtil.getSpringBean(StandupReportService::class.java)
                    val standup = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = ProjectTooltipGenerator.generateToolTipStandUp(locale, dateFormat, standup, siteURL, timeZone)
                }
                CrmTypeConstants.ACCOUNT -> {
                    val service = AppContextUtil.getSpringBean(AccountService::class.java)
                    val account = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateToolTipAccount(locale, account, siteURL)
                }
                CrmTypeConstants.CONTACT -> {
                    val service = AppContextUtil.getSpringBean(ContactService::class.java)
                    val contact = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateToolTipContact(locale, dateFormat, contact, siteURL, timeZone)
                }
                CrmTypeConstants.CAMPAIGN -> {
                    val service = AppContextUtil.getSpringBean(CampaignService::class.java)
                    val account = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateTooltipCampaign(locale, dateFormat, account, siteURL, timeZone)
                }
                CrmTypeConstants.LEAD -> {
                    val service = AppContextUtil.getSpringBean(LeadService::class.java)
                    val lead = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateTooltipLead(locale, lead, siteURL, timeZone)
                }
                CrmTypeConstants.OPPORTUNITY -> {
                    val service = AppContextUtil.getSpringBean(OpportunityService::class.java)
                    val opportunity = service.findById(
                            Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateTooltipOpportunity(locale, dateFormat, opportunity, siteURL, timeZone)
                }
                CrmTypeConstants.CASE -> {
                    val service = AppContextUtil.getSpringBean(CaseService::class.java)
                    val cases = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateTooltipCases(locale, cases, siteURL, timeZone)
                }
                CrmTypeConstants.MEETING -> {
                    val service = AppContextUtil.getSpringBean(MeetingService::class.java)
                    val meeting = service.findById(
                            Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateToolTipMeeting(locale, dateFormat, meeting, siteURL, timeZone)
                }
                CrmTypeConstants.CALL -> {
                    val service = AppContextUtil.getSpringBean(CallService::class.java)
                    val call = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateToolTipCall(locale, dateFormat, call, siteURL, timeZone)
                }
                CrmTypeConstants.TASK -> {
                    val service = AppContextUtil.getSpringBean(TaskService::class.java)
                    val crmTask = service.findById(Integer.parseInt(typeId), sAccountId)
                    html = CrmTooltipGenerator.generateToolTipCrmTask(locale, dateFormat,
                            crmTask, siteURL, timeZone)
                }
                AdminTypeConstants.USER -> {
                    val service = AppContextUtil.getSpringBean(UserService::class.java)
                    val user = service.findUserByUserNameInAccount(username, sAccountId)
                    html = CommonTooltipGenerator.generateTooltipUser(locale, user, siteURL, timeZone)
                }
                else -> LOG.error("Can not generate tooltip for item has type $type")
            }

            response.characterEncoding = "UTF-8"
            response.contentType = "text/html;charset=UTF-8"
            val out = response.writer
            out.println(html)
        } catch (e: Exception) {
            LOG.error("Error while get html tooltip attachForm TooltipGeneratorServletRequestHandler", e)
            val out = response.writer
            out.println("")
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TooltipGeneratorServletRequestHandler::class.java)
    }
}
