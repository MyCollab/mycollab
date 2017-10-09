package com.mycollab.module.mail

import com.mycollab.core.MyCollabException
import org.apache.commons.io.FileUtils
import org.apache.commons.mail.EmailAttachment

import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class FileAttachmentSource : AttachmentSource {
    private var file: File? = null
    private var name: String? = null

    constructor(file: File) : this(null, file)

    constructor(name: String?, file: File) {
        this.file = file
        this.name = name
    }

    constructor(name: String, inputStream: InputStream) {
        this.name = name
        try {
            this.file = File.createTempFile("mycollab", "tmp")
            FileUtils.copyInputStreamToFile(inputStream, file!!)
        } catch (e: IOException) {
            throw MyCollabException(e)
        }

    }

    override val attachmentObj: EmailAttachment
        get() {
            val attachment = EmailAttachment()
            attachment.path = file!!.path
            attachment.disposition = EmailAttachment.ATTACHMENT
            attachment.name = if (name == null) file!!.name else name
            return attachment
        }
}
