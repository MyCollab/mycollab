package com.mycollab.servlet;

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
        }
    }
}
