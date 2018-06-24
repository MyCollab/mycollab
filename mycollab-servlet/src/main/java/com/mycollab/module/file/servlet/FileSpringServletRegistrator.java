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
package com.mycollab.module.file.servlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Configuration
@Profile("program")
public class FileSpringServletRegistrator {
    @Bean("assetServlet")
    public ServletRegistrationBean assetServlet() {
        return new ServletRegistrationBean(new AssetHandler(), "/assets/*");
    }

    @Bean("resourceGetServlet")
    public ServletRegistrationBean resourceGetServlet() {
        return new ServletRegistrationBean(new ResourceGetHandler(), "/file/*");
    }

    @Bean("userAvatarServlet")
    public ServletRegistrationBean userAvatarServlet() {
        return new ServletRegistrationBean(new UserAvatarHttpServletRequestHandler(), "/file/avatar/*");
    }
}
