package com.mycollab.module.mail

import org.apache.commons.mail.EmailAttachment

/**
 * Attachment source of email
 *
 * @author MyCollab Ltd
 * @since 1.0
 */
interface AttachmentSource {
    val attachmentObj: EmailAttachment
}
