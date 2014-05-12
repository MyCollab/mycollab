package com.esofthead.mycollab.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class EmailValidationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String smtpUserName = request.getParameter("smtpUserName");
		String smtpPassword = request.getParameter("smtpPassword");
		String smtpHost = request.getParameter("smtpHost");
		String smtpPort = request.getParameter("smtpPort");
		String tls = request.getParameter("tls");

		int mailServerPort = 1;
		try {
			mailServerPort = Integer.parseInt(smtpPort);
		} catch (Exception e) {

		}
		try {
			Email email = new SimpleEmail();
			email.setHostName(smtpHost);
			email.setSmtpPort(mailServerPort);
			email.setAuthenticator(new DefaultAuthenticator(smtpUserName,
					smtpPassword));
			if (tls.equals("true")) {
				email.setSSLOnConnect(true);
			} else {
				email.setSSLOnConnect(false);
			}
			email.setFrom(smtpUserName);
			email.setSubject("TestMail");
			email.setMsg("This is a test mail ... :-)");
			email.addTo(smtpUserName);
			email.send();
		} catch (EmailException e) {
			PrintWriter out = response.getWriter();
			out.write("Cannot establish SMTP connection. Please recheck your config.");
			return;
		}
	}

}
