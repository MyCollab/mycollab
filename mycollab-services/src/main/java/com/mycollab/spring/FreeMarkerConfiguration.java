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
