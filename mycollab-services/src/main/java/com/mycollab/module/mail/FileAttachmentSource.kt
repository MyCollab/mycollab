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
