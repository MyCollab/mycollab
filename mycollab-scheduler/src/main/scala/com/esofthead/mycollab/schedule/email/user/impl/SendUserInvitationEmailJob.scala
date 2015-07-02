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

import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.core.arguments.{SearchRequest, SetSearchField}
import com.esofthead.mycollab.html.LinkUtils
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.mail.IContentGenerator
import com.esofthead.mycollab.module.mail.service.ExtMailService
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria
import com.esofthead.mycollab.module.user.service.UserService
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
    @Autowired var userService: UserService = _
    @Autowired var contentGenerator: IContentGenerator = _
    @Autowired var extMailService: ExtMailService = _

    @throws(classOf[JobExecutionException])
    def executeJob(context: JobExecutionContext) {
        val searchCriteria = new UserSearchCriteria()
        searchCriteria.setRegisterStatuses(new SetSearchField[String](RegisterStatusConstants.VERIFICATING))
        searchCriteria.setSaccountid(null)
        import scala.collection.JavaConverters._
        val inviteUsers: List[Any] = userService.findPagableListByCriteria(new
                SearchRequest[UserSearchCriteria](searchCriteria, 0, Integer.MAX_VALUE)).asScala.toList
        for (item <- inviteUsers) {
            val invitedUser: SimpleUser = item.asInstanceOf[SimpleUser]
            LOG.debug("Send invitation email to user {} of subdomain {}", Array(invitedUser.getUsername, invitedUser.getSubdomain))
            contentGenerator.putVariable("invitation", invitedUser)
            contentGenerator.putVariable("urlAccept", LinkUtils.generateUserAcceptLink(invitedUser.getSubdomain,
                invitedUser.getAccountId, invitedUser.getUsername))
            val inviterName: String = invitedUser.getInviteUserFullName
            val inviterMail: String = invitedUser.getInviteUser
            contentGenerator.putVariable("urlDeny", LinkUtils.generateUserDenyLink(invitedUser.getSubdomain,
                invitedUser.getAccountId, invitedUser.getUsername, inviterName, inviterMail))
            val userName: String = if (invitedUser.getUsername != null) invitedUser.getUsername else invitedUser.getEmail
            contentGenerator.putVariable("userName", userName)
            contentGenerator.putVariable("inviterName", inviterName)
            extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getDefaultSiteName,
                Arrays.asList(new MailRecipientField(invitedUser.getUsername, invitedUser.getUsername)), null, null,
                contentGenerator.parseString(LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale,
                    UserI18nEnum.MAIL_INVITE_USER_SUBJECT, SiteConfiguration.getDefaultSiteName)),
                contentGenerator.parseFile("templates/email/user/userInvitationNotifier.mt",
                    SiteConfiguration.getDefaultLocale), null)
            userService.updateUserAccountStatus(invitedUser.getUsername, invitedUser.getAccountId,
                RegisterStatusConstants.SENT_VERIFICATION_EMAIL)
        }
    }
}
