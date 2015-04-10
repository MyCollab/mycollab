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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.AuthenticationFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class InstallUtils {
    private static final Logger LOG = LoggerFactory.getLogger(InstallUtils.class);

    public static boolean checkSMTPConfig(String host, int port, String username, String password, boolean auth, String
            enctype) {
        boolean result = false;
        try {
            Properties props = new Properties();
            if (auth) {
                props.setProperty("mail.smtp.auth", "true");
            } else {
                props.setProperty("mail.smtp.auth", "false");
            }
            if (enctype.endsWith("TLS")) {
                props.setProperty("mail.smtp.starttls.enable", "true");
                props.setProperty("mail.smtp.startssl.enable", "true");
            } else if (enctype.endsWith("SSL")) {
                props.setProperty("mail.smtp.startssl.enable", "true");
            }
            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, username, password);
            transport.close();
            result = true;
        } catch (AuthenticationFailedException e) {
            LOG.debug("Authenticate failed", e);
        } catch (Exception e) {
            LOG.debug("Unknown error", e);
        }
        return result;
    }
}
