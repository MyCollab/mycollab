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
package com.mycollab.module.project.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.hp.gagawa.java.elements.A
import com.mycollab.common.FontAwesomeUtils
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.html.DivLessFormatter
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.user.service.BillingAccountService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class NewProjectMemberJoinCommand(private val billingAccountService: BillingAccountService,
                                  private val projectMemberService: ProjectMemberService,
                                  private val extMailService: ExtMailService,
                                  private val contentGenerator: IContentGenerator,
                                  private val deploymentMode: IDeploymentMode,
                                  private val applicationConfiguration: ApplicationConfiguration) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun newMemberJoinHandler(event: NewProjectMemberJoinEvent) {
        val projectId = event.projectId
        val sAccountId = event.sAccountId
        val newMember = projectMemberService.findMemberByUsername(event.username, projectId, sAccountId)
        val membersInProjects = projectMemberService.getActiveUsersInProject(projectId, sAccountId)
        val account = billingAccountService.getAccountById(sAccountId)
        if (account != null && newMember != null) {
            contentGenerator.putVariable("newMember", newMember)
            contentGenerator.putVariable("formatter", Formatter())
            contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(Locale.US, MailI18nEnum.Copyright,
                    DateTimeUtils.getCurrentYear()))
            contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(account.id, account.logopath))
            contentGenerator.putVariable("siteUrl", deploymentMode.getSiteUrl(account.subdomain))
            val recipients = mutableListOf<MailRecipientField>()
            membersInProjects.forEach {
                if (event.username != it.username)
                    recipients.add(MailRecipientField(it.username, it.displayName))
            }
            extMailService.sendHTMLMail(applicationConfiguration.notifyEmail, applicationConfiguration.siteName, recipients,
                    "${newMember.displayName} has just joined on project ${newMember.projectName}",
                    contentGenerator.parseFile("mailProjectNewMemberJoinProjectNotifier.ftl", Locale.US))
        } else {
            LOG.error("Error while inform the new member join $account $newMember")
        }
    }

    companion object {
        val LOG = LoggerFactory.getLogger(NewProjectMemberJoinCommand::class.java)

        class Formatter {
            fun formatProjectLink(siteUrl: String, newMember: SimpleProjectMember): String =
                    DivLessFormatter().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.PROJECT)).appendChild(DivLessFormatter.EMPTY_SPACE, A(ProjectLinkGenerator.generateProjectFullLink(siteUrl,
                            newMember.projectid)).appendText(newMember.projectName)).write()


            fun formatMemberLink(siteUrl: String, newMember: SimpleProjectMember): String =
                    A(ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, newMember.projectid, newMember.username)).appendText(newMember.displayName).write()

            fun formatRoleName(siteUrl: String, newMember: SimpleProjectMember): String =
                    if (newMember.isProjectOwner) "Project Owner"
                    else A(ProjectLinkGenerator.generateRolePreviewFullLink(siteUrl, newMember.projectid,
                            newMember.projectid)).appendText(newMember.roleName).write()
        }
    }
}