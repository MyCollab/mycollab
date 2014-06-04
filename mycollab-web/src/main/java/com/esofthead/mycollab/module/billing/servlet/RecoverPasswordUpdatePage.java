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

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.VelocityWebServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("recoverUserPasswordServlet")
public class RecoverPasswordUpdatePage extends
		VelocityWebServletRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(RecoverPasswordUpdatePage.class);

	@Autowired
	private UserService userService;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pathInfo = request.getPathInfo();
		try {
			if (pathInfo != null) {
				UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);
				String username = urlTokenizer.getString();
				User user = userService.findUserByUserName(username);
				if (user == null) {
					PageGeneratorUtil.responeUserNotExistPage(response,
							request.getContextPath() + "/");
					return;
				} else {
					String loginURL = (SiteConfiguration.getDeploymentMode() == DeploymentMode.SITE) ? ("https://www.mycollab.com/sign-in?username=" + username)
							: (request.getContextPath() + "/");

					String redirectURL = request.getContextPath() + "/"
							+ "user/recoverypassword/action";

					Map<String, Object> context = new HashMap<String, Object>();
					context.put("username", username);
					context.put("loginURL", loginURL);
					context.put("redirectURL", redirectURL);

					String html = generatePageByTemplate(
							"templates/page/user/UserRecoveryPasswordPage.mt",
							context);
					PrintWriter out = response.getWriter();
					out.print(html);
					return;
				}
			} else {
				throw new ResourceNotFoundException(
						"Can not recover user password with context "
								+ pathInfo);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new ResourceNotFoundException(e);
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error with userService", e);
			throw new MyCollabException(e);
		}
	}

}
