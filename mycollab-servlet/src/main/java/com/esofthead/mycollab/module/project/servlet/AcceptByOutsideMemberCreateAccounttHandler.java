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
package com.esofthead.mycollab.module.project.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.InvalidPasswordException;
import com.esofthead.mycollab.core.utils.PasswordCheckerUtil;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@Component("acceptMemberInvitationCreateAccountServlet")
public class AcceptByOutsideMemberCreateAccounttHandler extends
		GenericServletRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(AcceptByOutsideMemberCreateAccounttHandler.class);

	@Autowired
	private ProjectMemberService projectMemberService;

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// email , projectId, sAccountId, projectURL
		Integer projectId = Integer.parseInt(request.getParameter("projectId"));
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		Integer sAccountId = Integer.parseInt(request
				.getParameter("sAccountId"));
		Integer roleId = Integer.parseInt(request.getParameter("roleId"));
		try {
			PasswordCheckerUtil.checkValidPassword(password);
			projectMemberService.acceptProjectInvitationByNewUser(email,
					password, projectId, roleId, sAccountId);
		} catch (NumberFormatException e) {
			throw new UserInvalidInputException(
					"Your request has been refused! Invalid input.");
		} catch (InvalidPasswordException e) {
			throw new UserInvalidInputException(e.getMessage());
		} catch (Exception e) {
			log.error("Error while user try update user password", e);
			throw new MyCollabException(
					"Error in while create your account. We so sorry for this inconvenience");
		}
	}
}
