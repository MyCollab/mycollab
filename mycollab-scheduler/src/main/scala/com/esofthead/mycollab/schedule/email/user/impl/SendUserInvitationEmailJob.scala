/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email.user.impl

import java.util.Arrays

import com.esofthead.mycollab.common.UrlEncodeDecoder
import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.html.LinkUtils
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.mail.IContentGenerator
import com.esofthead.mycollab.module.mail.service.ExtMailService
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.esofthead.mycollab.module.user.dao.{UserAccountInvitationMapper, UserAccountInvitationMapperExt}
import com.esofthead.mycollab.module.user.domain.SimpleUserAccountInvitation
import com.esofthead.mycollab.schedule.jobs.GenericQuartzJobBean
import org.quartz.{JobExecutionContext, JobExecutionException}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE) class SendUserInvitationEmailJob extends GenericQuartzJobBean {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[SendUserInvitationEmailJob])
  @Autowired var userAccountInvitationMapper: UserAccountInvitationMapper = _
  @Autowired var userAccountInvitationMapperExt: UserAccountInvitationMapperExt = _
  @Autowired var contentGenerator: IContentGenerator = _
  @Autowired var extMailService: ExtMailService = _

  @throws(classOf[JobExecutionException])
  def executeJob(context: JobExecutionContext) {
    import scala.collection.JavaConverters._
    val invitations: List[SimpleUserAccountInvitation] = userAccountInvitationMapperExt.findAccountInvitations(RegisterStatusConstants.VERIFICATING).asScala.toList

    for (invitation <- invitations) {
      LOG.debug("Send invitation email to user {} of subdomain {}", Array(invitation.getUsername, invitation
        .getSubdomain))
      contentGenerator.putVariable("invitation", invitation)
      contentGenerator.putVariable("urlAccept", LinkUtils.generateUserAcceptLink(invitation.getSubdomain, invitation.getAccountid, invitation.getUsername))
      val inviterName: String = invitation.getInviterFullName
      val inviterMail: String = invitation.getInviteuser
      val subdomain: String = invitation.getSubdomain
      contentGenerator.putVariable("urlDeny", LinkUtils.generateUserDenyLink(invitation.getSubdomain, invitation.getAccountid, invitation.getUsername, inviterName, inviterMail))
      val userName: String = if (invitation.getUsername != null) invitation.getUsername else "there"
      contentGenerator.putVariable("userName", userName)
      contentGenerator.putVariable("inviterName", inviterName)
      extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getSiteName, Arrays.asList(new MailRecipientField(invitation.getUsername, invitation.getUsername)), null, null, contentGenerator.generateSubjectContent(LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale, UserI18nEnum.MAIL_INVITE_USER_SUBJECT, SiteConfiguration.getSiteName)), contentGenerator.generateBodyContent("templates/email/user/userInvitationNotifier.mt", SiteConfiguration.getDefaultLocale), null)
      invitation.setInvitationstatus(RegisterStatusConstants.SENT_VERIFICATION_EMAIL)
      userAccountInvitationMapper.updateByPrimaryKeySelective(invitation)
    }
  }
}
