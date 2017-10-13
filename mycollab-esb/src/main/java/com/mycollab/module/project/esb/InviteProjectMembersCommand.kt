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
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.RandomPasswordGenerator
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.billing.RegisterStatusConstants
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.ProjectMemberStatusConstants
import com.mycollab.module.project.domain.ProjectMember
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.project.service.ProjectService
import com.mycollab.module.user.domain.User
import com.mycollab.module.user.service.RoleService
import com.mycollab.module.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class InviteProjectMembersCommand(private val userService: UserService,
                                  private val roleService: RoleService,
                                  private val deploymentMode: IDeploymentMode,
                                  private val extMailService: ExtMailService,
                                  private val projectService: ProjectService,
                                  private val projectMemberService: ProjectMemberService,
                                  private val contentGenerator: IContentGenerator) : GenericCommand() {
    companion object {
        val LOG = LoggerFactory.getLogger(InviteProjectMembersCommand::class.java)
    }

    @AllowConcurrentEvents
    @Subscribe
    fun inviteUsers(event: InviteProjectMembersEvent) {
        val project = projectService.findById(event.projectId, event.sAccountId)
        val user = userService.findUserInAccount(event.inviteUser, event.sAccountId)
        val billingAccount = projectService.getAccountInfoOfProject(event.projectId)

        if (project!= null && user != null) {
            contentGenerator.putVariable("inviteUser", user.displayName)
            contentGenerator.putVariable("inviteMessage", event.inviteMessage)
            contentGenerator.putVariable("project", project)
            contentGenerator.putVariable("password", "")
            contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(billingAccount.id, billingAccount.logopath))

            event.emails.forEach { inviteeEmail ->
                val invitee = userService.findUserInAccount(inviteeEmail, event.sAccountId)
                contentGenerator.putVariable("inviteeEmail", inviteeEmail)
                if (invitee != null) {
                    if (RegisterStatusConstants.ACTIVE != invitee.registerstatus) {
                        userService.updateUserAccountStatus(inviteeEmail, event.sAccountId, RegisterStatusConstants.ACTIVE)
                    }
                } else {
                    val systemGuestRoleId = roleService.getDefaultRoleId(event.sAccountId)
                    if (systemGuestRoleId == null) {
                        LOG.error("Can not find the guess role of account ", event.sAccountId)
                    }

                    val newUser = User()
                    newUser.email = inviteeEmail
                    val password = RandomPasswordGenerator.generateRandomPassword()
                    contentGenerator.putVariable("password", password)
                    newUser.password = password
                    userService.saveUserAccount(newUser, systemGuestRoleId, billingAccount.subdomain, event.sAccountId, event.inviteUser, false)
                }
                val projectMember = projectMemberService.findMemberByUsername(inviteeEmail, event.projectId, event.sAccountId)
                if (projectMember != null) {
                    if (ProjectMemberStatusConstants.ACTIVE != projectMember.status) {
                        projectMember.status = ProjectMemberStatusConstants.NOT_ACCESS_YET
                    } else {
                        return
                    }
                    if (event.projectRoleId == null || event.projectRoleId!! < 0) {
                        projectMember.isadmin = true
                        projectMember.projectroleid = null
                    } else {
                        projectMember.isadmin = false
                        projectMember.projectroleid = event.projectRoleId
                    }
                    projectMemberService.updateWithSession(projectMember, "")
                } else {
                    val member = ProjectMember()
                    member.projectid = event.projectId
                    member.username = inviteeEmail
                    member.joindate = Date()
                    member.saccountid = event.sAccountId
                    member.billingrate = project.defaultbillingrate
                    member.overtimebillingrate = project.defaultovertimebillingrate
                    member.status = ProjectMemberStatusConstants.NOT_ACCESS_YET
                    if (event.projectRoleId == null || event.projectRoleId!! < 0) {
                        member.isadmin = true
                        member.projectroleid = null
                    } else {
                        member.isadmin = false
                        member.projectroleid = event.projectRoleId
                    }
                    projectMemberService.saveWithSession(member, "")
                }
                contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(Locale.US, MailI18nEnum.Copyright,
                        DateTimeUtils.getCurrentYear()))
                contentGenerator.putVariable("urlAccept", ProjectLinkGenerator.generateProjectFullLink(deploymentMode.getSiteUrl(billingAccount.getSubdomain()),
                        event.projectId))
                val subject = LocalizationHelper.getMessage(Locale.US, ProjectMemberI18nEnum.MAIL_INVITE_USERS_SUBJECT,
                        project.name, SiteConfiguration.getDefaultSiteName())
                val content = contentGenerator.parseFile("mailMemberInvitationNotifier.ftl", Locale.US)
                val toUser = listOf(MailRecipientField(inviteeEmail, inviteeEmail))
                extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), toUser, subject, content)
            }
        } else {
            LOG.error("Can not find user ${event.inviteUser} in account ${event.sAccountId}")
        }
    }
}