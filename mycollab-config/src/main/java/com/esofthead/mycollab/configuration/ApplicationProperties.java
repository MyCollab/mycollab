/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.FileUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * This file contains all constants define in system properties file
 * mycollab.properties read at system started.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ApplicationProperties {
    private static final String RESOURCE_PROPERTIES = "mycollab.properties";
    private static final String DECRYPT_PASS = "esofthead321";

    private static Properties properties;

    public static final String MYCOLLAB_PORT = "mycollab.port";

    public static final String DB_USERNAME = "db.username";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_DRIVER_CLASS = "db.driverClassName";
    public static final String DB_URL = "db.url";

    public static final String CDN_URL = "cdn.url";
    public static final String APP_URL = "app.url";

    public static final String FACEBOOK_URL = "facebook.url";
    public static final String GOOGLE_URL = "google.url";
    public static final String LINKEDIN_URL = "linkedin.url";
    public static final String TWITTER_URL = "twitter.url";

    public static final String MAIL_SMTPHOST = "mail.smtphost";
    public static final String MAIL_PORT = "mail.port";
    public static final String MAIL_USERNAME = "mail.username";
    public static final String MAIL_PASSWORD = "mail.password";
    public static final String MAIL_IS_TLS = "mail.isTLS";
    public static final String MAIL_IS_SSL = "mail.isSSL";
    public static final String MAIL_NOREPLY = "mail.noreply";

    public static final String ERROR_SENDTO = "error.sendTo";
    public static final String STORAGE_SYSTEM = "storageSystem";

    public static final String LOCALES = "locale.list";
    public static final String DEFAULT_LOCALE = "locale.default";
    public static final String SITE_NAME = "site.name";
    public static final String SERVER_ADDRESS = "server.address";
    public static final String RUNNING_MODE = "running.mode";

    public static final String RESOURCE_DOWNLOAD_URL = "resource.downloadUrl";

    public static final String DROPBOX_AUTH_LINK = "dropbox.callbackUrl";
    public static final String GOOGLE_DRIVE_LINK = "ggDrive.callbackUrl";

    public static final String BI_ENDECRYPT_PASSWORD = "endecryptPassword";

    public static final String COPYRIGHT_MSG = "copyright";

    public static void loadProps() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(DECRYPT_PASS);

        properties = new EncryptableProperties(encryptor);
        try {
            File myCollabResourceFile = getAppConfigFile();

            if (myCollabResourceFile != null) {
                try (FileInputStream propsStream = new FileInputStream(myCollabResourceFile)) {
                    properties.load(propsStream);
                }
            } else {
                InputStream propStreams = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_PROPERTIES);
                if (propStreams == null) {
                    // Probably we are running testing
                    properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("default-mycollab-test.properties"));
                }
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    public static File getAppConfigFile() {
        return FileUtils.getDesireFile(System.getProperty("user.dir"), "conf/mycollab.properties", "src/main/conf/mycollab.properties");
    }

    public static Properties getAppProperties() {
        return properties;
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        if (properties == null) {
            return defaultValue;
        }
        return properties.getProperty(key, defaultValue);
    }
}
