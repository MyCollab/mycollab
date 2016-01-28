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

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.servlet.VelocityWebServletRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(name = "denyMemberInvitationServlet", urlPatterns = "/project/member/invitation/deny_invite/*")
public class DenyProjectInvitationHandler extends VelocityWebServletRequestHandler {

    static String DENY_FEEDBACK_TEMPLATE = "templates/page/project/MemberDenyInvitationPage.mt";
    static String REFUSE_MEMBER_DENY_TEMPLATE = "templates/page/project/RefuseMemberDenyActionPage.mt";
    static String PROJECT_NOT_AVAILABLE_TEMPLATE = "templates/page/project/ProjectNotAvaiablePage.mt";

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectService projectService;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);

            String inviteeEmail = urlTokenizer.getString();
            int sAccountId = urlTokenizer.getInt();
            int projectId = urlTokenizer.getInt();

            String inviterName = urlTokenizer.getString();
            String inviterEmail = urlTokenizer.getString();

            String subdomain = projectService.getSubdomainOfProject(projectId);
            SimpleProject project = projectService.findById(projectId, sAccountId);
            if (project == null) {
                Map<String, Object> context = new HashMap<>();
                context.put("loginURL", request.getContextPath() + "/");
                String html = generatePageByTemplate(response.getLocale(), PROJECT_NOT_AVAILABLE_TEMPLATE, context);
                PrintWriter out = response.getWriter();
                out.println(html);
                return;
            }

            ProjectMember projectMember = projectMemberService.findMemberByUsername(inviteeEmail, projectId, sAccountId);

            if (projectMember != null) {
                Map<String, Object> context = new HashMap<>();
                context.put("projectLinkURL", ProjectLinkGenerator.generateProjectFullLink(SiteConfiguration.getSiteUrl(subdomain), projectId));

                String html = generatePageByTemplate(response.getLocale(), REFUSE_MEMBER_DENY_TEMPLATE, context);
                PrintWriter out = response.getWriter();
                out.println(html);
                return;
            } else {
                String redirectURL = SiteConfiguration.getSiteUrl(subdomain) + "project/member/feedback/";
                Map<String, Object> context = new HashMap<>();
                context.put("inviterEmail", inviterEmail);
                context.put("redirectURL", redirectURL);
                context.put("toEmail", inviterEmail);
                context.put("toName", "You");
                context.put("inviterName", inviterName);
                context.put("projectName", project.getName());
                context.put("sAccountId", sAccountId);
                context.put("projectId", projectId);

                String html = generatePageByTemplate(response.getLocale(), DENY_FEEDBACK_TEMPLATE, context);

                PrintWriter out = response.getWriter();
                out.println(html);
                return;
            }

        } else {
            throw new ResourceNotFoundException();
        }
    }
}
