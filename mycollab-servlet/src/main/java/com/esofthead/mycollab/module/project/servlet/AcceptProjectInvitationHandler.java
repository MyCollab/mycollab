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
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esofthead.mycollab.module.project.domain.SimpleProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.servlet.VelocityWebServletRequestHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@WebServlet(name="acceptMemberInvitationServlet", urlPatterns = "project/member/invitation/confirm_invite/*")
public class AcceptProjectInvitationHandler extends VelocityWebServletRequestHandler {
    static final String OUTSIDE_MEMBER_WELCOME_PAGE =
            "templates/page/project/OutsideMemberAcceptInvitationPage.mt";
    static final String EXPIRE_PAGE = "templates/page/ExpirePage.mt";

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private ProjectService projectService;

    @Override
    protected void onHandleRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            try {
                UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);

                String inviteeEmail = urlTokenizer.getString();
                int sAccountId = urlTokenizer.getInt();
                int projectId = urlTokenizer.getInt();
                int projectRoleId = urlTokenizer.getInt();
                String inviterName = urlTokenizer.getString();
                String inviterEmail = urlTokenizer.getString();

                String timeStr = urlTokenizer.getString();
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                Date invitedDate = df.parse(timeStr);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);
                Date dateBefore7Days = cal.getTime();

                if (invitedDate.compareTo(dateBefore7Days) < 0) { // expire
                    // print out page expire
                    Map<String, Object> context = new HashMap<>();
                    context.put("inviterEmail", inviterEmail);
                    context.put("inviterName", inviterName);

                    String html = generatePageByTemplate(response.getLocale(), EXPIRE_PAGE, context);

                    PrintWriter out = response.getWriter();
                    out.println(html);
                    return;
                }

                String subDomain = projectService.getSubdomainOfProject(projectId);
                String siteUrl = SiteConfiguration.getSiteUrl(subDomain);

                User user = userService.findUserByUserName(inviteeEmail);
                if (user != null) { // there is already user in the current account
                    handleMemberInviteWithExistAccount(siteUrl, inviteeEmail,
							projectId, sAccountId, projectRoleId, response);
                } else {
                    handleOutSideMemberInvite(siteUrl, inviteeEmail, projectId,
                            sAccountId, projectRoleId, inviterName, response,
                            request);
                }
            } catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException();
            } catch (Exception e) {
                throw new MyCollabException(e);
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void handleMemberInviteWithExistAccount(String siteUrl,
            String username, Integer projectId, Integer sAccountId,
            Integer projectRoleId, HttpServletResponse response)
            throws IOException {

        // search has in table User account
        UserAccountExample example = new UserAccountExample();
        example.createCriteria().andUsernameEqualTo(username)
                .andAccountidEqualTo(sAccountId);

        Date now = new GregorianCalendar().getTime();
        try {
            List<UserAccount> users = userAccountMapper.selectByExample(example);
            if (users.size() > 0) {
                for (UserAccount record : users) {
                    record.setRegisterstatus(RegisterStatusConstants.ACTIVE);
                    userAccountMapper.updateByPrimaryKeySelective(record);
                }
            } else {
                UserAccount userAccount = new UserAccount();
                userAccount.setUsername(username);
                userAccount.setAccountid(sAccountId);
                userAccount.setRegisterstatus(RegisterStatusConstants.ACTIVE);
                userAccount.setIsaccountowner(false);
                userAccount.setRegisteredtime(now);
                userAccountMapper.insert(userAccount);
            }
            // search has in table projectMember
            SimpleProjectMember member = projectMemberService
                    .findMemberByUsername(username, projectId, sAccountId);
            if (member == null) {
                ProjectMember projectMember = new ProjectMember();
                projectMember.setProjectid(projectId);
                projectMember.setUsername(username);
                projectMember.setJoindate(now);
                projectMember.setProjectroleid(projectRoleId);
                projectMember.setSaccountid(sAccountId);
                projectMember.setIsadmin(false);
                projectMember.setStatus(RegisterStatusConstants.ACTIVE);
                projectMemberService.saveWithSession(projectMember, "");
            } else {
                member.setStatus(RegisterStatusConstants.ACTIVE);
                member.setSaccountid(sAccountId);
                member.setProjectroleid(projectRoleId);
                projectMemberService.updateWithSession(member, "");
            }
            String projectLink = ProjectLinkGenerator.generateProjectFullLink(siteUrl, projectId);
            response.sendRedirect(projectLink);
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private void handleOutSideMemberInvite(String siteUrl, String email,
            Integer projectId, Integer sAccountId, Integer projectRoleId,
            String inviterName, HttpServletResponse response,
            HttpServletRequest request) {
        String projectLinkURL = ProjectLinkGenerator.generateProjectFullLink(
                siteUrl, projectId);

        String handelCreateAccountURL = request.getContextPath() + "/"
                + "project/outside/createAccount/";
        Map<String, Object> context = new HashMap<>();
        context.put("projectLinkURL", projectLinkURL);
        context.put("email", email);
        context.put("handelCreateAccountURL", handelCreateAccountURL);
        context.put("sAccountId", sAccountId);
        context.put("projectId", projectId);
        context.put("roleId", projectRoleId);
        context.put("inviterName", inviterName);

		SimpleProject project = projectService.findById(projectId, sAccountId);
        context.put("projectName", project.getName());

        String html = generatePageByTemplate(response.getLocale(), OUTSIDE_MEMBER_WELCOME_PAGE, context);
        try {
            PrintWriter out = response.getWriter();
            out.println(html);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }
}
