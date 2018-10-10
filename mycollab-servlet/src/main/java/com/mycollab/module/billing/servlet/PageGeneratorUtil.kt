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
package com.mycollab.module.billing.servlet

import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.spring.AppContextUtil
import freemarker.template.Configuration
import freemarker.template.TemplateException
import java.io.IOException
import java.io.StringWriter
import java.util.*
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
internal object PageGeneratorUtil {
    @Throws(IOException::class, TemplateException::class)
    @JvmStatic
    fun responseUserNotExistPage(response: HttpServletResponse, username: String, loginURL: String) {
        val context = HashMap<String, Any>()
        context["loginURL"] = loginURL
        context["username"] = username
        val applicationConfiguration = AppContextUtil.getSpringBean(ApplicationConfiguration::class.java)
        val deploymentMode = AppContextUtil.getSpringBean(IDeploymentMode::class.java)
        val defaultUrls = applicationConfiguration.defaultUrls()
        defaultUrls["cdn_url"] = deploymentMode.getCdnUrl()
        context["defaultUrls"] = defaultUrls

        val writer = StringWriter()
        val templateEngine = AppContextUtil.getSpringBean(Configuration::class.java)
        val template = templateEngine.getTemplate("pageUserNotExist.ftl", Locale.US)
        template.process(context, writer)

        val html = writer.toString()
        val out = response.writer
        out.println(html)
    }
}
