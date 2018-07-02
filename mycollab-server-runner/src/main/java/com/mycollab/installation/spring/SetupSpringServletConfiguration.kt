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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.installation.spring

import com.mycollab.installation.servlet.*
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Configuration
@Profile("setup")
class SetupSpringServletConfiguration {
    @Bean
    fun assetServlet() = ServletRegistrationBean(AssetHttpServletRequestHandler(), "/assets/*")

    @Bean
    fun databaseValidateServlet() = ServletRegistrationBean(DatabaseValidateServlet(), "/validate")

    @Bean
    fun emailValidateServlet() = ServletRegistrationBean(EmailValidationServlet(), "/emailValidate")

    @Bean
    fun installationServlet() = ServletRegistrationBean(InstallationServlet(), "/install")

    @Bean
    fun setupServlet() = ServletRegistrationBean(SetupServlet(), "/")
}
