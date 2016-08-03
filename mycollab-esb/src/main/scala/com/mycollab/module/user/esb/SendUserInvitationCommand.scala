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
package com.mycollab.module.user.esb

import java.util.{Arrays, Locale}

import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.SimpleLogging
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.module.user.service.UserService
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
    val inviteeUser = userService.findUserByUserNameInAccount(event.invitee, event.sAccountId)
    if (inviteeUser != null) {
      contentGenerator.putVariable("siteUrl", SiteConfiguration.getSiteUrl(inviteeUser.getSubdomain))
      contentGenerator.putVariable("invitee", inviteeUser)
      contentGenerator.putVariable("password", event.password)
      extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName,
        Arrays.asList(new MailRecipientField(event.invitee, event.invitee)), null, null,
        LocalizationHelper.getMessage(Locale.US, UserI18nEnum.MAIL_INVITE_USER_SUBJECT,
          SiteConfiguration.getDefaultSiteName),
        contentGenerator.parseFile("mailUserInvitationNotifier.ftl", Locale.US), null)
    } else {
     SimpleLogging.error("Can not find the user with username %s in account %s".format(event.invitee, event.sAccountId))
    }
  }
}
