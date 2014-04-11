/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.billing.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.servlet.DenyProjectMemberInvitationServletRequestHandler.FeedBackPageGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.template.velocity.TemplateContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("denyUserInviteServlet")
public class DenyUserServletRequestHandler extends GenericServletRequestHandler {

	private static String USER_DENY_FEEDBACK_TEMPLATE = "templates/page/user/UserDenyInvitationPage.mt";
	private static String USER_HAS_DENIED_PAGE = "templates/page/user/UserDeniedPage.mt";

	private static Logger log = LoggerFactory
			.getLogger(DenyUserServletRequestHandler.class);

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
				String subdomain = urlTokenizer.getString();

				UserService userService = ApplicationContextUtil
						.getSpringBean(UserService.class);
				SimpleUser checkUser = userService.findUserByUserNameInAccount(
						username, accountId);

				if (checkUser == null) {
					// this user no long exist on System page
					PageGeneratorUtil.responeUserNotExistPage(response,
							request.getContextPath() + "/");
					return;
				} else {
					if (checkUser.getRegisterstatus().equals(
							RegisterStatusConstants.ACTIVE)) {
						// You cant deny , Userhas active , go to login Page
						String html = generateRefuseUserDenyActionPage(
								request.getContextPath() + "/",
								"templates/page/project/RefuseUserDenyActionPage.mt");
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
						String html = FeedBackPageGenerator
								.generateDenyFeedbacktoInviter(0, 0,
										inviterEmail, inviterName, redirectURL,
										checkUser.getEmail(),
										checkUser.getUsername(), "",
										USER_DENY_FEEDBACK_TEMPLATE, 0);
						PrintWriter out = response.getWriter();
						out.println(html);
						return;
					} else if (checkUser.getRegisterstatus().equals(
							RegisterStatusConstants.DELETE)) {
						String html = generateRefuseUserDenyActionPage(
								request.getContextPath() + "/",
								USER_HAS_DENIED_PAGE);
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
			log.error("Error with userService", e);
			throw new MyCollabException(e);
		}
	}

	private String generateRefuseUserDenyActionPage(String loginURL,
			String pageNotFoundTemplate) {
		TemplateContext context = new TemplateContext();

		Reader reader;
		try {
			reader = new InputStreamReader(
					DenyUserServletRequestHandler.class.getClassLoader()
							.getResourceAsStream(pageNotFoundTemplate), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(DenyUserServletRequestHandler.class
					.getClassLoader().getResourceAsStream(pageNotFoundTemplate));
		}
		context.put("loginURL", loginURL);
		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();

		VelocityEngine templateEngine = ApplicationContextUtil
				.getSpringBean(VelocityEngine.class);
		templateEngine.evaluate(context.getVelocityContext(), writer,
				"log task", reader);

		return writer.toString();
	}
}
