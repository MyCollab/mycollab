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

import com.esofthead.mycollab.core.DeploymentMode;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.esofthead.mycollab.configuration.ApplicationProperties.*;

/**
 * Utility class read mycollab system properties when system starts
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class SiteConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(SiteConfiguration.class);

    private static SiteConfiguration instance;

    private DeploymentMode deploymentMode;
    private String sentErrorEmail;
    private String siteName;
    private String serverAddress;
    private int serverPort;
    private String noreplyEmail;
    private EmailConfiguration emailConfiguration;
    private DatabaseConfiguration databaseConfiguration;
    private String cdnUrl;
    private String endecryptPassword;
    private String dropboxCallbackUrl;
    private String ggDriveCallbackUrl;
    private String appUrl;
    private String resourceDownloadUrl;

    private Locale defaultLocale;
    private List<Locale> supportedLanguages;

    public static void loadInstance(int serverPort) {
        ApplicationProperties.loadProps();
        instance = new SiteConfiguration();

        instance.sentErrorEmail = ApplicationProperties.getString(ERROR_SENDTO, "support@mycollab.com");
        instance.siteName = ApplicationProperties.getString(SITE_NAME, "MyCollab");
        instance.serverAddress = ApplicationProperties.getString(SERVER_ADDRESS, "localhost");
        instance.defaultLocale = toLocale(ApplicationProperties.getString(DEFAULT_LOCALE, "en_US"));

        instance.supportedLanguages = getSupportedLocales(ApplicationProperties.getString(LOCALES, "en_US, ja_JP"));

        instance.serverPort = serverPort;

        // load Deployment Mode
        String runningMode = ApplicationProperties.getString(RUNNING_MODE, null);
        instance.deploymentMode = DeploymentMode.valueOf(runningMode);
        LOG.debug("Site is running under {} mode", instance.deploymentMode);

        instance.cdnUrl = String.format(ApplicationProperties.getString(CDN_URL),
                instance.serverAddress, instance.serverPort);

        instance.appUrl = String.format(ApplicationProperties.getString(APP_URL),
                instance.serverAddress, instance.serverPort);
        if (!instance.appUrl.endsWith("/")) {
            instance.appUrl += "/";
        }

        StorageManager.loadStorageConfig();

        instance.endecryptPassword = ApplicationProperties.getString(
                BI_ENDECRYPT_PASSWORD, "esofthead321");

        // load email
        String host = ApplicationProperties.getString(MAIL_SMTPHOST);
        String user = ApplicationProperties.getString(MAIL_USERNAME);
        String password = ApplicationProperties.getString(MAIL_PASSWORD);
        Integer port = Integer.parseInt(ApplicationProperties.getString(
                MAIL_PORT, "-1"));
        Boolean isTls = Boolean.parseBoolean(ApplicationProperties.getString(
                MAIL_IS_TLS, "false"));
        instance.emailConfiguration = new EmailConfiguration(host, user,
                password, port, isTls);
        instance.noreplyEmail = ApplicationProperties.getString(MAIL_NOREPLY,
                "noreply@mycollab.com");

        // load database configuration
        String driverClass = ApplicationProperties.getString(DB_DRIVER_CLASS);
        String dbUrl = ApplicationProperties.getString(DB_URL);
        String dbUser = ApplicationProperties.getString(DB_USERNAME);
        String dbPassword = ApplicationProperties.getString(DB_PASSWORD);
        instance.databaseConfiguration = new DatabaseConfiguration(driverClass, dbUrl, dbUser, dbPassword);

        instance.resourceDownloadUrl = ApplicationProperties.getString(RESOURCE_DOWNLOAD_URL, instance.appUrl + "file/");

        instance.dropboxCallbackUrl = ApplicationProperties.getString(DROPBOX_AUTH_LINK);
        instance.ggDriveCallbackUrl = ApplicationProperties.getString(GOOGLE_DRIVE_LINK);
    }

    private static SiteConfiguration getInstance() {
        if (instance == null) {
            loadInstance(8080);
        }
        return instance;
    }

    public static String getCdnUrl() {
        return getInstance().cdnUrl;
    }

    public static String getAppUrl() {
        return getInstance().appUrl;
    }

    public static String getResourceDownloadUrl() {
        return getInstance().resourceDownloadUrl;
    }

    public static DatabaseConfiguration getDatabaseConfiguration() {
        return getInstance().databaseConfiguration;
    }

    public static EmailConfiguration getEmailConfiguration() {
        return getInstance().emailConfiguration;
    }

    public static void setEmailConfiguration(EmailConfiguration conf) {
        getInstance().emailConfiguration = conf;
    }

    public static String getNoReplyEmail() {
        return getInstance().noreplyEmail;
    }

    public static String getSiteName() {
        return getInstance().siteName;
    }

    public static DeploymentMode getDeploymentMode() {
        return getInstance().deploymentMode;
    }

    public static String getSendErrorEmail() {
        return getInstance().sentErrorEmail;
    }

    public static Locale getDefaultLocale() {
        return getInstance().defaultLocale;
    }

    public static List<Locale> getSupportedLanguages() {
        return getInstance().supportedLanguages;
    }

    public static String getSiteUrl(String subDomain) {
        String siteUrl;
        if (getInstance().deploymentMode == DeploymentMode.site) {
            siteUrl = String.format(ApplicationProperties.getString(ApplicationProperties.APP_URL), subDomain);
        } else {
            siteUrl = String.format(ApplicationProperties.getString(ApplicationProperties.APP_URL),
                    instance.serverAddress, instance.serverPort);
        }
        return siteUrl;
    }

    public static String getDropboxCallbackUrl() {
        return getInstance().dropboxCallbackUrl;
    }

    public static String getGGDriveCallbackUrl() {
        return getInstance().ggDriveCallbackUrl;
    }

    public static String getEnDecryptPassword() {
        return getInstance().endecryptPassword;
    }

    public static String getServerAddress() {
        return getInstance().serverAddress;
    }

    public static int getServerPort() {
        return getInstance().serverPort;
    }

    public static Locale toLocale(String language) {
        if (language == null) {
            return Locale.US;
        }

        return LocaleUtils.toLocale(language);
    }

    private static List<Locale> getSupportedLocales(String languageVal) {
        List<Locale> locales = new ArrayList<>();
        String[] languages = languageVal.split(",");
        for (String language : languages) {
            Locale locale = toLocale(language.trim());
            if (locale == null) {
                LOG.error("Do not support native language {}", language);
                continue;
            }

            locales.add(locale);
        }
        return locales;
    }
}