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
