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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.VelocityWebServletRequestHandler;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@WebServlet(name = "denyUserInviteServlet", urlPatterns = "/user/deny_invite/*")
public class DenyInvitationPage extends VelocityWebServletRequestHandler {

	private static String USER_DENY_FEEDBACK_TEMPLATE = "templates/page/user/UserDenyInvitationPage.mt";
	private static String USER_HAS_DENIED_PAGE = "templates/page/user/UserDeniedPage.mt";

	private static final Logger LOG = LoggerFactory
			.getLogger(DenyInvitationPage.class);

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		try {
			if (pathInfo != null) {
				UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);
				int accountId = urlTokenizer.getInt();
				String username = urlTokenizer.getString();
				String inviterName = urlTokenizer.getString();
				String inviterEmail = urlTokenizer.getString();
				String subdomain = "";

				if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
					subdomain = urlTokenizer.getString();
				}

				UserService userService = ApplicationContextUtil
						.getSpringBean(UserService.class);
				SimpleUser checkUser = userService.findUserByUserNameInAccount(
						username, accountId);

				if (checkUser == null) {
					// this user no long exist on System page
					PageGeneratorUtil.responeUserNotExistPage(response,
							username, request.getContextPath() + "/");
					return;
				} else {

					if (checkUser.getRegisterstatus().equals(
							RegisterStatusConstants.ACTIVE)) {
						// You cant deny , User has active , go to login Page
						Map<String, Object> context = new HashMap<>();
						context.put("loginURL", request.getContextPath() + "/");
						String html = generatePageByTemplate(
								response.getLocale(),
								"templates/page/project/RefuseUserDenyActionPage.mt",
								context);
						PrintWriter out = response.getWriter();
						out.println(html);
						return;
					} else if (checkUser.getRegisterstatus().equals(
							RegisterStatusConstants.VERIFICATING)) {
						UserSearchCriteria criteria = new UserSearchCriteria();
						criteria.setUsername(new StringSearchField(username));
						criteria.setSaccountid(new NumberSearchField(accountId));
						userService.pendingUserAccount(username, accountId);

						String redirectURL = SiteConfiguration
								.getSiteUrl(subdomain)
								+ "project/member/feedback/";

						Map<String, Object> context = new HashMap<>();
						context.put("inviterEmail", inviterEmail);
						context.put("redirectURL", redirectURL);
						context.put("toEmail", checkUser.getEmail());
						context.put("toName", checkUser.getUsername());
						context.put("inviterName", inviterName);
						context.put("projectName", "");
						context.put("sAccountId", 0);
						context.put("projectId", 0);
						context.put("projectRoleId", 0);
						String html = generatePageByTemplate(
								response.getLocale(),
								USER_DENY_FEEDBACK_TEMPLATE, context);

						PrintWriter out = response.getWriter();
						out.println(html);
						return;
					} else if (checkUser.getRegisterstatus().equals(
							RegisterStatusConstants.DELETE)) {

						Map<String, Object> context = new HashMap<>();
						context.put("loginURL", request.getContextPath() + "/");

						String html = generatePageByTemplate(
								response.getLocale(), USER_HAS_DENIED_PAGE,
								context);

						PrintWriter out = response.getWriter();
						out.println(html);
						return;
					}
				}
			}
			throw new ResourceNotFoundException();
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("Error with userService", e);
			throw new MyCollabException(e);
		}
	}

}
