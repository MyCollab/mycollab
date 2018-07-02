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
package com.mycollab.installation.servlet

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.EmailException
import org.apache.commons.mail.SimpleEmail
import org.slf4j.LoggerFactory
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class EmailValidationServlet : HttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val smtpUserName = request.getParameter("smtpUserName")
        val smtpPassword = request.getParameter("smtpPassword")
        val smtpHost = request.getParameter("smtpHost")
        val smtpPort = request.getParameter("smtpPort")
        val tls = request.getParameter("tls")
        val ssl = request.getParameter("ssl")

        var mailServerPort = 25
        try {
            mailServerPort = Integer.parseInt(smtpPort)
        } catch (e: Exception) {
            LOG.info("The smtp port value is not a number. We will use default port value is 25")
        }

        try {
            val email = SimpleEmail()
            email.hostName = smtpHost
            email.setSmtpPort(mailServerPort)
            email.setAuthenticator(DefaultAuthenticator(smtpUserName, smtpPassword))
            email.isStartTLSEnabled = "true" == tls

            email.isSSLOnConnect = "true" == ssl
            email.setFrom(smtpUserName)
            email.subject = "MyCollab Test Email"
            email.setMsg("This is a test mail ... :-)")
            email.addTo(smtpUserName)
            email.send()
        } catch (e: EmailException) {
            val out = response.writer
            out.write("Cannot establish SMTP connection. Please recheck your config.")
            LOG.warn("Can not login to SMTP", e)
        }

    }

    companion object {
        private val serialVersionUID = 1L
        private val LOG = LoggerFactory.getLogger(EmailValidationServlet::class.java)
    }
}
