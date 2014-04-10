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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("userconfirmsignupServlet")
public class UserSignUpConfirmEmailActionHandler extends
		GenericServletRequestHandler {

	@Autowired
	private UserService userServices;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		try {
			if (pathInfo != null) {
				if (pathInfo.startsWith("/")) {
					pathInfo = pathInfo.substring(1);

					pathInfo = UrlEncodeDecoder.decode(pathInfo);

					String username = pathInfo.substring(0,
							pathInfo.indexOf("/"));
					pathInfo = pathInfo.substring(username.length() + 1);

					Integer accountId = Integer.parseInt(pathInfo);

					SimpleUser user = userServices.findUserByUserNameInAccount(
							username, accountId);
					if (user != null) {
						user.setStatus(UserStatusConstants.EMAIL_VERIFIED);
						userServices.updateWithSession(user, username);
						response.sendRedirect(request.getContextPath() + "/");
						return;
					} else {
						PageGeneratorUtil.responeUserNotExistPage(
								response, request.getContextPath() + "/");
						return;
					}
				}
			}
			throw new ResourceNotFoundException();
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}
}
