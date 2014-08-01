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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.servlet.GenericServletTest;

public class DenyProjectInvitationHandlerTest extends GenericServletTest {

	@InjectMocks
	@Spy
	private DenyProjectInvitationHandler denyInvitationHandler;

	@Mock
	private ProjectMemberService projectMemberService;

	@Mock
	private RelayEmailNotificationService relayEmailService;

	@Mock
	private ProjectService projectService;

	@Test
	public void testCannotFindProject() throws ServletException, IOException {
		String pathInfo = ProjectLinkGenerator.generateDenyInvitationParams(
				"hainguyen@mycollab.com", 1, 1, "hainguyen@esofthead.com",
				"hainguyen@esofthead.com");
		when(request.getPathInfo()).thenReturn(pathInfo);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));

		denyInvitationHandler.onHandleRequest(request, response);

		ArgumentCaptor<Locale> localeArgument = ArgumentCaptor
				.forClass(Locale.class);

		ArgumentCaptor<String> strArgument = ArgumentCaptor
				.forClass(String.class);

		ArgumentCaptor<Map> mapArgument = ArgumentCaptor.forClass(Map.class);

		verify(denyInvitationHandler).generatePageByTemplate(
				localeArgument.capture(), strArgument.capture(),
				mapArgument.capture());
		Assert.assertEquals(
				DenyProjectInvitationHandler.PROJECT_NOT_AVAILABLE_TEMPLATE,
				strArgument.getValue());
	}

	@Test
	public void testDenyWithProjectMember() throws ServletException,
			IOException {
		String pathInfo = ProjectLinkGenerator.generateDenyInvitationParams(
				"hainguyen@mycollab.com", 1, 1, "hainguyen@esofthead.com",
				"hainguyen@esofthead.com");
		when(request.getPathInfo()).thenReturn(pathInfo);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));

		when(projectService.findById(1, 1)).thenReturn(new SimpleProject());
		when(
				projectMemberService.findMemberByUsername(any(String.class),
						any(Integer.class), any(Integer.class))).thenReturn(
				new SimpleProjectMember());

		denyInvitationHandler.onHandleRequest(request, response);

		ArgumentCaptor<Locale> localeArgument = ArgumentCaptor
				.forClass(Locale.class);

		ArgumentCaptor<String> strArgument = ArgumentCaptor
				.forClass(String.class);

		ArgumentCaptor<Map> mapArgument = ArgumentCaptor.forClass(Map.class);

		verify(denyInvitationHandler).generatePageByTemplate(
				localeArgument.capture(), strArgument.capture(),
				mapArgument.capture());
		Assert.assertEquals(
				DenyProjectInvitationHandler.REFUSE_MEMBER_DENY_TEMPLATE,
				strArgument.getValue());
	}

	@Test
	public void testDenyNotFoundProjectMember() throws ServletException,
			IOException {
		String pathInfo = ProjectLinkGenerator.generateDenyInvitationParams(
				"hainguyen@mycollab.com", 1, 1, "hainguyen@esofthead.com",
				"hainguyen@esofthead.com");
		when(request.getPathInfo()).thenReturn(pathInfo);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));

		when(projectService.findById(1, 1)).thenReturn(new SimpleProject());

		denyInvitationHandler.onHandleRequest(request, response);

		ArgumentCaptor<Locale> localeArgument = ArgumentCaptor
				.forClass(Locale.class);

		ArgumentCaptor<String> strArgument = ArgumentCaptor
				.forClass(String.class);

		ArgumentCaptor<Map> mapArgument = ArgumentCaptor.forClass(Map.class);

		verify(denyInvitationHandler).generatePageByTemplate(
				localeArgument.capture(), strArgument.capture(),
				mapArgument.capture());
		Assert.assertEquals(
				DenyProjectInvitationHandler.DENY_FEEDBACK_TEMPLATE,
				strArgument.getValue());
	}

	@Test(expected = MyCollabException.class)
	public void testNullParam() throws ServletException, IOException {
		when(request.getPathInfo()).thenReturn(null);
		denyInvitationHandler.onHandleRequest(request, response);
	}

	@Test(expected = MyCollabException.class)
	public void testInvalidParams() throws ServletException, IOException {
		when(request.getPathInfo()).thenReturn("1");
		denyInvitationHandler.onHandleRequest(request, response);
	}
}
