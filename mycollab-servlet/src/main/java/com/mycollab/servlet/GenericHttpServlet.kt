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

import com.mycollab.core.ResourceNotFoundException
import com.mycollab.core.UserInvalidInputException
import com.mycollab.i18n.LocalizationHelper
import freemarker.template.TemplateException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.context.support.SpringBeanAutowiringSupport

import javax.servlet.ServletConfig
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.io.PrintWriter
import java.util.Locale

import com.mycollab.core.utils.ExceptionUtils.getExceptionType

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
abstract class GenericHttpServlet : HttpServlet() {

    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        super.init(config)
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.servletContext)
    }

    @Throws(ServletException::class, IOException::class)
    override fun service(request: HttpServletRequest, response: HttpServletResponse) {
        try {
            response.locale = getResponseLocale(request)
            response.characterEncoding = "UTF-8"
            onHandleRequest(request, response)
        } catch (e: Exception) {
            var filterException: Exception? = getExceptionType(e, UserInvalidInputException::class.java)
            if (filterException != null) {
                val out = response.writer
                out.println(filterException.message)
                return
            }
            filterException = getExceptionType(e, ResourceNotFoundException::class.java)
            if (filterException != null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND)
                LOG.error("Resource not found", filterException)
                return
            }
            throw ServletException(e)
        }

    }

    private fun getResponseLocale(request: HttpServletRequest): Locale {
        val locale = request.getParameter("locale")
        return if (locale == null) Locale.US else LocalizationHelper.getLocaleInstance(locale)
    }

    @Throws(ServletException::class, IOException::class, TemplateException::class)
    protected abstract fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse)

    companion object {
        private val LOG = LoggerFactory.getLogger(GenericHttpServlet::class.java)
    }
}
