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

import com.esofthead.mycollab.core.UserInvalidInputException;

import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class InstallUtils {

    public static void checkSMTPConfig(String host, int port, String username,
                                          String password, boolean auth, boolean isStartTls, boolean isSSL) {
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
            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, username, password);
            transport.close();
        } catch (Exception e) {
            throw new UserInvalidInputException(e);
        }
    }
}
