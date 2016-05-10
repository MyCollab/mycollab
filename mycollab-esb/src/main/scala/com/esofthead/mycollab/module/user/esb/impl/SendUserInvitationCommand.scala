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

import java.util.{Arrays, Locale}

import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.configuration.{SiteConfiguration, StorageFactory}
import com.esofthead.mycollab.html.{DivLessFormatter, LinkUtils}
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.billing.RegisterStatusConstants
import com.esofthead.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.esb.SendUserInvitationEvent
import com.esofthead.mycollab.module.user.esb.impl.SendUserInvitationCommand.Formatter
import com.esofthead.mycollab.module.user.service.UserService
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.hp.gagawa.java.elements.{Img, Text}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.2.2
  */
object SendUserInvitationCommand {

  class Formatter {
    def formatUser(user: SimpleUser): String = {
      if (user != null) {
        val img: Img = new Img("", StorageFactory.getInstance().getAvatarPath(user.getAvatarid, 16))
        new DivLessFormatter().appendChild(img, DivLessFormatter.EMPTY_SPACE, new Text(user.getDisplayName)).write
      } else {
        ""
      }
    }
  }

}

@Component class SendUserInvitationCommand extends GenericCommand {
  @Autowired var userService: UserService = _
  @Autowired var contentGenerator: IContentGenerator = _
  @Autowired var extMailService: ExtMailService = _

  @AllowConcurrentEvents
  @Subscribe
  def execute(event: SendUserInvitationEvent): Unit = {
    try {
      val inviterUser = userService.findUserByUserNameInAccount(event.inviter, event.sAccountId)
      contentGenerator.putVariable("urlAccept", LinkUtils.generateUserAcceptLink(event.subdomain,
        event.sAccountId, event.invitee))
      contentGenerator.putVariable("userName", event.invitee)
      contentGenerator.putVariable("formatter", new Formatter)
      contentGenerator.putVariable("inviter", inviterUser)
      extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getDefaultSiteName,
        Arrays.asList(new MailRecipientField(event.invitee, event.invitee)), null, null,
        contentGenerator.parseString(LocalizationHelper.getMessage(Locale.US,
          UserI18nEnum.MAIL_INVITE_USER_SUBJECT, SiteConfiguration.getDefaultSiteName)),
        contentGenerator.parseFile("templates/email/user/userInvitationNotifier.mt",
          Locale.US), null)
      userService.updateUserAccountStatus(event.invitee, event.sAccountId,
        RegisterStatusConstants.SENT_VERIFICATION_EMAIL)
    } catch {
      case e: Exception => {
        userService.updateUserAccountStatus(event.invitee, event.sAccountId, RegisterStatusConstants.VERIFICATING)
      }
    }
  }
}
