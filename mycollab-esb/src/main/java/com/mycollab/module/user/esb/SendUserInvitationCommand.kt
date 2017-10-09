package com.mycollab.module.user.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
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
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class SendUserInvitationCommand(private val userService: UserService,
                                private val contentGenerator: IContentGenerator,
                                private val extMailService: ExtMailService,
                                private val deploymentMode: IDeploymentMode) : GenericCommand() {
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
            extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(),
                    Collections.singletonList(MailRecipientField(event.invitee, event.invitee)),
                    LocalizationHelper.getMessage(Locale.US, UserI18nEnum.MAIL_INVITE_USER_SUBJECT, SiteConfiguration.getDefaultSiteName()),
                    contentGenerator.parseFile("mailUserInvitationNotifier.ftl", Locale.US))
        } else {
            LOG.error("Can not find the user with username ${event.invitee} in account ${event.sAccountId}")
        }
    }
}