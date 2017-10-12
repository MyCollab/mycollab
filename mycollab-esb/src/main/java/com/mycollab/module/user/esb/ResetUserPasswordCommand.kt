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
package com.mycollab.module.user.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.UrlEncodeDecoder
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.module.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class ResetUserPasswordCommand(private val extMailService: ExtMailService,
                               private val userService: UserService,
                               private val contentGenerator: IContentGenerator,
                               private val deploymentMode: IDeploymentMode) : GenericCommand() {
    companion object {
        val LOG = LoggerFactory.getLogger(ResetUserPasswordCommand::class.java)
    }

    @AllowConcurrentEvents
    @Subscribe
    fun execute(event: RequestToResetPasswordEvent) {
        val username = event.username
        val user = userService.findUserByUserName(username)
        if (user != null) {
            val subDomain = "api"
            val recoveryPasswordURL = deploymentMode.getSiteUrl(subDomain) + "user/recoverypassword/" +
                    UrlEncodeDecoder.encode(username)
            val locale = LocalizationHelper.getLocaleInstance(user.language)
            contentGenerator.putVariable("username", user.username)
            contentGenerator.putVariable("urlRecoveryPassword", recoveryPasswordURL)
            contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(locale, MailI18nEnum.Copyright,
                    DateTimeUtils.getCurrentYear()))
            val recipient = MailRecipientField(user.email, user.username)
            val recipientFields = listOf(recipient)

            extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(),
                    recipientFields,
                    LocalizationHelper.getMessage(locale, UserI18nEnum.MAIL_RECOVERY_PASSWORD_SUBJECT,
                            SiteConfiguration.getDefaultSiteName()),
                    contentGenerator.parseFile("mailUserRecoveryPasswordNotifier.ftl", locale))
        } else {
            LOG.error("Can not reset the password of username $username because this user is not existed")
        }
    }
}