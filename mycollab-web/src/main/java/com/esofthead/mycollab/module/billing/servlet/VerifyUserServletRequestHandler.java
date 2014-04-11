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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.dao.UserAccountInvitationMapper;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.domain.UserAccountInvitationExample;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;
import com.esofthead.template.velocity.TemplateContext;
import com.esofthead.template.velocity.TemplateEngine;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("acceptUserInvitationServlet")
public class VerifyUserServletRequestHandler extends
		GenericServletRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(VerifyUserServletRequestHandler.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserAccountMapper userAccountMapper;

	@Autowired
	private UserAccountInvitationMapper userAccountInvitationMapper;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String subdomain = "";
		String loginURL = request.getContextPath() + "/";

		try {
			if (pathInfo != null) {
				UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);

				int accountId = urlTokenizer.getInt();
				String username = urlTokenizer.getString();
				subdomain = urlTokenizer.getString();

				User user = userService.findUserByUserName(username);
				SimpleUser userInAccount = userService
						.findUserByUserNameInAccount(username, accountId);

				if (user == null || userInAccount == null) {
					PageGeneratorUtil.responeUserNotExistPage(response,
							request.getContextPath() + "/");
					return;
				} else {
					if (userInAccount.getRegisterstatus().equals(
							RegisterStatusConstants.ACTIVE)) {
						log.debug("Forward user {} to page {}",
								user.getUsername(), request.getContextPath());
						response.sendRedirect(request.getContextPath() + "/");
						return;
					} else {
						// remove account invitation
						UserAccountInvitationExample userAccountInvitationExample = new UserAccountInvitationExample();
						userAccountInvitationExample.createCriteria()
								.andUsernameEqualTo(username)
								.andAccountidEqualTo(accountId);
						userAccountInvitationMapper
								.deleteByExample(userAccountInvitationExample);

						if (user.getPassword() == null
								|| user.getPassword().trim().equals("")) {
							log.debug(
									"User {} has null password. It seems he is the new user join to mycollab. Redirect him to page let him update his password {}",
									user.getUsername(),
									BeanUtility.printBeanObj(user));
							// forward to page create password for new user
							String redirectURL = SiteConfiguration
									.getSiteUrl(subdomain)
									+ "user/confirm_invite/update_info/";
							String html = generateUserFillInformationPage(
									request, accountId, username,
									user.getEmail(), redirectURL, loginURL);
							PrintWriter out = response.getWriter();
							out.print(html);
							return;
						} else {
							log.debug("Forward user {} to page {}",
									user.getUsername(),
									request.getContextPath());
							// redirect to account site
							userService.updateUserAccountStatus(username,
									accountId, RegisterStatusConstants.ACTIVE);
							response.sendRedirect(request.getContextPath()
									+ "/");
							return;
						}
					}
				}
			}
			throw new ResourceNotFoundException();
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException();
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException();
		} catch (Exception e) {
			log.error("Error when delete UserAccountInvitation", e);
			throw new MyCollabException(e);
		}

	}

	private String generateUserFillInformationPage(HttpServletRequest request,
			int accountId, String username, String email, String redirectURL,
			String loginURL) {
		String template = "templates/page/user/FillUserInformation.mt";
		TemplateContext context = new TemplateContext();
		Reader reader;
		try {
			reader = new InputStreamReader(
					VerifyUserServletRequestHandler.class.getClassLoader()
							.getResourceAsStream(template), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(
					VerifyUserServletRequestHandler.class.getClassLoader()
							.getResourceAsStream(template));
		}

		context.put("username", username);
		context.put("accountId", accountId);
		context.put("email", email);
		context.put("redirectURL", redirectURL);
		context.put("loginURL", loginURL);

		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		TemplateEngine.evaluate(context, writer, "log task", reader);
		return writer.toString();
	}
}
