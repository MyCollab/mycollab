/**
 * This file is part of mycollab-server-runner.
 *
 * mycollab-server-runner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-server-runner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-server-runner.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.servlet;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class EmailValidationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(EmailValidationServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String smtpUserName = request.getParameter("smtpUserName");
        String smtpPassword = request.getParameter("smtpPassword");
        String smtpHost = request.getParameter("smtpHost");
        String smtpPort = request.getParameter("smtpPort");
        String tls = request.getParameter("tls");
        String ssl = request.getParameter("ssl");

        int mailServerPort = 25;
        try {
            mailServerPort = Integer.parseInt(smtpPort);
        } catch (Exception e) {
            LOG.info("The smtp port value is not a number. We will use default port value is 25");
        }
        try {
            Email email = new SimpleEmail();
            email.setHostName(smtpHost);
            email.setSmtpPort(mailServerPort);
            email.setAuthenticator(new DefaultAuthenticator(smtpUserName, smtpPassword));
            if ("true" .equals(tls)) {
                email.setStartTLSEnabled(true);
            } else {
                email.setStartTLSEnabled(false);
            }

            if ("true" .equals(ssl)) {
                email.setSSLOnConnect(true);
            } else {
                email.setSSLOnConnect(false);
            }
            email.setFrom(smtpUserName);
            email.setSubject("MyCollab Test Email");
            email.setMsg("This is a test mail ... :-)");
            email.addTo(smtpUserName);
            email.send();
        } catch (EmailException e) {
            PrintWriter out = response.getWriter();
            out.write("Cannot establish SMTP connection. Please recheck your config.");
            LOG.warn("Can not login to SMTP", e);
            return;
        }
    }
}
