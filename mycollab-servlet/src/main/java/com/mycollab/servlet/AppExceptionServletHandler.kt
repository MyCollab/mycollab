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
package com.mycollab.servlet

import com.mycollab.common.i18n.ErrorI18nEnum
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.i18n.LocalizationHelper
import freemarker.template.Configuration
import freemarker.template.TemplateException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import java.io.IOException
import java.io.StringWriter
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = arrayOf("/error"), name = "appExceptionHandlerServlet")
class AppExceptionServletHandler : GenericHttpServlet() {

    @Autowired
    private lateinit var templateEngine: Configuration

    @Autowired
    private lateinit var deploymentMode: IDeploymentMode

    @Autowired
    private lateinit var applicationConfiguration: ApplicationConfiguration

    @Throws(ServletException::class, IOException::class)
    override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val status_code = request.getAttribute("javax.servlet.error.status_code") as? Int

        if (request.getHeader("User-Agent") == null) {
            return
        }

        try {
            if (status_code != null && status_code == 404 || "404" == request.getParameter("param")) {
                responsePage404(response)
            } else {
                // Analyze the servlet exception
                val throwable = request.getAttribute("javax.servlet.error.exception") as? Throwable
                responsePage500(response, throwable)
            }

        } catch (e: Exception) {
            LOG.error("Error in servlet", e)
        }

    }

    @Throws(IOException::class, TemplateException::class)
    private fun responsePage404(response: HttpServletResponse) {
        val context = mutableMapOf<String, Any>()
        val defaultUrls = applicationConfiguration.defaultUrls()
        defaultUrls.put("cdn_url", deploymentMode.getCdnUrl())
        context.put("defaultUrls", defaultUrls)

        val writer = StringWriter()
        val template = templateEngine.getTemplate("page404.ftl", response.locale)
        template.process(context, writer)

        val html = writer.toString()
        val out = response.writer
        out.println(html)
    }

    @Throws(IOException::class, TemplateException::class)
    private fun responsePage500(response: HttpServletResponse, throwable: Throwable?) {
        if (throwable != null) {
            val integrityViolationException = getExceptionType(throwable,
                    DataIntegrityViolationException::class.java)
            if (integrityViolationException != null) {
                response.writer.println(LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale(),
                        GenericI18Enum.ERROR_USER_NOTICE_INFORMATION_MESSAGE))
                LOG.error("Exception in MyCollab", throwable)
                return
            }
            val exception = getExceptionType(throwable, DataAccessException::class.java)
            if (exception != null) {
                response.writer.println(String.format("<h1>%s</h1>", LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale(),
                        ErrorI18nEnum.ERROR_ESTABLISH_DATABASE_CONNECTION)))
                return
            }
            LOG.error("Exception in mycollab", throwable)
        }

        val context = mutableMapOf<String, Any>()
        val defaultUrls = applicationConfiguration.defaultUrls()
        defaultUrls.put("cdn_url", deploymentMode.getCdnUrl())
        context.put("defaultUrls", defaultUrls)

        val writer = StringWriter()
        val template = templateEngine.getTemplate("page500.ftl", response.locale)
        template.process(context, writer)

        val html = writer.toString()
        val out = response.writer
        out.println(html)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AppExceptionServletHandler::class.java)

        private fun <T> getExceptionType(e: Throwable, exceptionType: Class<T>): T? {
            return when {
                exceptionType.isAssignableFrom(e.javaClass) -> e as T
                e.cause != null -> getExceptionType(e.cause as Throwable, exceptionType)
                else -> null
            }
        }
    }
}
