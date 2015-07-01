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

import java.util.Locale

import com.esofthead.mycollab.common.UrlEncodeDecoder
import com.esofthead.mycollab.common.domain.{MailRecipientField, RelayEmailWithBLOBs}
import com.esofthead.mycollab.configuration.{LocaleHelper, SiteConfiguration}
import com.esofthead.mycollab.i18n.LocalizationHelper
import com.esofthead.mycollab.module.mail.IContentGenerator
import com.esofthead.mycollab.module.mail.service.ExtMailService
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.esofthead.mycollab.module.user.domain.User
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.user.SendingRecoveryPasswordEmailAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Component
class SendingRecoveryPasswordEmailActionImpl extends SendingRecoveryPasswordEmailAction {

    @Autowired var extMailService: ExtMailService = _

    @Autowired var userService: UserService = _

    @Autowired var contentGenerator: IContentGenerator = _

    def sendEmail(relayEmail: RelayEmailWithBLOBs) {
        val username: String = relayEmail.getRecipients
        if (username != null) {
            val user: User = userService.findUserByUserName(username)
            val subDomain: String = "api"
            val recoveryPasswordURL: String = SiteConfiguration.getSiteUrl(subDomain) + "user/recoverypassword/" +
                UrlEncodeDecoder.encode(username)
            val locale: Locale = LocaleHelper.toLocale(user.getLanguage)
            contentGenerator.putVariable("username", user.getUsername)
            contentGenerator.putVariable("urlRecoveryPassword", recoveryPasswordURL)
            val recipient: MailRecipientField = new MailRecipientField(user.getEmail, user.getUsername)
            val lst: List[MailRecipientField] = List[MailRecipientField](recipient)
            import scala.collection.JavaConversions._
            extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getDefaultSiteName, lst, null, null,
                contentGenerator.parseString(LocalizationHelper.getMessage(locale, UserI18nEnum.MAIL_RECOVERY_PASSWORD_SUBJECT,
                    SiteConfiguration.getDefaultSiteName)),
                contentGenerator.parseFile("templates/email/user/userRecoveryPasswordNotifier.mt", locale,
                    SiteConfiguration.getDefaultLocale), null)
        }
    }
}
