package com.mycollab.module.mail

import org.apache.commons.mail.EmailAttachment

import java.net.URL

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
class UrlAttachmentSource(private val name: String, private val url: URL) : AttachmentSource {

    override val attachmentObj: EmailAttachment
        get() {
            val attachment = EmailAttachment()
            attachment.url = url
            attachment.disposition = EmailAttachment.ATTACHMENT
            attachment.name = name
            return attachment
        }
}
