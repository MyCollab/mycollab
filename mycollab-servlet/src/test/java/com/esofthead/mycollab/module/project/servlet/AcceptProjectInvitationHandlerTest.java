package com.esofthead.mycollab.module.project.servlet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.servlet.GenericServletTest;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class AcceptProjectInvitationHandlerTest extends GenericServletTest {

	@InjectMocks
	@Spy
	private AcceptProjectInvitationHandler verifyPrjMemberHandler;

	@Mock
	private ProjectMemberService projectMemberService;

	@Mock
	private UserService userService;

	@Mock
	private UserAccountMapper userAccountMapper;

	@Mock
	private ProjectService projectService;

	@Test(expected = ResourceNotFoundException.class)
	public void tetsGetPathIsNull() throws ServletException, IOException {
		when(request.getPathInfo()).thenReturn(null);
		verifyPrjMemberHandler.onHandleRequest(request, response);
	}

	@Test
	public void testExpireInvitation() throws ServletException, IOException {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -8);
		String pathInfo = ProjectLinkGenerator.generateAcceptInvitationParams(
				"hainguyen@mycollab.com", 1, 1, 1, 1,
				"hainguyen@esofthead.com", "hainguyen@esofthead.com", calendar);

		when(request.getPathInfo()).thenReturn(pathInfo);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));

		verifyPrjMemberHandler.onHandleRequest(request, response);

		ArgumentCaptor<String> strArgument = ArgumentCaptor
				.forClass(String.class);

		ArgumentCaptor<Map> mapArgument = ArgumentCaptor.forClass(Map.class);

		verify(verifyPrjMemberHandler).generatePageByTemplate(
				strArgument.capture(), mapArgument.capture());
		Assert.assertEquals(AcceptProjectInvitationHandler.EXPIER_PAGE,
				strArgument.getValue());
	}

	@Test
	public void testInviteMemberIsAlreadyBelongToAccount() throws IOException,
			ServletException {
		GregorianCalendar calendar = new GregorianCalendar();
		String pathInfo = ProjectLinkGenerator.generateAcceptInvitationParams(
				"hainguyen@mycollab.com", 1, 1, 1, 1,
				"hainguyen@esofthead.com", "hainguyen@esofthead.com", calendar);

		when(request.getPathInfo()).thenReturn(pathInfo);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));

		when(userService.findUserByUserName("hainguyen@mycollab.com"))
				.thenReturn(new SimpleUser());

		List<UserAccount> users = Arrays.asList(new UserAccount());
		when(userAccountMapper.selectByExample(any(UserAccountExample.class)))
				.thenReturn(users);

		verifyPrjMemberHandler.onHandleRequest(request, response);
	}

	@Test
	public void testInviteMemberNotBelongToSystem() throws IOException,
			ServletException {
		GregorianCalendar calendar = new GregorianCalendar();
		String pathInfo = ProjectLinkGenerator.generateAcceptInvitationParams(
				"hainguyen@mycollab.com", 1, 1, 1, 1,
				"hainguyen@esofthead.com", "hainguyen@esofthead.com", calendar);

		when(request.getPathInfo()).thenReturn(pathInfo);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));

		verifyPrjMemberHandler.onHandleRequest(request, response);

		ArgumentCaptor<String> strArgument = ArgumentCaptor
				.forClass(String.class);

		ArgumentCaptor<Map> mapArgument = ArgumentCaptor.forClass(Map.class);

		verify(verifyPrjMemberHandler).generatePageByTemplate(
				strArgument.capture(), mapArgument.capture());
		Assert.assertEquals(
				AcceptProjectInvitationHandler.OUTSIDE_MEMBER_WELCOME_PAGE,
				strArgument.getValue());
	}
}
