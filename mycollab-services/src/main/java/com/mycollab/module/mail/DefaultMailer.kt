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
import com.mycollab.configuration.EmailConfiguration
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import org.apache.commons.mail.EmailConstants
import org.apache.commons.mail.EmailException
import org.apache.commons.mail.HtmlEmail
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class DefaultMailer(private val emailConf: EmailConfiguration) : IMailer {

    private fun getBasicEmail(fromEmail: String, fromName: String, toEmail: List<MailRecipientField>,
                              ccEmail: List<MailRecipientField>?, bccEmail: List<MailRecipientField>?,
                              subject: String, html: String): HtmlEmail {
        try {
            val email = HtmlEmail()
            email.hostName = emailConf.host
            email.setSmtpPort(emailConf.port!!)
            email.isStartTLSEnabled = emailConf.isStartTls
            email.isSSLOnConnect = emailConf.isSsl
            email.setFrom(fromEmail, fromName)
            email.setCharset(EmailConstants.UTF_8)
            toEmail.forEach {
                when {
                    isValidate(it.email) && isValidate(it.name) -> email.addTo(it.email, it.name)
                    else -> LOG.error("Invalid cc email input: ${it.email}---${it.email}")
                }
            }

            if (ccEmail!= null) {
                ccEmail.forEach {
                    when {
                        isValidate(it.email) && isValidate(it.name) -> email.addCc(it.email, it.name)
                        else -> LOG.error("Invalid cc email input: ${it.email}---${it.email}")
                    }
                }
            }

            if (bccEmail!= null) {
                bccEmail.forEach {
                    when {
                        isValidate(it.email) && isValidate(it.name) -> email.addBcc(it.email, it.name)
                        else -> LOG.error("Invalid cc email input: ${it.email}---${it.email}")
                    }
                }
            }

            if (emailConf.user != null) {
                email.setAuthentication(emailConf.user, emailConf.password)
            }

            email.subject = subject

            if (StringUtils.isNotBlank(html)) {
                email.setHtmlMsg(html)
                email.setTextMsg(StringUtils.convertHtmlToPlainText(html))
            }

            return email
        } catch (e: EmailException) {
            throw MyCollabException(e)
        }

    }

    override fun sendHTMLMail(fromEmail: String, fromName: String, toEmails: List<MailRecipientField>, ccEmails: List<MailRecipientField>?,
                     bccEmails: List<MailRecipientField>?, subject: String, html: String) {
        try {
            val email = getBasicEmail(fromEmail, fromName, toEmails, ccEmails, bccEmails, subject, html)
            email.send()
        } catch (e: EmailException) {
            throw MyCollabException(e)
        }

    }

    override fun sendHTMLMail(fromEmail: String, fromName: String, toEmails: List<MailRecipientField>,
                     ccEmails: List<MailRecipientField>?, bccEmails: List<MailRecipientField>?,
                     subject: String, html: String, attachments: List<AttachmentSource>?) {
        try {
            when (attachments) {
                null -> sendHTMLMail(fromEmail, fromName, toEmails, ccEmails, bccEmails, subject, html)
                else -> {
                    val email = getBasicEmail(fromEmail, fromName, toEmails, ccEmails, bccEmails, subject, html)
                    attachments.forEach { email.attach(it.attachmentObj) }
                    email.send()
                }
            }
        } catch (e: EmailException) {
            throw MyCollabException(e)
        }

    }

    private fun isValidate(value: String): Boolean {
        return StringUtils.isNotBlank(value)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DefaultMailer::class.java)
    }
}
