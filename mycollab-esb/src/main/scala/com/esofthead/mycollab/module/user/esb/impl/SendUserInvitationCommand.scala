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
package com.esofthead.mycollab.module.user.esb.impl

import java.util.Arrays

import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.html.LinkUtils
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.esofthead.mycollab.module.user.esb.SendUserInvitationEvent
import com.esofthead.mycollab.module.user.service.UserService
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
@Component class SendUserInvitationCommand extends GenericCommand {
    @Autowired var userService: UserService = _
    @Autowired var contentGenerator: IContentGenerator = _
    @Autowired var extMailService: ExtMailService = _

    @AllowConcurrentEvents
    @Subscribe
    def execute(event: SendUserInvitationEvent): Unit = {
        try {
            contentGenerator.putVariable("urlAccept", LinkUtils.generateUserAcceptLink(event.subdomain,
                event.accountId, event.inviterUsername))
            contentGenerator.putVariable("urlDeny", LinkUtils.generateUserDenyLink(event.subdomain,
                event.accountId, event.inviterUsername, event.inviterFullName, event.inviterUsername))
            contentGenerator.putVariable("userName", event.inviterUsername)
            contentGenerator.putVariable("inviterName", event.inviterFullName)
            extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getDefaultSiteName,
                Arrays.asList(new MailRecipientField(event.inviterUsername, event.inviterUsername)), null, null,
                contentGenerator.parseString(LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale,
                    UserI18nEnum.MAIL_INVITE_USER_SUBJECT, SiteConfiguration.getDefaultSiteName)),
                contentGenerator.parseFile("templates/email/user/userInvitationNotifier.mt",
                    SiteConfiguration.getDefaultLocale), null)
            userService.updateUserAccountStatus(event.inviterUsername, event.accountId,
                RegisterStatusConstants.SENT_VERIFICATION_EMAIL)
        } catch {
            case e: Exception => {
                userService.updateUserAccountStatus(event.inviterUsername, event.accountId, RegisterStatusConstants.VERIFICATING)
            }
        }
    }
}
