package com.mycollab.spring;

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.FileUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
@Component
@Profile("production")
public class FreeMarkerConfiguration extends FreeMarkerConfigurationFactoryBean {
    private static Logger LOG = LoggerFactory.getLogger(FreeMarkerConfiguration.class);

    private Configuration configuration;

    @Override
    protected Configuration newConfiguration() throws IOException, TemplateException {
        configuration = new Configuration(Configuration.VERSION_2_3_25);
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
        } catch (Exception e) {
            LOG.error("Can not configure freemarker", e);
            System.exit(-1);
        }
        return configuration;
    }
}
