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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.InvalidPasswordException;
import com.esofthead.mycollab.utils.PasswordCheckerUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("updateUserPasswordServlet")
public class RecoverPasswordUpdateAction extends
		GenericServletRequestHandler {
	private static Logger log = LoggerFactory
			.getLogger(RecoverPasswordUpdateAction.class);

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String errMsg = "";

		String username = request.getParameter("username");

		String password = request.getParameter("password");

		try {
			PasswordCheckerUtil.checkValidPassword(password);
		} catch (InvalidPasswordException e) {
			throw new UserInvalidInputException(e.getMessage());
		}

		SimpleUser simpleUser = new SimpleUser();
		simpleUser.setPassword(PasswordEncryptHelper
				.encryptSaltPassword(password));
		simpleUser.setRegisterstatus(RegisterStatusConstants.ACTIVE);
		simpleUser.setUsername(username);

		try {
			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			userService.updateWithSession(simpleUser, username);
		} catch (Exception e) {
			log.error("Error with update userService", e);
			errMsg = AppContext
					.getMessage(GenericI18Enum.ERROR_USER_NOTICE_INFORMATION_MESSAGE);
			throw new MyCollabException(errMsg);
		}
	}
}
