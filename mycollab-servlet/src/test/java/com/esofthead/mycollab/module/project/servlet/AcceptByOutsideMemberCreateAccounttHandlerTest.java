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

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import org.mockito.Spy;

import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.servlet.GenericServletTest;

public class AcceptByOutsideMemberCreateAccounttHandlerTest extends
		GenericServletTest {

	@InjectMocks
	@Spy
	private AcceptByOutsideMemberCreateAccounttHandler acceptByOutsideMemberHandler;

	@Mock
	private ProjectMemberService projectMemberService;

	@Test(expected = UserInvalidInputException.class)
	public void testInvalidPassword() throws ServletException, IOException {
		when(request.getParameter("projectId")).thenReturn("1");
		when(request.getParameter("email")).thenReturn(
				"hainguyen@esofthead.com");
		when(request.getParameter("password")).thenReturn("1");
		when(request.getParameter("sAccountId")).thenReturn("1");
		when(request.getParameter("roleId")).thenReturn("1");

		acceptByOutsideMemberHandler.onHandleRequest(request, response);
	}

	@Test
	public void testValidPassword() throws ServletException, IOException {
		when(request.getParameter("projectId")).thenReturn("1");
		when(request.getParameter("email")).thenReturn(
				"hainguyen@esofthead.com");
		when(request.getParameter("password")).thenReturn("eSoftHead321");
		when(request.getParameter("sAccountId")).thenReturn("1");
		when(request.getParameter("roleId")).thenReturn("1");

		acceptByOutsideMemberHandler.onHandleRequest(request, response);
	}
}
