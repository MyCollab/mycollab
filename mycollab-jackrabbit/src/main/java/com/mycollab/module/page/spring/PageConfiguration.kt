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
package com.mycollab.module.page.spring

import com.mycollab.module.page.PageSessionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.extensions.jcr.JcrTemplate
import org.springframework.extensions.jcr.jackrabbit.RepositoryFactoryBean

import javax.jcr.SimpleCredentials

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
class PageConfiguration {

    @Bean
    @DependsOn(value = ["dataSource"])
    fun pageRepository(): RepositoryFactoryBean {
        val bean = RepositoryFactoryBean()
        bean.configuration = ClassPathResource("wiki-repo.xml")
        bean.homeDir = FileSystemResource("repo2/wiki-workspace")
        return bean
    }

    @Bean
    @Throws(Exception::class)
    fun pageJcrSessionFactory(): PageSessionFactory {
        val bean = PageSessionFactory()
        bean.repository = pageRepository().getObject()
        bean.credentials = SimpleCredentials("hainguyen", "esofthead321".toCharArray())
        return bean
    }

    @Bean
    @Throws(Exception::class)
    fun pageJcrTemplate(): JcrTemplate {
        val bean = JcrTemplate()
        bean.sessionFactory = pageJcrSessionFactory()
        bean.isAllowCreate = true
        return bean
    }
}
