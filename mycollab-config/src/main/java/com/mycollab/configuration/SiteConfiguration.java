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
package com.mycollab.configuration;

import com.mycollab.core.utils.FileUtils;
import com.mycollab.spring.AppContextUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.mycollab.configuration.ApplicationProperties.*;

/**
 * Utility class read mycollab system properties when system starts
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SiteConfiguration {

    private static SiteConfiguration instance;

    private String sentErrorEmail;
    private String siteName;
    private String serverAddress;
    private Locale defaultLocale;
    private EmailConfiguration emailConfiguration;
    private DatabaseConfiguration databaseConfiguration;
    private String endecryptPassword;
    private String dropboxCallbackUrl;
    private String ggDriveCallbackUrl;

    private Configuration freemarkerConfiguration;

    public static void loadConfiguration() {
        TimeZone.setDefault(DateTimeZone.UTC.toTimeZone());
        DateTimeZone.setDefault(DateTimeZone.UTC);
        ApplicationProperties.loadProps();
        instance = new SiteConfiguration();

        instance.sentErrorEmail = ApplicationProperties.getString(ERROR_SENDTO, "support@mycollab.com");
        instance.siteName = ApplicationProperties.getString(SITE_NAME, "MyCollab");
        instance.serverAddress = ApplicationProperties.getString(SERVER_ADDRESS, "localhost");
        String propLocale = ApplicationProperties.getString(DEFAULT_LOCALE, "en_US");
        try {
            instance.defaultLocale = Locale.forLanguageTag(propLocale);
        } catch (Exception e) {
            instance.defaultLocale = Locale.US;
        }

        instance.endecryptPassword = ApplicationProperties.getString(BI_ENDECRYPT_PASSWORD, "mycollab123");

        // load email
        String host = ApplicationProperties.getString(MAIL_SMTPHOST);
        String user = ApplicationProperties.getString(MAIL_USERNAME);
        String password = ApplicationProperties.getString(MAIL_PASSWORD);
        Integer port = Integer.parseInt(ApplicationProperties.getString(MAIL_PORT, "25"));
        Boolean isTls = Boolean.parseBoolean(ApplicationProperties.getString(MAIL_IS_TLS, "false"));
        Boolean isSsl = Boolean.parseBoolean(ApplicationProperties.getString(MAIL_IS_SSL, "false"));
        String noreplyEmail = ApplicationProperties.getString(MAIL_NOTIFY, "");
        instance.emailConfiguration = new EmailConfiguration(host, user, password, port, isTls, isSsl, noreplyEmail);

        // load database configuration
        String driverClass = ApplicationProperties.getString(DB_DRIVER_CLASS);
        String dbUrl = ApplicationProperties.getString(DB_URL);
        String dbUser = ApplicationProperties.getString(DB_USERNAME);
        String dbPassword = ApplicationProperties.getString(DB_PASSWORD);
        instance.databaseConfiguration = new DatabaseConfiguration(driverClass, dbUrl, dbUser, dbPassword);

        instance.dropboxCallbackUrl = ApplicationProperties.getString(DROPBOX_AUTH_LINK);
        instance.ggDriveCallbackUrl = ApplicationProperties.getString(GOOGLE_DRIVE_LINK);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);
        configuration.setDefaultEncoding("UTF-8");
        try {
            List<TemplateLoader> loaders = new ArrayList<>();
            File i18nFolder = new File(FileUtils.getUserFolder(), "i18n");
            File confFolder1 = new File(FileUtils.getUserFolder(), "config");
            File confFolder2 = new File(FileUtils.getUserFolder(), "src/main/config");
            if (i18nFolder.exists()) {
                loaders.add(new FileTemplateLoader(i18nFolder));
            }
            if (confFolder1.exists()) {
                loaders.add(new FileTemplateLoader(confFolder1));
            }
            if (confFolder2.exists()) {
                loaders.add(new FileTemplateLoader(confFolder2));
            }
            loaders.add(new ClassTemplateLoader(SiteConfiguration.class.getClassLoader(), ""));
            configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
            instance.freemarkerConfiguration = configuration;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static SiteConfiguration getInstance() {
        if (instance == null) {
            loadConfiguration();
        }
        return instance;
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

    public static String getNotifyEmail() {
        return getInstance().emailConfiguration.getNotifyEmail();
    }

    public static String getDefaultSiteName() {
        return getInstance().siteName;
    }

    public static String getSendErrorEmail() {
        return getInstance().sentErrorEmail;
    }

    public static String getSiteUrl(String subDomain) {
        IDeploymentMode modeService = AppContextUtil.getSpringBean(IDeploymentMode.class);
        return modeService.getSiteUrl(subDomain);
    }

    public static boolean isDemandEdition() {
        IDeploymentMode modeService = AppContextUtil.getSpringBean(IDeploymentMode.class);
        return modeService.isDemandEdition();
    }

    public static boolean isCommunityEdition() {
        IDeploymentMode modeService = AppContextUtil.getSpringBean(IDeploymentMode.class);
        return modeService.isCommunityEdition();
    }

    public static Locale getDefaultLocale() {
        return instance.defaultLocale;
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

    public static Configuration freemarkerConfiguration() {
        return getInstance().freemarkerConfiguration;
    }
}