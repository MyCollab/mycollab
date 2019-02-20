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
package com.mycollab.logging

import ch.qos.logback.classic.net.SMTPAppender
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Layout
import ch.qos.logback.core.helpers.CyclicBuffer
import ch.qos.logback.core.util.OptionHelper
import com.mycollab.configuration.EmailConfiguration
import com.mycollab.core.Version
import com.mycollab.spring.AppContextUtil
import java.util.*
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
class MailAppender : SMTPAppender() {

    private var isSessionCreated = false

    override fun makeSubjectLayout(subjectStr: String?): Layout<ILoggingEvent> {
        var subjectStr = subjectStr
        if (subjectStr == null) {
            subjectStr = "MyCollab ${Version.getVersion()} - Error: %logger{20} - %m"
        }

        return super.makeSubjectLayout(subjectStr)
    }

    override fun sendBuffer(cb: CyclicBuffer<ILoggingEvent>?, lastEventObject: ILoggingEvent?) {
//        if (!isSessionCreated) {
            try {
                val emailConfiguration = AppContextUtil.getSpringBean(EmailConfiguration::class.java)
                val props = Properties(OptionHelper.getSystemProperties())
                isSessionCreated = true;
                this.from = emailConfiguration.username
                this.addTo(emailConfiguration.errorTo)

                props["mail.smtp.host"] = emailConfiguration.smtphost
                props["mail.smtp.port"] = emailConfiguration.port

                var loginAuthenticator: Authenticator? = null

                if (emailConfiguration.username != null) {
                    loginAuthenticator = object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication? {
                            return PasswordAuthentication(emailConfiguration.username, emailConfiguration.password)
                        }
                    }
                    props["mail.smtp.auth"] = "true"
                }

                if (emailConfiguration.ssl && emailConfiguration.startTls) {
                    addError("Both SSL and StartTLS cannot be enabled simultaneously")
                } else {
                    if (emailConfiguration.startTls) {
                        // see also http://jira.qos.ch/browse/LBCORE-225
                        props["mail.smtp.starttls.enable"] = "true"
                    }
                    if (emailConfiguration.ssl) {
                        val SSL_FACTORY = "javax.net.ssl.SSLSocketFactory"
                        props["mail.smtp.socketFactory.port"] = emailConfiguration.port
                        props["mail.smtp.socketFactory.class"] = SSL_FACTORY
                        props["mail.smtp.socketFactory.fallback"] = "true"
                    }
                }

                // props.put("mail.debug", "true");

                session = Session.getInstance(props, loginAuthenticator)
            } catch (e: Exception) {
                addError("Error to get email configuration")
            }

//        }
        super.sendBuffer(cb, lastEventObject)
    }
}