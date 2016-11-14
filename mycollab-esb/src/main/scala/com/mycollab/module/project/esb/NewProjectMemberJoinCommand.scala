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
package com.mycollab.module.project.esb

import java.util.Locale

import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.hp.gagawa.java.elements.A
import com.mycollab.common.FontAwesomeUtils
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.html.{DivLessFormatter, LinkUtils}
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.esb.NewProjectMemberJoinCommand.Formatter
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.project.{ProjectLinkGenerator, ProjectTypeConstants}
import com.mycollab.module.user.service.BillingAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.collection.mutable.ListBuffer

/**
  * @author MyCollab Ltd
  * @since 5.2.6
  */
object NewProjectMemberJoinCommand {

  class Formatter {
    def formatProjectLink(siteUrl: String, newMember: SimpleProjectMember): String = {
      new DivLessFormatter().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.PROJECT)).
        appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateProjectFullLink(siteUrl,
          newMember.getProjectid)).appendText(newMember.getProjectName)).write()
    }

    def formatMemberLink(siteUrl: String, newMember: SimpleProjectMember): String = {
      new A(ProjectLinkGenerator.generateProjectMemberFullLink(siteUrl, newMember.getProjectid, newMember.getUsername)).
        appendText(newMember.getDisplayName).write()
    }

    def formatRoleName(siteUrl: String, newMember: SimpleProjectMember): String = {
      if (newMember.isProjectOwner) "Project Owner"
      else new A(ProjectLinkGenerator.generateRolePreviewFullLink(siteUrl, newMember.getProjectid,
        newMember.getProjectid)).appendText(newMember.getRoleName).write()
    }
  }

}

@Component class NewProjectMemberJoinCommand extends GenericCommand {
  @Autowired private val billingAccountService: BillingAccountService = null
  @Autowired private val projectMemberService: ProjectMemberService = null
  @Autowired private val extMailService: ExtMailService = null
  @Autowired private val contentGenerator: IContentGenerator = null

  @AllowConcurrentEvents
  @Subscribe
  def newMemberJoinHandler(event: NewProjectMemberJoinEvent): Unit = {
    val projectId = event.projectId
    val sAccountId = event.sAccountId
    val newMember = projectMemberService.findMemberByUsername(event.username, projectId, sAccountId)
    import scala.collection.JavaConverters._
    val membersInProjects = projectMemberService.getActiveUsersInProject(projectId, sAccountId).asScala.toList
    val account = billingAccountService.getAccountById(sAccountId)
    contentGenerator.putVariable("newMember", newMember)
    contentGenerator.putVariable("formatter", new Formatter)
    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(Locale.US, MailI18nEnum.Copyright,
      DateTimeUtils.getCurrentYear))
    contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(account.getId, account.getLogopath))
    contentGenerator.putVariable("siteUrl", SiteConfiguration.getSiteUrl(account.getSubdomain))
    val recipients = ListBuffer[MailRecipientField]()
    membersInProjects.foreach(user => {
      if (event.username != user.getUsername)
        recipients.append(new MailRecipientField(user.getUsername, user.getDisplayName))
    })
    extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients.asJava,
      String.format("%s has just joined on project %s", newMember.getDisplayName, newMember.getProjectName),
      contentGenerator.parseFile("mailProjectNewMemberJoinProjectNotifier.ftl", Locale.US))
  }
}
