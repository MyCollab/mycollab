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

import org.slf4j.LoggerFactory
import java.io.IOException
import java.sql.DriverManager
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class DatabaseValidateServlet : HttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val databaseName = request.getParameter("databaseName")
        val dbUserName = request.getParameter("dbUserName")
        val dbPassword = request.getParameter("dbPassword")
        val databaseServer = request.getParameter("databaseServer")

        try {
            Class.forName("com.mysql.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            LOG.error("Can not load mysql driver", e)
        }

        val dbUrl = "jdbc:mysql://$databaseServer/$databaseName?useUnicode=true"
        try {
            DriverManager.getConnection(dbUrl, dbUserName, dbPassword).use { connection -> connection.metaData }
        } catch (e: Exception) {
            val out = response.writer
            out.write("Cannot establish connection to database. Recheck your input.")
            LOG.warn("Can not connect database", e)
        }

    }

    companion object {
        private val serialVersionUID = 1L
        private val LOG = LoggerFactory.getLogger(DatabaseValidateServlet::class.java)
    }
}
