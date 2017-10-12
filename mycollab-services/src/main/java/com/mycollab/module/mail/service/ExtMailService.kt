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
