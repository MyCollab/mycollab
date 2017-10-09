package com.mycollab.module.mail.service.impl

import com.mycollab.common.domain.MailRecipientField
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.mail.AttachmentSource
import com.mycollab.module.mail.DefaultMailer
import com.mycollab.module.mail.IMailer
import com.mycollab.module.mail.NullMailer
import com.mycollab.module.mail.service.ExtMailService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class ExtMailServiceImpl : ExtMailService {

    override val isMailSetupValid: Boolean
        get() {
            val emailConfiguration = SiteConfiguration.getEmailConfiguration()
            return StringUtils.isNotBlank(emailConfiguration.host) && StringUtils.isNotBlank(emailConfiguration.user)
                    && emailConfiguration.port > -1
        }

    private val mailer: IMailer
        get() {
            val emailConfiguration = SiteConfiguration.getEmailConfiguration()
            return if (!isMailSetupValid) {
                NullMailer()
            } else DefaultMailer(emailConfiguration)

        }

    override fun sendHTMLMail(fromEmail: String, fromName: String, toEmail: List<MailRecipientField>, subject: String, html: String) {
        mailer.sendHTMLMail(fromEmail, fromName, toEmail, null, null, subject, html, null)
    }

    override fun sendHTMLMail(fromEmail: String, fromName: String, toEmail: List<MailRecipientField>, ccEmail: List<MailRecipientField>?,
                              bccEmail: List<MailRecipientField>?, subject: String, html: String) {
        mailer.sendHTMLMail(fromEmail, fromName, toEmail, ccEmail, bccEmail, subject, html, null)
    }

    override fun sendHTMLMail(fromEmail: String, fromName: String,
                              toEmail: List<MailRecipientField>, ccEmail: List<MailRecipientField>?,
                              bccEmail: List<MailRecipientField>?, subject: String, html: String,
                              attachments: List<AttachmentSource>?, canRetry: Boolean) {
        mailer.sendHTMLMail(fromEmail, fromName, toEmail, ccEmail, bccEmail, subject, html, attachments)
    }
}
