package com.mycollab.module.mail

import com.mycollab.common.domain.MailRecipientField
import com.mycollab.core.utils.StringUtils
import org.slf4j.LoggerFactory

/**
 * Dummy a mailer in case email configuration not properly set.
 *
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class NullMailer : IMailer {

    override fun sendHTMLMail(fromEmail: String, fromName: String, toEmails: List<MailRecipientField>, ccEmails: List<MailRecipientField>?,
                              bccEmails: List<MailRecipientField>?, subject: String, html: String) {
        LOG.info("You has not configured the email server. So Email feature is disable \n $subject \n Html Email: $html \n Text Email: ${StringUtils.convertHtmlToPlainText(html)}")
    }

    override fun sendHTMLMail(fromEmail: String, fromName: String, toEmails: List<MailRecipientField>, ccEmails: List<MailRecipientField>?,
                              bccEmails: List<MailRecipientField>?, subject: String, html: String,
                              attachments: List<AttachmentSource>?) {
        LOG.info("You has not configured the email server. So Email feature is disable \n $subject \n Html Email: $html \n Text Email: ${StringUtils.convertHtmlToPlainText(html)}")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(NullMailer::class.java)
    }

}
