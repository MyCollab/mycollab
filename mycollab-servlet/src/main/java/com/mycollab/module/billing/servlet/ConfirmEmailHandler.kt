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
package com.mycollab.module.billing.servlet

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.module.billing.UserStatusConstants
import com.mycollab.module.user.service.UserService
import com.mycollab.servlet.GenericHttpServlet
import freemarker.template.TemplateException
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = arrayOf("/user/confirm_signup/*"), name = "userconfirmsignupServlet")
class ConfirmEmailHandler : GenericHttpServlet() {

    @Autowired
    private lateinit var userServices: UserService

    @Throws(ServletException::class, IOException::class, TemplateException::class)
    override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val pathInfo = request.pathInfo
        if (pathInfo != null) {
            val urlTokenizer = UrlTokenizer(pathInfo)
            val username = urlTokenizer.getString()
            val accountId = urlTokenizer.getInt()
            val user = userServices.findUserByUserNameInAccount(username, accountId)

            if (user != null) {
                user.status = UserStatusConstants.EMAIL_VERIFIED
                userServices.updateWithSession(user, username)
                response.sendRedirect(request.contextPath + "/")
            } else {
                PageGeneratorUtil.responseUserNotExistPage(response, username, request.contextPath + "/")
            }
        } else {
            throw ResourceNotFoundException("Can not find user from path $pathInfo")
        }
    }
}
