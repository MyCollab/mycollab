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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.schedule.email.service

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Div
import com.hp.gagawa.java.elements.Img
import com.mycollab.common.NotificationType
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.RangeDateSearchField
import com.mycollab.db.arguments.SearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.html.DivLessFormatter
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.ProjectResources
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.module.project.i18n.TicketI18nEnum
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.project.service.ProjectNotificationSettingService
import com.mycollab.module.project.service.ProjectTicketService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.schedule.jobs.GenericQuartzJobBean
import com.mycollab.spring.AppContextUtil
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class OverdueProjectTicketsNotificationJob : GenericQuartzJobBean() {

    @Autowired
    private lateinit var applicationConfiguration: ApplicationConfiguration

    @Autowired
    private lateinit var projectAssignmentService: ProjectTicketService

    @Autowired
    private lateinit var deploymentMode: IDeploymentMode

    @Autowired
    private lateinit var extMailService: ExtMailService

    @Autowired
    private lateinit var contentGenerator: IContentGenerator

    @Autowired
    private lateinit var projectMemberService: ProjectMemberService

    @Autowired
    private lateinit var projectNotificationService: ProjectNotificationSettingService

    @Throws(JobExecutionException::class)
    override fun executeJob(context: JobExecutionContext) {
        val searchCriteria = ProjectTicketSearchCriteria()
        searchCriteria.saccountid = null
        val now = LocalDate.now()
        val past = now.minusDays(10000)
        val rangeDate = RangeDateSearchField(past, now)
        searchCriteria.dateInRange = rangeDate
        searchCriteria.open = SearchField()
        val accounts = projectAssignmentService.getAccountsHasOverdueAssignments(searchCriteria)
        accounts.forEach { account ->
            searchCriteria.saccountid = NumberSearchField(account.id)
            contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(account.id, account.logopath))
            val projectIds = projectAssignmentService.getProjectsHasOverdueAssignments(searchCriteria)
            for (projectId in projectIds) {
                searchCriteria.projectIds = SetSearchField(projectId)
                val siteUrl = deploymentMode.getSiteUrl(account.subdomain)
                contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, projectId))
                val assignments = projectAssignmentService.findAbsoluteListByCriteria(searchCriteria, 0, Integer.MAX_VALUE)
                if (assignments.isNotEmpty()) {
                    val projectName = (assignments[0] as ProjectTicket).projectName
                    val notifiers = getNotifiersOfProject(projectId, account.id)
                    contentGenerator.putVariable("assignments", assignments)
                    contentGenerator.putVariable("siteUrl", siteUrl)
                    contentGenerator.putVariable("formatter", OverdueAssignmentFormatter())
                    notifiers.forEach {
                        val userMail = MailRecipientField(it.email, it.displayName)
                        val recipients = listOf(userMail)
                        val userLocale = LocalizationHelper.getLocaleInstance(it.language)
                        contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(userLocale, MailI18nEnum.Copyright,
                                DateTimeUtils.getCurrentYear()))
                        val projectSettingUrl = A(ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, projectId)).appendText(LocalizationHelper.getMessage(userLocale, MailI18nEnum.Project_Notification_Setting)).write()
                        val projectFooter = LocalizationHelper.getMessage(userLocale, MailI18nEnum.Project_Footer, projectName, projectSettingUrl)
                        contentGenerator.putVariable("Project_Footer", projectFooter)
                        val content = contentGenerator.parseFile("mailProjectOverdueAssignmentsNotifier.ftl", Locale.US)
                        val overdueAssignments = "${LocalizationHelper.getMessage(userLocale, TicketI18nEnum.VAL_OVERDUE_TICKETS)}(${assignments.size})"
                        contentGenerator.putVariable("overdueAssignments", overdueAssignments)
                        extMailService.sendHTMLMail(applicationConfiguration.notifyEmail, applicationConfiguration.siteName, recipients,
                                "[$projectName] $overdueAssignments", content)
                    }
                }
            }
        }
    }


    private fun getNotifiersOfProject(projectId: Int, accountId: Int): Set<SimpleUser> {
        var notifyUsers = projectMemberService.getActiveUsersInProject(projectId, accountId)
        val notificationSettings = projectNotificationService.findNotifications(projectId, accountId)
        if (notificationSettings.isNotEmpty()) {
            notificationSettings
                    .asSequence()
                    .filter { (NotificationType.None.name == it.level) || (NotificationType.Minimal.name == it.level) }
                    .forEach { setting -> notifyUsers = notifyUsers.filter { it.username != setting.username } }
        }
        return notifyUsers.toSet()
    }

    companion object {
        val LOG = LoggerFactory.getLogger(OverdueProjectTicketsNotificationJob::class.java)

        class OverdueAssignmentFormatter {
            fun formatDate(date: LocalDate?): String = DateTimeUtils.formatDate(date, "yyyy-MM-dd", Locale.US)

            fun formatLink(siteUrl: String, assignment: ProjectTicket): String {
                try {
                    return when (assignment.type) {
                        ProjectTypeConstants.BUG -> Div().appendText(ProjectResources.getFontIconHtml(ProjectTypeConstants.BUG)).appendChild(DivLessFormatter.EMPTY_SPACE, A(ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl,
                                assignment.extraTypeId, assignment.projectShortName)).appendText(assignment.name)).write()
                        ProjectTypeConstants.TASK -> Div().appendText(ProjectResources.getFontIconHtml(ProjectTypeConstants.TASK)).appendChild(DivLessFormatter.EMPTY_SPACE, A(ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl,
                                assignment.extraTypeId, assignment.projectShortName)).appendText(assignment.name)).write()
                        ProjectTypeConstants.RISK -> Div().appendText(ProjectResources.getFontIconHtml(ProjectTypeConstants.RISK)).appendChild(DivLessFormatter.EMPTY_SPACE, A(ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl,
                                assignment.projectId!!, assignment.typeId!!)).appendText(assignment.name)).write()
                        ProjectTypeConstants.MILESTONE -> Div().appendText(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE)).appendChild(DivLessFormatter.EMPTY_SPACE, A(ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl,
                                assignment.projectId!!, assignment.typeId!!)).appendText(assignment.name)).write()
                        else -> throw  MyCollabException("Do not support type $assignment.type")
                    }
                } catch (e: Exception) {
                    LOG.error("Error in format assignment $assignment")
                    return siteUrl
                }
            }

            fun formatAssignUser(siteUrl: String, assignment: ProjectTicket): String =
                    Div().appendChild(Img("", storageService().getAvatarPath(assignment.assignUserAvatarId, 16)),
                            A(AccountLinkGenerator.generatePreviewFullUserLink(siteUrl, assignment.assignUser)).appendText(assignment.assignUserFullName)).write()

            private fun storageService() = AppContextUtil.getSpringBean(AbstractStorageService::class.java)
        }
    }
}