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

import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.core.utils.FileUtils
import com.mycollab.servlet.GenericHttpServlet
import org.apache.commons.io.FilenameUtils
import java.io.*
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = ["/file/avatar/*"], name = "userAvatarFSServlet")
class UserAvatarHttpServletRequestHandler : GenericHttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        if (SiteConfiguration.isDemandEdition()) {
            throw MyCollabException("This servlet support file system setting only")
        }

        var path: String? = request.pathInfo

        if (path != null) {
            path = FilenameUtils.getBaseName(path)
            val lastIndex = path!!.lastIndexOf("_")
            if (lastIndex > 0) {
                val username = path.substring(0, lastIndex)
                val size = Integer.valueOf(path.substring(lastIndex + 1, path.length))!!

                val userAvatarFile = File(FileUtils.homeFolder, "/avatar/${username}_$size.png")
                val avatarInputStream = FileInputStream(userAvatarFile)

                response.setHeader("Content-Type", "image/png")
                response.setHeader("Content-Length", avatarInputStream.available().toString())

                BufferedInputStream(avatarInputStream).use { input ->
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
                throw ResourceNotFoundException("Invalid path $path")
            }
        }
    }
}
