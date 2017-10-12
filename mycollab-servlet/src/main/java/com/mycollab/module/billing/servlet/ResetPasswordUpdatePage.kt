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

import com.mycollab.common.UrlTokenizer
import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.module.user.service.UserService
import com.mycollab.servlet.TemplateWebServletRequestHandler
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(name = "recoverUserPasswordServlet", urlPatterns = arrayOf("/user/recoverypassword/*"))
class ResetPasswordUpdatePage : TemplateWebServletRequestHandler() {

    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val deploymentMode: IDeploymentMode? = null

    override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val pathInfo = request.pathInfo
        try {
            if (pathInfo != null) {
                val urlTokenizer = UrlTokenizer(pathInfo)
                val username = urlTokenizer.getString()
                val user = userService!!.findUserByUserName(username)
                if (user == null) {
                    PageGeneratorUtil.responseUserNotExistPage(response, username, request.contextPath + "/")
                } else {
                    val loginURL = if (deploymentMode!!.isDemandEdition)
                        "https://www.mycollab.com/sign-in?username=" + username
                    else
                        request.contextPath + "/"

                    val redirectURL = request.contextPath + "/user/recoverypassword/action"
                    val context = mapOf("username" to username,
                            "loginURL" to loginURL,
                            "redirectURL" to redirectURL)
                    val html = generatePageByTemplate(response.locale, "pageUserRecoveryPassword.ftl", context)
                    val out = response.writer
                    out.print(html)
                }
            } else {
                throw ResourceNotFoundException("Can not recover user password with context path is null")
            }
        } catch (e: Exception) {
            throw MyCollabException(e)
        }
    }
}
