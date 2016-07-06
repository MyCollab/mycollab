/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.billing.servlet;

import com.mycollab.common.UrlTokenizer;
import com.mycollab.configuration.IDeploymentMode;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.servlet.TemplateWebServletRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(name = "recoverUserPasswordServlet", urlPatterns = "/user/recoverypassword/*")
public class ResetPasswordUpdatePage extends TemplateWebServletRequestHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private IDeploymentMode deploymentMode;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo != null) {
                UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);
                String username = urlTokenizer.getString();
                User user = userService.findUserByUserName(username);
                if (user == null) {
                    PageGeneratorUtil.responseUserNotExistPage(response, username, request.getContextPath() + "/");
                } else {
                    String loginURL = (deploymentMode.isDemandEdition())
                            ? ("https://www.mycollab.com/sign-in?username=" + username) : (request.getContextPath() + "/");

                    String redirectURL = request.getContextPath() + "/" + "user/recoverypassword/action";
                    Map<String, Object> context = new HashMap<>();
                    context.put("username", username);
                    context.put("loginURL", loginURL);
                    context.put("redirectURL", redirectURL);

                    String html = generatePageByTemplate(response.getLocale(), "pageUserRecoveryPassword.ftl", context);
                    PrintWriter out = response.getWriter();
                    out.print(html);
                }
            } else {
                throw new ResourceNotFoundException("Can not recover user password with context path is null");
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

}
