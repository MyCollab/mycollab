package com.esofthead.mycollab.module.billing.servlet;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.servlet.GenericServletTest;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;

public class ResetPasswordHandlerTest extends GenericServletTest {
	@InjectMocks
	@Spy
	private ResetPasswordHandler resetPasswordHandler;

	@Mock
	private UserService userService;

	@Test(expected = UserInvalidInputException.class)
	public void testInvalidPassword() throws ServletException, IOException {
		when(request.getParameter("username")).thenReturn(
				"hainguyen@esofthead.com");
		when(request.getParameter("password")).thenReturn("123456");
		resetPasswordHandler.onHandleRequest(request, response);
	}

	@Test(expected = UserInvalidInputException.class)
	public void testResetPasswordOfInactiveUser() throws ServletException,
			IOException {
		when(request.getParameter("username")).thenReturn(
				"hainguyen@esofthead.com");
		when(request.getParameter("password")).thenReturn("abc123");
		when(userService.findUserByUserName(anyString())).thenReturn(null);
		resetPasswordHandler.onHandleRequest(request, response);
	}

	@Test
	public void testResetEmailSuccessful() throws ServletException, IOException {
		when(request.getParameter("username")).thenReturn(
				"hainguyen@esofthead.com");
		when(request.getParameter("password")).thenReturn("abc123");
		when(userService.findUserByUserName(anyString()))
				.thenReturn(new User());
		resetPasswordHandler.onHandleRequest(request, response);

		ArgumentCaptor<String> strArgument = ArgumentCaptor
				.forClass(String.class);
		ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
		verify(userService, times(1)).updateWithSession(userArgument.capture(),
				strArgument.capture());
	}
}
