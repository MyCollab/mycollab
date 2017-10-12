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
