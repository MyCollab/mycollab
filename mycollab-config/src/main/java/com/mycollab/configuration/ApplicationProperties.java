package com.mycollab.configuration;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.FileUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static final String DB_USERNAME = "db.username";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_DRIVER_CLASS = "db.driverClassName";
    public static final String DB_URL = "db.url";

    public static final String CDN_URL = "cdn.url";
    public static final String APP_URL = "app.url";

    public static final String MAIL_SMTPHOST = "mail.smtphost";
    public static final String MAIL_PORT = "mail.port";
    public static final String MAIL_USERNAME = "mail.username";
    public static final String MAIL_PASSWORD = "mail.password";
    public static final String MAIL_IS_TLS = "mail.isTLS";
    public static final String MAIL_IS_SSL = "mail.isSSL";
    public static final String MAIL_NOTIFY = "mail.notify";

    public static final String ERROR_SENDTO = "error.sendTo";

    public static final String SITE_NAME = "site.name";
    public static final String SERVER_ADDRESS = "server.address";

    public static final String DROPBOX_AUTH_LINK = "dropbox.callbackUrl";
    public static final String GOOGLE_DRIVE_LINK = "ggDrive.callbackUrl";

    public static final String BI_ENDECRYPT_PASSWORD = "endecryptPassword";

    public static final String COPYRIGHT_MSG = "copyright";
    public static final String DEFAULT_LOCALE = "defaultLocale";

    public static void loadProps() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(DECRYPT_PASS);

        properties = new EncryptableProperties(encryptor);
        try {
            File configFile = getAppConfigFile();

            if (configFile != null) {
                try (InputStreamReader isr = new InputStreamReader(new FileInputStream(configFile), "UTF-8")) {
                    properties.load(isr);
                }
            } else {
                InputStream propStreams = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_PROPERTIES);
                if (propStreams == null) {
                    // Probably we are running testing
                    InputStream propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("default-mycollab-test.properties");
                    if (propStream != null) {
                        try (InputStreamReader isr = new InputStreamReader(propStream, "UTF-8")) {
                            properties.load(isr);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    public static File getAppConfigFile() {
        return FileUtils.getDesireFile(FileUtils.getUserFolder(), "config/mycollab.properties", "src/main/config/mycollab.properties");
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
