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
package com.mycollab.module.billing.servlet;

import com.mycollab.common.UrlTokenizer;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.module.billing.UserStatusConstants;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.servlet.GenericHttpServlet;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = "/user/confirm_signup/*", name = "userconfirmsignupServlet")
public class ConfirmEmailHandler extends GenericHttpServlet {

    @Autowired
    private UserService userServices;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TemplateException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);
            String username = urlTokenizer.getString();
            Integer accountId = urlTokenizer.getInt();
            SimpleUser user = userServices.findUserByUserNameInAccount(username, accountId);

            if (user != null) {
                user.setStatus(UserStatusConstants.EMAIL_VERIFIED);
                userServices.updateWithSession(user, username);
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                PageGeneratorUtil.responseUserNotExistPage(response, username, request.getContextPath() + "/");
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
