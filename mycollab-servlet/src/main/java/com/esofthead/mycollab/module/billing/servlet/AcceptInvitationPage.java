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
package com.esofthead.mycollab.module.billing.servlet;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.IDeploymentMode;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.VelocityWebServletRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static com.esofthead.mycollab.core.utils.StringUtils.isBlank;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(name = "acceptUserInvitationServlet", urlPatterns = "/user/confirm_invite/*")
public class AcceptInvitationPage extends VelocityWebServletRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AcceptInvitationPage.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private IDeploymentMode deploymentMode;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String subDomain = "";
        String loginURL = request.getContextPath() + "/";

        try {
            if (pathInfo != null) {
                UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);

                Integer accountId = urlTokenizer.getInt();
                String username = urlTokenizer.getString();
                if (deploymentMode.isDemandEdition()) {
                    subDomain = urlTokenizer.getString();
                }

                User user = userService.findUserByUserName(username);
                if (user == null) {
                    PageGeneratorUtil.responeUserNotExistPage(response, username, request.getContextPath() + "/");
                    return;
                } else {
                    if (!UserStatusConstants.EMAIL_VERIFIED.equals(user.getStatus())) {
                        user.setStatus(UserStatusConstants.EMAIL_VERIFIED);
                        userService.updateWithSession(user, "");
                    }

                    SimpleUser userInAccount = userService.findUserByUserNameInAccount(username, accountId);

                    if (userInAccount == null) {
                        PageGeneratorUtil.responeUserNotExistPage(response, username, request.getContextPath() + "/");
                        return;
                    } else {
                        if (userInAccount.getRegisterstatus().equals(RegisterStatusConstants.ACTIVE)) {
                            LOG.debug("Forward user {} to page {}", user.getUsername(), request.getContextPath());
                            response.sendRedirect(request.getContextPath() + "/");
                            return;
                        } else {
                            UserAccount userAccount = new UserAccount();
                            userAccount.setRegisterstatus(RegisterStatusConstants.ACTIVE);
                            userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
                            UserAccountExample userAccountExample = new UserAccountExample();
                            userAccountExample.createCriteria().andAccountidEqualTo(accountId).andUsernameEqualTo(username);
                            userAccountMapper.updateByExampleSelective(userAccount, userAccountExample);

                            if (isBlank(user.getPassword())) {
                                LOG.debug(
                                        "User {} has null password. It seems he is the new user join to mycollab. Redirect him to page let him update his password {}",
                                        user.getUsername(), BeanUtility.printBeanObj(user));
                                // forward to page create password for new user
                                String redirectURL = String.format("%suser/confirm_invite/update_info/", SiteConfiguration.getSiteUrl(subDomain));

                                Map<String, Object> context = new HashMap<>();
                                context.put("username", username);
                                context.put("accountId", accountId);
                                context.put("email", user.getEmail());
                                context.put("redirectURL", redirectURL);
                                context.put("loginURL", loginURL);
                                String html = generatePageByTemplate(response.getLocale(), "templates/page/user/FillUserInformation.mt", context);
                                PrintWriter out = response.getWriter();
                                out.print(html);
                            } else {
                                LOG.debug("Forward user {} to page {}", user.getUsername(), request.getContextPath());
                                // redirect to account site
                                userService.updateUserAccountStatus(username, accountId, RegisterStatusConstants.ACTIVE);
                                response.sendRedirect(request.getContextPath() + "/");
                            }
                        }
                    }
                }
            } else {
                throw new ResourceNotFoundException();
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
