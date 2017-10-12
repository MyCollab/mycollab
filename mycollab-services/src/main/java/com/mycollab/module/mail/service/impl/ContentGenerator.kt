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
package com.mycollab.module.mail.service.impl

import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.ServerConfiguration
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.schedule.email.MailStyles
import com.mycollab.spring.AppContextUtil
import freemarker.template.Configuration
import org.joda.time.LocalDate
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.io.StringWriter
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ContentGenerator(private val applicationConfiguration: ApplicationConfiguration,
                       private val deploymentMode: IDeploymentMode,
                       private val templateEngine: Configuration) : IContentGenerator, InitializingBean {
    private val templateContext: MutableMap<String, Any> = mutableMapOf()

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        val defaultUrls = mutableMapOf(
                "cdn_url" to deploymentMode.getCdnUrl(),
                "facebook_url" to (applicationConfiguration.facebookUrl ?: ""),
                "google_url" to (applicationConfiguration.googleUrl ?: ""),
                "linkedin_url" to (applicationConfiguration.linkedinUrl ?: ""),
                "twitter_url" to (applicationConfiguration.twitterUrl ?: ""))
        putVariable("defaultUrls", defaultUrls)
        putVariable("current_year", LocalDate().year)
        putVariable("siteName", SiteConfiguration.getDefaultSiteName())
        putVariable("styles", MailStyles.instance())

        val storageFactory = AppContextUtil.getSpringBean(AbstractStorageService::class.java)
        putVariable("storageFactory", storageFactory)
    }

    override fun putVariable(key: String, value: Any) {
        templateContext.put(key, value)
    }

    override fun parseFile(templateFilePath: String): String = parseFile(templateFilePath, null)

    override fun parseFile(templateFilePath: String, locale: Locale?): String {
        val writer = StringWriter()
        val template = templateEngine.getTemplate(templateFilePath, locale)
        template.process(templateContext, writer)
        return writer.toString()
    }
}