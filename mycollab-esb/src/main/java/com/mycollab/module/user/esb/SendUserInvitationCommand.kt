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
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.module.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class SendUserInvitationCommand(private val userService: UserService,
                                private val contentGenerator: IContentGenerator,
                                private val extMailService: ExtMailService,
                                private val deploymentMode: IDeploymentMode,
                                private val applicationConfiguration: ApplicationConfiguration) : GenericCommand() {
    companion object {
        val LOG = LoggerFactory.getLogger(SendUserInvitationCommand::class.java)
    }

    @AllowConcurrentEvents
    @Subscribe
    fun execute(event: SendUserInvitationEvent) {
        val inviteeUser = userService.findUserInAccount(event.invitee, event.sAccountId)
        if (inviteeUser != null) {
            contentGenerator.putVariable("siteUrl", deploymentMode.getSiteUrl(inviteeUser.subdomain))
            contentGenerator.putVariable("invitee", inviteeUser)
            contentGenerator.putVariable("password", event.password)
            contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(Locale.US, MailI18nEnum.Copyright,
                    DateTimeUtils.getCurrentYear()))
            extMailService.sendHTMLMail(applicationConfiguration.notifyEmail, applicationConfiguration.siteName,
                    Collections.singletonList(MailRecipientField(event.invitee, event.invitee)),
                    LocalizationHelper.getMessage(Locale.US, UserI18nEnum.MAIL_INVITE_USER_SUBJECT, applicationConfiguration.siteName),
                    contentGenerator.parseFile("mailUserInvitationNotifier.ftl", Locale.US))
        } else {
            LOG.error("Can not find the user with username ${event.invitee} in account ${event.sAccountId}")
        }
    }
}