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
package com.mycollab.servlet

import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.ServerConfiguration
import com.mycollab.configuration.SiteConfiguration
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import org.springframework.beans.factory.annotation.Autowired

import java.io.IOException
import java.io.StringWriter
import java.util.HashMap
import java.util.Locale

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
abstract class TemplateWebServletRequestHandler : GenericHttpServlet() {

    @Autowired
    private lateinit var templateEngine: Configuration

    @Autowired
    private lateinit var applicationConfiguration: ApplicationConfiguration

    @Autowired
    private lateinit var deploymentMode: IDeploymentMode

    @Throws(IOException::class, TemplateException::class)
    fun generatePageByTemplate(locale: Locale, templatePath: String, params: Map<String, Any>?): String {
        val pageContext = HashMap<String, Any>()
        params?.forEach { (key, value) -> pageContext[key] = value }

        val defaultUrls = applicationConfiguration.defaultUrls()

        defaultUrls["cdn_url"] = deploymentMode.getCdnUrl()
        pageContext["defaultUrls"] = defaultUrls

        val writer = StringWriter()
        //Load template from source folder
        val template = templateEngine.getTemplate(templatePath, locale)
        template.process(pageContext, writer)
        return writer.toString()
    }
}
