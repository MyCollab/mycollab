package com.mycollab.module.mail

import com.mycollab.common.domain.MailRecipientField
import com.mycollab.module.user.domain.SimpleUser

/**
 * Abstract mailer. Note: in MyCollab we now support send HTML content email
 * only.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface IMailer {
    /**
     * @param fromEmail
     * @param fromName
     * @param toEmails
     * @param ccEmails
     * @param bccEmails
     * @param subject
     * @param html
     */
    fun sendHTMLMail(fromEmail: String, fromName: String, toEmails: List<MailRecipientField>, ccEmails: List<MailRecipientField>?,
                     bccEmails: List<MailRecipientField>?, subject: String, html: String)

    /**
     * @param fromEmail
     * @param fromName
     * @param toEmails
     * @param ccEmails
     * @param bccEmails
     * @param subject
     * @param html
     * @param attachments
     */
    fun sendHTMLMail(fromEmail: String, fromName: String, toEmails: List<MailRecipientField>, ccEmails: List<MailRecipientField>?,
                     bccEmails: List<MailRecipientField>?, subject: String, html: String, attachments: List<AttachmentSource>?)
}
