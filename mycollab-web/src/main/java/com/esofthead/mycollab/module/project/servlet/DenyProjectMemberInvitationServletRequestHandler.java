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
package com.esofthead.mycollab.module.project.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.configuration.SharingOptions;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;
import com.esofthead.template.velocity.TemplateContext;
import com.esofthead.template.velocity.TemplateEngine;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
@Component("denyMemberInvitationServlet")
public class DenyProjectMemberInvitationServletRequestHandler extends
		GenericServletRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(DenyProjectMemberInvitationServletRequestHandler.class);

	private static String DENY_FEEDBACK_TEMPLATE = "templates/page/project/MemberDenyInvitationPage.mt";
	private static String REFUSE_MEMBER_DENY_TEMPLATE = "templates/page/project/RefuseMemberDenyActionPage.mt";

	@Autowired
	private ProjectMemberService projectMemberService;

	@Autowired
	private RelayEmailNotificationService relayEmailService;

	@Autowired
	private ProjectService projectService;

	private String generateRefuseMemberDenyActionPage(String projectLinkURL) {
		TemplateContext context = new TemplateContext();

		Reader reader;
		try {
			reader = new InputStreamReader(
					DenyProjectMemberInvitationServletRequestHandler.class
							.getClassLoader().getResourceAsStream(
									REFUSE_MEMBER_DENY_TEMPLATE), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(
					DenyProjectMemberInvitationServletRequestHandler.class
							.getClassLoader().getResourceAsStream(
									REFUSE_MEMBER_DENY_TEMPLATE));
		}
		context.put("projectLinkURL", projectLinkURL);
		Map<String, String> defaultUrls = new HashMap<String, String>();

		SharingOptions sharingOptions = SiteConfiguration.getSharingOptions();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
		defaultUrls.put("facebook_url", sharingOptions.getFacebookUrl());
		defaultUrls.put("google_url", sharingOptions.getGoogleplusUrl());
		defaultUrls.put("linkedin_url", sharingOptions.getLinkedinUrl());
		defaultUrls.put("twitter_url", sharingOptions.getTwitterUrl());

		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		TemplateEngine.evaluate(context, writer, "log task", reader);
		return writer.toString();
	}

	public static class FeedBackPageGenerator {
		public static String generateDenyFeedbacktoInviter(Integer sAccountId,
				Integer projectId, String inviterEmail, String inviterName,
				String redirectURL, String memberEmail, String memberName,
				String projectName, String templateURL, Integer projectRoleId) {
			TemplateContext context = new TemplateContext();

			Reader reader;
			try {
				reader = new InputStreamReader(
						DenyProjectMemberInvitationServletRequestHandler.class
								.getClassLoader().getResourceAsStream(
										templateURL), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				reader = new InputStreamReader(
						DenyProjectMemberInvitationServletRequestHandler.class
								.getClassLoader().getResourceAsStream(
										templateURL));
			}
			context.put("inviterEmail", inviterEmail);
			context.put("redirectURL", redirectURL);
			context.put("toEmail", memberEmail);
			context.put("toName", memberName);
			context.put("inviterName", inviterName);
			context.put("projectName", projectName);
			context.put("sAccountId", sAccountId);
			context.put("projectId", projectId);
			context.put("projectRoleId", projectRoleId);

			Map<String, String> defaultUrls = new HashMap<String, String>();

			SharingOptions sharingOptions = SiteConfiguration
					.getSharingOptions();

			defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
			defaultUrls.put("facebook_url", sharingOptions.getFacebookUrl());
			defaultUrls.put("google_url", sharingOptions.getGoogleplusUrl());
			defaultUrls.put("linkedin_url", sharingOptions.getLinkedinUrl());
			defaultUrls.put("twitter_url", sharingOptions.getTwitterUrl());

			context.put("defaultUrls", defaultUrls);

			StringWriter writer = new StringWriter();
			TemplateEngine.evaluate(context, writer, "log task", reader);
			return writer.toString();
		}
	}

	public static class ProjectRemovedGenerator {
		public static void responePageProjectHasRemoved(
				HttpServletRequest request, HttpServletResponse response)
				throws IOException {
			String pageNotFoundTemplate = "templates/page/project/ProjectNotAvaiablePage.mt";
			TemplateContext context = new TemplateContext();

			Reader reader;
			try {
				reader = new InputStreamReader(ProjectRemovedGenerator.class
						.getClassLoader().getResourceAsStream(
								pageNotFoundTemplate), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				reader = new InputStreamReader(ProjectRemovedGenerator.class
						.getClassLoader().getResourceAsStream(
								pageNotFoundTemplate));
			}
			context.put("loginURL", request.getContextPath() + "/");

			Map<String, String> defaultUrls = new HashMap<String, String>();

			defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());
			context.put("defaultUrls", defaultUrls);

			StringWriter writer = new StringWriter();
			TemplateEngine.evaluate(context, writer, "log task", reader);

			String html = writer.toString();
			PrintWriter out = response.getWriter();
			out.println(html);
		}
	}

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		try {
			if (pathInfo != null) {
				if (pathInfo.startsWith("/")) {
					pathInfo = pathInfo.substring(1);

					// email, projectId, sAccountId , inviterName, inviterEmail
					String pathVariables = UrlEncodeDecoder.decode(pathInfo);

					String email = pathVariables.substring(0,
							pathVariables.indexOf("/"));
					pathVariables = pathVariables.substring(email.length() + 1);

					int projectId = Integer.parseInt(pathVariables.substring(0,
							pathVariables.indexOf("/")));
					pathVariables = pathVariables.substring((projectId + "")
							.length() + 1);

					int sAccountId = Integer.parseInt(pathVariables.substring(
							0, pathVariables.indexOf("/")));
					pathVariables = pathVariables.substring((sAccountId + "")
							.length() + 1);

					String inviterName = pathVariables.substring(0,
							pathVariables.indexOf("/"));
					pathVariables = pathVariables.substring(inviterName
							.length() + 1);

					String inviterEmail = pathVariables.substring(0,
							pathVariables.indexOf("/"));
					pathVariables = pathVariables.substring(inviterName
							.length() + 1);

					Integer projectRoleId = Integer.parseInt(pathVariables);

					String subdomain = projectService
							.getSubdomainOfProject(projectId);
					SimpleProject project = projectService.findById(projectId,
							sAccountId);
					if (project == null) {
						ProjectRemovedGenerator.responePageProjectHasRemoved(
								request, response);
						return;
					}

					ProjectMember projectMember = projectMemberService
							.findMemberByUsername(email, projectId, sAccountId);
					if (projectMember != null) {
						ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
								projectId);
						String html = generateRefuseMemberDenyActionPage(linkGenerator
								.generateProjectFullLink());
						PrintWriter out = response.getWriter();
						out.println(html);
						return;
					} else {
						String redirectURL = SiteConfiguration
								.getSiteUrl(subdomain)
								+ "project/member/feedback/";

						String html = FeedBackPageGenerator
								.generateDenyFeedbacktoInviter(sAccountId,
										projectId, inviterEmail, inviterName,
										redirectURL, email, "You",
										project.getName(),
										DENY_FEEDBACK_TEMPLATE, projectRoleId);
						PrintWriter out = response.getWriter();
						out.println(html);
						return;
					}
				}
			}
			throw new ResourceNotFoundException();
		} catch (IndexOutOfBoundsException e) {
			throw new ResourceNotFoundException();
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException();
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException();
		} catch (Exception e) {
			log.error("Error with projectService", e);
			throw new MyCollabException(e);
		}
	}
}
