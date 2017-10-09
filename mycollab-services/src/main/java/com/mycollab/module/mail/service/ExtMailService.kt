package com.mycollab.module.mail.service

import com.mycollab.common.domain.MailRecipientField
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.mail.AttachmentSource

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ExtMailService : IService {
    val isMailSetupValid: Boolean

    /**
     * @param fromEmail
     * @param fromName
     * @param toEmail
     * @param subject
     * @param html
     */
    fun sendHTMLMail(fromEmail: String, fromName: String, toEmail: List<MailRecipientField>, subject: String, html: String)

    fun sendHTMLMail(fromEmail: String, fromName: String, toEmail: List<MailRecipientField>,
                     ccEmail: List<MailRecipientField>?, bccEmail: List<MailRecipientField>?, subject: String, html: String)

    /**
     * @param fromEmail
     * @param fromName
     * @param toEmail
     * @param ccEmail
     * @param bccEmail
     * @param subject
     * @param html
     * @param attachments
     * @param canRetry
     */
    fun sendHTMLMail(fromEmail: String, fromName: String,
                     toEmail: List<MailRecipientField>, ccEmail: List<MailRecipientField>?,
                     bccEmail: List<MailRecipientField>?, subject: String, html: String,
                     attachments: List<AttachmentSource>?, canRetry: Boolean)
}
