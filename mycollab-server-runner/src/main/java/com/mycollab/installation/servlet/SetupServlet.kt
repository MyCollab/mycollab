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
package com.mycollab.installation.servlet

import com.mycollab.template.FreemarkerFactory
import freemarker.template.TemplateException
import org.joda.time.LocalDate
import java.io.IOException
import java.io.StringWriter
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class SetupServlet : HttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html"
        response.status = HttpServletResponse.SC_OK

        val defaultUrls = mutableMapOf("cdn_url" to "/assets/", "app_url" to "/",
                "facebook_url" to "https://www.facebook.com/mycollab2",
                "google_url" to "https://plus.google.com/u/0/b/112053350736358775306/+Mycollab/about/p/pub",
                "twitter_url" to "https://twitter.com/mycollabdotcom")

        val context = mutableMapOf("postUrl" to "/install", "defaultUrls" to defaultUrls,
                "current_year" to LocalDate().year)

        val writer = StringWriter()
        val template = FreemarkerFactory.template("pageSetupFresh.ftl")
        try {
            template.process(context, writer)
        } catch (e: TemplateException) {
            throw IOException(e)
        }

        val out = response.writer
        out.print(writer.toString())
    }
}
