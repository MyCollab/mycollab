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

import java.util.Locale

import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import com.mycollab.common.UrlEncodeDecoder
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.module.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @version 5.2.5
  */
@Component class ResetUserPasswordCommand extends GenericCommand {
  @Autowired var extMailService: ExtMailService = _
  @Autowired var userService: UserService = _
  @Autowired var contentGenerator: IContentGenerator = _

  @AllowConcurrentEvents
  @Subscribe
  def execute(event: RequestToResetPasswordEvent): Unit = {
    val username = event.username
    if (username != null) {
      val user = userService.findUserByUserName(username)
      val subDomain = "api"
      val recoveryPasswordURL = SiteConfiguration.getSiteUrl(subDomain) + "user/recoverypassword/" +
        UrlEncodeDecoder.encode(username)
      val locale: Locale = LocalizationHelper.getLocaleInstance(user.getLanguage)
      contentGenerator.putVariable("username", user.getUsername)
      contentGenerator.putVariable("urlRecoveryPassword", recoveryPasswordURL)
      val recipient = new MailRecipientField(user.getEmail, user.getUsername)
      val lst = List[MailRecipientField](recipient)
      import scala.collection.JavaConversions._
      extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, lst, null, null,
        LocalizationHelper.getMessage(locale, UserI18nEnum.MAIL_RECOVERY_PASSWORD_SUBJECT,
          SiteConfiguration.getDefaultSiteName),
        contentGenerator.parseFile("mailUserRecoveryPasswordNotifier.ftl", locale), null)
    }
  }
}
