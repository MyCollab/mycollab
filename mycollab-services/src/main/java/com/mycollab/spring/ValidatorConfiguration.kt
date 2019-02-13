/**
 * Copyright © MyCollab
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.spring

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.io.ClassPathResource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
class ValidatorConfiguration {

    @Bean
    fun validator(): LocalValidatorFactoryBean {
        val bean = LocalValidatorFactoryBean()

        bean.setValidationMessageSource(messageSource())
        bean.setMappingLocations(
                ClassPathResource("validator/user-constraints.xml"),
                ClassPathResource("validator/project-constraints.xml"),
                ClassPathResource("validator/tracker-constraints.xml"))
        return bean
    }

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()

        messageSource.setBasename("classpath:validation")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }

}
