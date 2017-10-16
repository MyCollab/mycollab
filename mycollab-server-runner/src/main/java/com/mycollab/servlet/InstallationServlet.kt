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

import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.utils.FileUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.StringWriter
import java.nio.file.FileSystems
import java.nio.file.Files
import java.sql.DriverManager
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class InstallationServlet : HttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        LOG.info("Try to install MyCollab")
        val out = response.writer
        val siteName = request.getParameter("sitename")
        val serverAddress = request.getParameter("serverAddress")
        val databaseName = request.getParameter("databaseName")
        val dbUserName = request.getParameter("dbUserName")
        val dbPassword = request.getParameter("dbPassword")
        val databaseServer = request.getParameter("databaseServer")
        val smtpUserName = request.getParameter("smtpUserName")
        val smtpPassword = request.getParameter("smtpPassword")
        val smtpHost = request.getParameter("smtpHost")
        val smtpPort = request.getParameter("smtpPort")
        val tls = request.getParameter("tls")
        val ssl = request.getParameter("ssl")

        val dbUrl = "jdbc:mysql://$databaseServer/$databaseName?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true&useCompression=true&useServerPrepStmts=false&verifyServerCertificate=false&useSSL=false"

        try {
            Class.forName("com.mysql.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            LOG.error("Can not load mysql driver", e)
            return
        }

        try {
            DriverManager.getConnection(dbUrl, dbUserName, dbPassword).use { connection ->
                LOG.debug("Check database config")
                connection.metaData
            }
        } catch (e: Exception) {
            val rootCause = if (e.cause == null) e.message else e.cause
            out.write("Cannot establish connection to database. Make sure your inputs are correct. Root cause is $rootCause")
            LOG.error("Can not connect database", e)
            return
        }

        val mailServerPort = try {
            Integer.parseInt(smtpPort)
        } catch (e: Exception) {
            0
        }

        val isStartTls = java.lang.Boolean.parseBoolean(tls)
        val isSsl = java.lang.Boolean.parseBoolean(ssl)
        try {
            InstallUtils.checkSMTPConfig(smtpHost, mailServerPort, smtpUserName, smtpPassword, true, isStartTls, isSsl)
        } catch (e: UserInvalidInputException) {
            LOG.warn("Cannot authenticate mail server successfully. Make sure your inputs are correct.")
        }

        val configuration = SiteConfiguration.freemarkerConfiguration()
        val templateContext = mutableMapOf("sitename" to siteName, "serveraddress" to serverAddress,
                "dbUrl" to dbUrl, "dbUser" to dbUserName, "dbPassword" to dbPassword,
                "smtpAddress" to smtpHost, "smtpPort" to mailServerPort, "smtpUserName" to smtpUserName,
                "smtpPassword" to smtpPassword, "smtpTLSEnable" to tls, "smtpSSLEnable" to ssl,
                "mailNotify" to smtpUserName)

        val confFolder = FileUtils.getDesireFile(FileUtils.userFolder, "config", "src/main/config")
        if (confFolder == null) {
            out.write("Can not write the settings to the file system. You should check our knowledge base article at " + "http://support.mycollab.com/topic/994098-/ to solve this issue.")
            return
        }

        if (!Files.isWritable(FileSystems.getDefault().getPath(confFolder.absolutePath))) {
            out.write("The folder ${confFolder.absolutePath} has no write permission with the current user." +
                    " You should set the write permission for MyCollab process for this folder.  You should check our knowledge base article at http://support.mycollab.com/topic/994098-/ to solve this issue.")
            return
        }

        try {
            val writer = StringWriter()
            val template = configuration.getTemplate("mycollab.properties.ftl")
            template.process(templateContext, writer)

            val outStream = FileOutputStream(File(confFolder, "mycollab.properties"))
            outStream.write(writer.toString().toByteArray())
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            LOG.error("Error while set up MyCollab", e)
            out.write("Can not write the settings to the file system. You should check our knowledge base article at http://support.mycollab.com/topic/994098-/ to solve this issue.")
        }
    }

    companion object {
        private val serialVersionUID = 1L
        private val LOG = LoggerFactory.getLogger(InstallationServlet::class.java)
    }
}
