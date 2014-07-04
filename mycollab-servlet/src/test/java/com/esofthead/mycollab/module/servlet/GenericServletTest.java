package com.esofthead.mycollab.module.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.template.velocity.TemplateEngine;

public class GenericServletTest {

	@Mock
	protected TemplateEngine templateEngine;

	@Mock
	protected HttpServletRequest request;

	@Mock
	protected HttpServletResponse response;

	@Before
	public void setUp() {
		SiteConfiguration.loadInstance(8080);
		MockitoAnnotations.initMocks(this);
	}
}
