/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.spring;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
@Service
public class FreeMarkerConfiguration extends FreeMarkerConfigurationFactoryBean {

    @Override
    protected Configuration newConfiguration() throws IOException, TemplateException {
        setPreTemplateLoaders();
        return SiteConfiguration.freemarkerConfiguration();
    }
}
