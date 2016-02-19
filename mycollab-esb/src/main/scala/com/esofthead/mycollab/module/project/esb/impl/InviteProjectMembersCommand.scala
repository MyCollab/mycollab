/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.esb.impl

import java.util
import java.util.Date

import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.esofthead.mycollab.module.project.ProjectLinkGenerator
import com.esofthead.mycollab.module.project.domain.{SimpleProject, SimpleProjectMember}
import com.esofthead.mycollab.module.project.esb.InviteProjectMembersEvent
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum
import com.esofthead.mycollab.module.project.service.ProjectService
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 1.0.0
  */
@Component class InviteProjectMembersCommand extends GenericCommand {
  @Autowired private val userService: UserService = null
  @Autowired private val extMailService: ExtMailService = null
  @Autowired private val projectService: ProjectService = null
  @Autowired private val contentGenerator: IContentGenerator = null

  @AllowConcurrentEvents
  @Subscribe
  def inviteUsers(event: InviteProjectMembersEvent): Unit = {
    val project = projectService.findById(event.projectId, event.sAccountId)
    val user = userService.findUserByUserNameInAccount(event.inviteUser, event.sAccountId)
    val member = new SimpleProjectMember
    member.setProjectName(project.getName)
    contentGenerator.putVariable("member", member)
    contentGenerator.putVariable("inviteUser", user.getDisplayName)
    contentGenerator.putVariable("inviteMessage", event.inviteMessage)
    val subDomain = projectService.getSubdomainOfProject(event.projectId)
    val date: Date = new Date
    for (inviteeEmail <- event.emails) {
      contentGenerator.putVariable("urlAccept", SiteConfiguration.getSiteUrl(subDomain) + "project/member/invitation/confirm_invite/" +
        ProjectLinkGenerator.generateAcceptInvitationParams(inviteeEmail, event.sAccountId, event.projectId,
          event.projectRoleId, user.getEmail, event.inviteUser, date))
      contentGenerator.putVariable("urlDeny", SiteConfiguration.getSiteUrl(subDomain) + "project/member/invitation/deny_invite/" +
        ProjectLinkGenerator.generateDenyInvitationParams(inviteeEmail, event.sAccountId, event.projectId, user.getEmail,
          event.inviteUser))
      val subject = contentGenerator.parseString(LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale,
        ProjectMemberI18nEnum.MAIL_INVITE_USERS_SUBJECT, member.getProjectName, SiteConfiguration.getDefaultSiteName))
      val content = contentGenerator.parseFile("templates/email/project/memberInvitationNotifier.mt",
        SiteConfiguration.getDefaultLocale)
      val toUser = util.Arrays.asList(new MailRecipientField(inviteeEmail, inviteeEmail))
      extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getDefaultSiteName,
        toUser, null, null, subject, content, null)
    }
  }
}