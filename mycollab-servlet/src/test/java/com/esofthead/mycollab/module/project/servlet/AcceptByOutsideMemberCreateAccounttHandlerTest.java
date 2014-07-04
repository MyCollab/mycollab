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
