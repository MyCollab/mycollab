package com.mycollab.servlet;

import com.mycollab.core.UserInvalidInputException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class InstallUtils {
    public static void checkSMTPConfig(String host, int port, String username, String password, boolean auth, boolean isStartTls, boolean isSSL) {
        try {
            Properties props = new Properties();
            if (auth) {
                props.setProperty("mail.smtp.auth", "true");
            } else {
                props.setProperty("mail.smtp.auth", "false");
            }
            if (isStartTls) {
                props.setProperty("mail.smtp.starttls.enable", "true");
                props.setProperty("mail.smtp.startssl.enable", "true");
            } else if (isSSL) {
                props.setProperty("mail.smtp.startssl.enable", "false");
                props.setProperty("mail.smtp.ssl.enable", "true");
                props.setProperty("mail.smtp.ssl.socketFactory.fallback", "false");
            }

            Email email = new SimpleEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(username, password));
            email.setStartTLSEnabled(isStartTls);
            email.setSSLOnConnect(isSSL);

            email.setFrom(username);
            email.setSubject("MyCollab Test Email");
            email.setMsg("This is a test mail ... :-)");
            email.addTo(username);
            email.send();
        } catch (Exception e) {
            throw new UserInvalidInputException(e);
        }
    }
}
