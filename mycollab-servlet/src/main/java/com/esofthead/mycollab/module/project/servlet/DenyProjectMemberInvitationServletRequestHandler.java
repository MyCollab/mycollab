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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.servlet.VelocityWebServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("denyMemberInvitationServlet")
public class DenyProjectMemberInvitationServletRequestHandler extends
		VelocityWebServletRequestHandler {

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

	@Override
	protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		try {
			if (pathInfo != null) {
				UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);

				String email = urlTokenizer.getString();
				int projectId = urlTokenizer.getInt();
				int sAccountId = urlTokenizer.getInt();
				String inviterName = urlTokenizer.getString();
				String inviterEmail = urlTokenizer.getString();
				Integer projectRoleId = urlTokenizer.getInt();

				String subdomain = projectService
						.getSubdomainOfProject(projectId);
				SimpleProject project = projectService.findById(projectId,
						sAccountId);
				if (project == null) {
					Map<String, Object> context = new HashMap<String, Object>();
					context.put("loginURL", request.getContextPath() + "/");

					String html = generatePageByTemplate(
							"templates/page/project/ProjectNotAvaiablePage.mt",
							context);
					PrintWriter out = response.getWriter();
					out.println(html);
					return;
				}

				ProjectMember projectMember = projectMemberService
						.findMemberByUsername(email, projectId, sAccountId);
				if (projectMember != null) {
					Map<String, Object> context = new HashMap<String, Object>();
					context.put("projectLinkURL", ProjectLinkUtils
							.generateProjectFullLink(
									SiteConfiguration.getSiteUrl(subdomain),
									projectId));

					String html = generatePageByTemplate(
							REFUSE_MEMBER_DENY_TEMPLATE, context);
					PrintWriter out = response.getWriter();
					out.println(html);
					return;
				} else {
					String redirectURL = SiteConfiguration
							.getSiteUrl(subdomain) + "project/member/feedback/";
					Map<String, Object> context = new HashMap<String, Object>();
					context.put("inviterEmail", inviterEmail);
					context.put("redirectURL", redirectURL);
					context.put("toEmail", email);
					context.put("toName", "You");
					context.put("inviterName", inviterName);
					context.put("projectName", project.getName());
					context.put("sAccountId", sAccountId);
					context.put("projectId", projectId);
					context.put("projectRoleId", projectRoleId);

					String html = generatePageByTemplate(
							DENY_FEEDBACK_TEMPLATE, context);

					PrintWriter out = response.getWriter();
					out.println(html);
					return;
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
