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
package com.mycollab.module.file.servlet

import com.mycollab.core.ResourceNotFoundException
import com.mycollab.core.utils.MimeTypesUtil
import com.mycollab.servlet.GenericHttpServlet
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@WebServlet(urlPatterns = ["/assets/*"], name = "assetHandler")
class AssetHandler : GenericHttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val path = request.pathInfo
        var resourcePath = "assets$path"

        var inputStream: InputStream? = AssetHandler::class.java.classLoader.getResourceAsStream(resourcePath)

        if (inputStream == null) {
            resourcePath = "VAADIN/themes/mycollab$path"
            inputStream = AssetHandler::class.java.classLoader.getResourceAsStream(resourcePath)
        }

        if (inputStream != null) {
            LOG.debug("Get resource {} successfully ", resourcePath)
            response.setHeader("Content-Type", MimeTypesUtil.detectMimeType(path))
            response.setHeader("Content-Length", inputStream.available().toString())

            BufferedInputStream(inputStream).use { input ->
                BufferedOutputStream(response.outputStream).use { output ->
                    val buffer = ByteArray(8192)
                    var length = input.read(buffer)
                    while (length > 0) {
                        output.write(buffer, 0, length)
                        length = input.read(buffer)
                    }
                }
            }
        } else {
            throw ResourceNotFoundException("Can not find resource has path $path")
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AssetHandler::class.java)
    }
}
