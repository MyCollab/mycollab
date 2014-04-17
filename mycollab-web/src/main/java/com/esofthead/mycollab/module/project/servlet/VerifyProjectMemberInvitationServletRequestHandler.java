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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.servlet.GenericServletRequestHandler;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.template.velocity.TemplateContext;
import com.esofthead.template.velocity.TemplateEngine;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component("acceptMemberInvitationServlet")
public class VerifyProjectMemberInvitationServletRequestHandler extends
		GenericServletRequestHandler {

	private static Logger log = LoggerFactory
			.getLogger(VerifyProjectMemberInvitationServletRequestHandler.class);

	private static String OUTSIDE_MEMBER_WELCOME_PAGE = "templates/page/project/OutsideMemberAcceptInvitationPage.mt";
	private static String EXPIER_PAGE = "templates/page/ExpirePage.mt";

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
				if (pathInfo.startsWith("/")) {
					UrlTokenizer urlTokenizer = new UrlTokenizer(pathInfo);
					String email = urlTokenizer.getString();
					int projectId = urlTokenizer.getInt();
					int sAccountId = urlTokenizer.getInt();

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
						String html = generateExpirePage(inviterName,
								inviterEmail);
						PrintWriter out = response.getWriter();
						out.println(html);
						return;
					}
					log.debug("Checking Member status --------");
					User user = userService.findUserByUserName(email);
					if (user != null) { // user exit
						log.debug("User exist on System -------------");
						handleMemberInviteWithExistAccount(email, projectId,
								sAccountId, projectRoleId, response);
					} else {
						log.debug("User not exist on System --------- to enter password'Page");
						handleOutSideMemberInvite(email, projectId, sAccountId,
								projectRoleId, inviterName, response, request);
					}
					return;
				} else {
					throw new ResourceNotFoundException();
				}
			} catch (ResourceNotFoundException e) {
				throw new ResourceNotFoundException();
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
		}
		throw new ResourceNotFoundException();
	}

	private String generateExpirePage(String inviterEmail, String inviterName) {
		TemplateContext context = new TemplateContext();
		Reader reader;
		try {
			reader = new InputStreamReader(
					VerifyProjectMemberInvitationServletRequestHandler.class
							.getClassLoader().getResourceAsStream(EXPIER_PAGE),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(
					VerifyProjectMemberInvitationServletRequestHandler.class
							.getClassLoader().getResourceAsStream(EXPIER_PAGE));
		}
		context.put("inviterEmail", inviterEmail);
		context.put("inviterName", inviterName);

		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());

		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		TemplateEngine.evaluate(context, writer, "log task", reader);
		return writer.toString();
	}

	private void handleMemberInviteWithExistAccount(String username,
			Integer projectId, Integer sAccountId, Integer projectRoleId,
			HttpServletResponse response) throws IOException {

		// search has in table User account
		UserAccountExample example = new UserAccountExample();
		example.createCriteria().andUsernameEqualTo(username)
				.andAccountidEqualTo(sAccountId);
		try {
			List<UserAccount> lst = userAccountMapper.selectByExample(example);
			if (lst.size() > 0) {
				for (UserAccount record : lst) {
					record.setRegisterstatus(RegisterStatusConstants.ACTIVE);
					userAccountMapper.updateByPrimaryKeySelective(record);
				}
			} else {
				UserAccount userAccount = new UserAccount();
				userAccount.setUsername(username);
				userAccount.setAccountid(sAccountId);
				userAccount.setRegisterstatus(RegisterStatusConstants.ACTIVE);
				userAccount.setIsaccountowner(false);
				userAccount.setRegisteredtime(new Date());

				userAccountMapper.insert(userAccount);
			}
			// search has in table projectMember
			SimpleProjectMember member = projectMemberService
					.findMemberByUsername(username, projectId, sAccountId);
			if (member == null) {
				ProjectMember projectMember = new ProjectMember();
				projectMember.setProjectid(projectId);
				projectMember.setUsername(username);
				projectMember.setJoindate(new Date());
				projectMember.setProjectroleid(projectRoleId);
				projectMember.setSaccountid(sAccountId);
				projectMember.setIsadmin(false);
				projectMember.setStatus(RegisterStatusConstants.ACTIVE);
				projectMemberService.saveWithSession(projectMember,
						AppContext.getUsername());
			} else if (member != null) {
				member.setStatus(RegisterStatusConstants.ACTIVE);
				member.setSaccountid(sAccountId);
				member.setProjectroleid(projectRoleId);
				projectMemberService.updateWithSession(member, "");
			}
			ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
					projectId);
			response.sendRedirect(linkGenerator.generateProjectFullLink());
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	private void handleOutSideMemberInvite(String email, Integer projectId,
			Integer sAccountId, Integer projectRoleId, String inviterName,
			HttpServletResponse response, HttpServletRequest request) {
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				projectId);
		String projectLinkURL = linkGenerator.generateProjectFullLink();

		String handelCreateAccountURL = request.getContextPath() + "/"
				+ "project/outside/createAccount/";

		String html = generateOutsideMemberAcceptPage(sAccountId, email,
				projectId, projectRoleId, projectLinkURL,
				handelCreateAccountURL, inviterName);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			throw new MyCollabException(e);
		}
		out.println(html);
	}

	private String generateOutsideMemberAcceptPage(int sAccountId,
			String email, int projectId, int roleId, String projectLinkURL,
			String handelCreateAccountURL, String inviterName) {
		TemplateContext context = new TemplateContext();

		Reader reader;
		try {
			reader = new InputStreamReader(
					VerifyProjectMemberInvitationServletRequestHandler.class
							.getClassLoader().getResourceAsStream(
									OUTSIDE_MEMBER_WELCOME_PAGE), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(
					VerifyProjectMemberInvitationServletRequestHandler.class
							.getClassLoader().getResourceAsStream(
									OUTSIDE_MEMBER_WELCOME_PAGE));
		}
		context.put("projectLinkURL", projectLinkURL);
		context.put("email", email);
		context.put("handelCreateAccountURL", handelCreateAccountURL);
		context.put("sAccountId", sAccountId);
		context.put("projectId", projectId);
		context.put("roleId", roleId);
		context.put("inviterName", inviterName);

		Map<String, String> defaultUrls = new HashMap<String, String>();

		defaultUrls.put("cdn_url", SiteConfiguration.getCdnUrl());

		context.put("defaultUrls", defaultUrls);

		StringWriter writer = new StringWriter();
		TemplateEngine.evaluate(context, writer, "log task", reader);
		return writer.toString();
	}
}
