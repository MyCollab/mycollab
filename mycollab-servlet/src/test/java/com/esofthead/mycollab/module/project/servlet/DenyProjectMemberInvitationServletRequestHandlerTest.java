package com.esofthead.mycollab.module.project.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.template.velocity.TemplateEngine;

@RunWith(MockitoJUnitRunner.class)
public class DenyProjectMemberInvitationServletRequestHandlerTest {

	@InjectMocks
	@Spy
	private DenyProjectMemberInvitationServletRequestHandler denyProjectMemberRequestHandler;

	@Mock
	private TemplateEngine templateEngine;

	@Mock
	private ProjectMemberService projectMemberService;

	@Mock
	private RelayEmailNotificationService relayEmailService;

	@Mock
	private ProjectService projectService;

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Before
	public void setUp() {
		SiteConfiguration.loadInstance(8080);

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	public void testCannotFindProject() throws ServletException, IOException {

		String pathInfo = ProjectLinkGenerator.generateDenyInvitationParams(1,
				1, 1, "hainguyen@esofthead.com", "hainguyen@esofthead.com");
		when(request.getPathInfo()).thenReturn(pathInfo);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));

		denyProjectMemberRequestHandler.onHandleRequest(request, response);

		ArgumentCaptor<String> strArgument = ArgumentCaptor
				.forClass(String.class);

		ArgumentCaptor<Map> mapArgument = ArgumentCaptor.forClass(Map.class);

		verify(denyProjectMemberRequestHandler).generatePageByTemplate(
				strArgument.capture(), mapArgument.capture());
		Assert.assertEquals(
				DenyProjectMemberInvitationServletRequestHandler.PROJECT_NOT_AVAILABLE_TEMPLATE,
				strArgument.getValue());
	}
}
