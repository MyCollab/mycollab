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

import com.mycollab.common.i18n.ErrorI18nEnum
import com.mycollab.configuration.EnDecryptHelper
import com.mycollab.core.InvalidPasswordException
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.utils.PasswordCheckerUtil
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.user.service.UserService
import com.mycollab.servlet.GenericHttpServlet
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import java.util.*
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = ["/user/recoverypassword/action/*"], name = "updateUserPasswordServlet")
class ResetPasswordHandler : GenericHttpServlet() {

    @Autowired
    private lateinit var userService: UserService

    @Throws(ServletException::class, IOException::class)
    public override fun onHandleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val username = request.getParameter("username")
        val password = request.getParameter("password")

        try {
            PasswordCheckerUtil.checkValidPassword(password)
        } catch (e: InvalidPasswordException) {
            throw UserInvalidInputException(e.message ?: "Invalid password $password")
        }

        val user = userService.findUserByUserName(username)
        if (user == null) {
            throw UserInvalidInputException(LocalizationHelper.getMessage(Locale.US,
                    ErrorI18nEnum.ERROR_USER_IS_NOT_EXISTED, username))
        } else {
            user.password = EnDecryptHelper.encryptSaltPassword(password)
            userService.updateWithSession(user, username)
        }
    }
}
