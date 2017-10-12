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
