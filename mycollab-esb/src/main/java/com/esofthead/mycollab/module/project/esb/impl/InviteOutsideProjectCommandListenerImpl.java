/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.esb.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.MailRelayService;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.esb.InviteOutsideProjectMemberCommand;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;

@Component
public class InviteOutsideProjectCommandListenerImpl implements
		InviteOutsideProjectMemberCommand {

	private static Logger log = LoggerFactory
			.getLogger(InviteOutsideProjectCommandListenerImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private MailRelayService mailRelayService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectMemberService projectMemberService;

	@Override
	public void inviteUsers(String[] emails, int projectId, int projectRoleId,
			String inviterUserName, String inviteMessage, int sAccountId) {

		log.debug(
				"Request sending invitation email to user {} in project id {} with role id {} and account id {} by user {}",
				new String[] { BeanUtility.printBeanObj(emails),
						projectId + "", projectRoleId + "", sAccountId + "",
						inviterUserName });

		SimpleProject project = projectService.findById(projectId, sAccountId);

		SimpleUser user = userService.findUserByUserNameInAccount(
				inviterUserName, sAccountId);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				"$inviteUser has invited you to join the team for project \" $member.projectName\"",
				"templates/email/project/memberInvitation/memberInvitationNotifier.mt");

		SimpleProjectMember member = new SimpleProjectMember();
		member.setProjectName(project.getName());

		templateGenerator.putVariable("member", member);
		templateGenerator.putVariable("inviteUser", user.getDisplayName());

		String subdomain = projectService.getSubdomainOfProject(projectId);

		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();

		for (String email : emails) {
			templateGenerator.putVariable(
					"urlAccept",
					SiteConfiguration.getSiteUrl(subdomain)
							+ "project/member/invitation/confirm_invite/"
							+ UrlEncodeDecoder.encode(email + "/" + projectId
									+ "/" + sAccountId + "/" + projectRoleId
									+ "/" + inviterUserName + "/"
									+ user.getEmail() + "/"
									+ dateFormat.format(date)));

			templateGenerator.putVariable(
					"urlDeny",
					SiteConfiguration.getSiteUrl(subdomain)
							+ "project/member/invitation/deny_invite/"
							+ UrlEncodeDecoder.encode(email + "/" + projectId
									+ "/" + sAccountId + "/" + inviterUserName
									+ "/" + user.getEmail() + "/"
									+ projectRoleId));

			templateGenerator.putVariable("userName", "You");

			mailRelayService.saveRelayEmail(new String[] { email },
					new String[] { email },
					templateGenerator.generateSubjectContent(),
					templateGenerator.generateBodyContent());
		}

	}
}
