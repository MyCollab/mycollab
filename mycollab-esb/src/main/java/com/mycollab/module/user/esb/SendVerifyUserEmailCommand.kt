package com.mycollab.module.user.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.GenericLinkUtils
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.billing.UserStatusConstants
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.module.user.domain.User
import com.mycollab.module.user.service.BillingAccountService
import com.mycollab.module.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class SendVerifyUserEmailCommand(private val deploymentMode: IDeploymentMode,
                                 private val billingAccountService: BillingAccountService,
                                 private val userService: UserService,
                                 private val extMailService: ExtMailService,
                                 private val contentGenerator: IContentGenerator,
                                 private val applicationConfiguration: ApplicationConfiguration) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun sendVerifyEmailRequest(event: SendUserEmailVerifyRequestEvent) {
        sendConfirmEmailToUser(event.sAccountId, event.user)
        event.user.status = UserStatusConstants.EMAIL_VERIFIED_REQUEST
        userService.updateWithSession(event.user, event.user.username)
    }

    fun sendConfirmEmailToUser(sAccountId:Int, user: User) {
        val account = billingAccountService.getAccountById(sAccountId)
        if (account != null) {
            contentGenerator.putVariable("user", user)
            val siteUrl = deploymentMode.getSiteUrl(account.subdomain)
            contentGenerator.putVariable("siteUrl", siteUrl)
            val confirmLink = GenericLinkUtils.generateConfirmEmailLink(siteUrl, user.username)
            contentGenerator.putVariable("linkConfirm", confirmLink)
            contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(Locale.US, MailI18nEnum.Copyright,
                    DateTimeUtils.getCurrentYear()))
            extMailService.sendHTMLMail(applicationConfiguration.notifyEmail, applicationConfiguration.siteName,
                    listOf(MailRecipientField(user.email, "${user.firstname} ${user.lastname}")),
                    LocalizationHelper.getMessage(Locale.US, UserI18nEnum.MAIL_CONFIRM_EMAIL_SUBJECT),
                    contentGenerator.parseFile("mailVerifyEmailUser.ftl", Locale.US))
        } else {
            LOG.error("Can not find account with id $sAccountId then can not send the verification email")
        }
    }

    companion object {
        val LOG = LoggerFactory.getLogger(SendVerifyUserEmailCommand::class.java)
    }
}