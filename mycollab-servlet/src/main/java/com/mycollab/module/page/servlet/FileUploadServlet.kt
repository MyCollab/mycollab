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
package com.mycollab.module.page.servlet

import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.servlet.GenericHttpServlet
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import javax.servlet.ServletException
import javax.servlet.annotation.MultipartConfig
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Part

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@WebServlet(urlPatterns = arrayOf("/page/upload"), name = "pageUploadServlet")
@MultipartConfig(maxFileSize = 24657920, maxRequestSize = 24657920, fileSizeThreshold = 1024)
class FileUploadServlet : GenericHttpServlet() {

    @Autowired
    private lateinit var resourceService: ResourceService

    @Throws(ServletException::class, IOException::class)
    override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html;charset=UTF-8"
        val path = request.getParameter("path")
        val ckEditorFuncNum = request.getParameter("CKEditorFuncNum")

        // Create path components to save the file
        val filePart = request.getPart("upload")
        val fileName = getFileName(filePart) ?: return
        val writer = response.writer
        try {
            filePart.inputStream.use {
                val content = Content(path + "/" + fileName)
                resourceService.saveContent(content, "", it, 1)
                val filePath = ""
                val responseHtml = "<html><body><script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction('$ckEditorFuncNum','$filePath','');</script></body></html>"
                writer.write(responseHtml)
            }
        } catch (fne: FileNotFoundException) {
            writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.")
            writer.println("<br/> ERROR: ${fne.message}")

            LOG.error("Problems during file upload. Error: ${fne.message}")
        }

    }

    private fun getFileName(part: Part): String? {
        for (content in part.getHeader("content-disposition").split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (content.trim { it <= ' ' }.startsWith("filename")) {
                var fileName = content.substring(content.indexOf('=') + 1).trim { it <= ' ' }.replace("\"", "")
                val index = fileName.lastIndexOf(".")
                if (index != -1) {
                    fileName = "${fileName.substring(0, index - 1)}${GregorianCalendar().timeInMillis}${fileName.substring(index)}"
                    return fileName
                }
            }
        }
        return null
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(FileUploadServlet::class.java)
    }
}
