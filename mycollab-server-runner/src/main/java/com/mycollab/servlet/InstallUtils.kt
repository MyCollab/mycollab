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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.servlet

import com.mycollab.core.UserInvalidInputException
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
object InstallUtils {
    @JvmStatic
    fun checkSMTPConfig(host: String, port: Int, username: String, password: String, auth: Boolean, isStartTls: Boolean, isSSL: Boolean) {
        try {
            val props = Properties()
            when {
                auth -> props.setProperty("mail.smtp.auth", "true")
                else -> props.setProperty("mail.smtp.auth", "false")
            }
            when {
                isStartTls -> {
                    props.setProperty("mail.smtp.starttls.enable", "true")
                    props.setProperty("mail.smtp.startssl.enable", "true")
                }
                isSSL -> {
                    props.setProperty("mail.smtp.startssl.enable", "false")
                    props.setProperty("mail.smtp.ssl.enable", "true")
                    props.setProperty("mail.smtp.ssl.socketFactory.fallback", "false")
                }
            }

            val email = SimpleEmail()
            email.hostName = host
            email.setSmtpPort(port)
            email.setAuthenticator(DefaultAuthenticator(username, password))
            email.isStartTLSEnabled = isStartTls
            email.isSSLOnConnect = isSSL

            email.setFrom(username)
            email.subject = "MyCollab Test Email"
            email.setMsg("This is a test mail ... :-)")
            email.addTo(username)
            email.send()
        } catch (e: Exception) {
            throw UserInvalidInputException(e)
        }
    }
}
