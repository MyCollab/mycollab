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

    private Locale defaultLocale;
    private String endecryptPassword;
    private String dropboxCallbackUrl;

    private Configuration freemarkerConfiguration;

    public static void loadConfiguration() {
        TimeZone.setDefault(DateTimeZone.UTC.toTimeZone());
        DateTimeZone.setDefault(DateTimeZone.UTC);
        ApplicationProperties.loadProps();
        instance = new SiteConfiguration();

        String propLocale = ApplicationProperties.getString(DEFAULT_LOCALE, "en_US");
        try {
            instance.defaultLocale = Locale.forLanguageTag(propLocale);
        } catch (Exception e) {
            instance.defaultLocale = Locale.US;
        }

        instance.endecryptPassword = ApplicationProperties.getString(BI_ENDECRYPT_PASSWORD, "mycollab123");

        instance.dropboxCallbackUrl = ApplicationProperties.getString(DROPBOX_AUTH_LINK);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
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

    public static String getEnDecryptPassword() {
        return getInstance().endecryptPassword;
    }

    public static Configuration freemarkerConfiguration() {
        return getInstance().freemarkerConfiguration;
    }
}