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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.MailRelayService;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.esb.InviteProjectMembersCommand;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;

@Component
public class InviteProjectMembersCommandImpl implements
		InviteProjectMembersCommand {

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

		SimpleProject project = projectService.findById(projectId, sAccountId);

		SimpleUser user = userService.findUserByUserNameInAccount(
				inviterUserName, sAccountId);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				"$inviteUser has invited you to join the team for project \" $member.projectName\"",
				MailUtils
						.templatePath(
								"templates/email/project/memberInvitation/memberInvitationNotifier.mt",
								SiteConfiguration.getDefaultLocale()));

		SimpleProjectMember member = new SimpleProjectMember();
		member.setProjectName(project.getName());

		templateGenerator.putVariable("member", member);
		templateGenerator.putVariable("inviteUser", user.getDisplayName());

		String subdomain = projectService.getSubdomainOfProject(projectId);

		Date date = new Date();

		for (String inviteeEmail : emails) {
			templateGenerator.putVariable(
					"urlAccept",
					SiteConfiguration.getSiteUrl(subdomain)
							+ "project/member/invitation/confirm_invite/"
							+ ProjectLinkGenerator
									.generateAcceptInvitationParams(
											inviteeEmail, sAccountId,
											projectId, projectRoleId,
											user.getEmail(), inviterUserName,
											date));

			templateGenerator.putVariable(
					"urlDeny",
					SiteConfiguration.getSiteUrl(subdomain)
							+ "project/member/invitation/deny_invite/"
							+ ProjectLinkGenerator
									.generateDenyInvitationParams(inviteeEmail,
											sAccountId, projectId,
											user.getEmail(), inviterUserName));

			templateGenerator.putVariable("userName", "You");

			mailRelayService.saveRelayEmail(new String[] { inviteeEmail },
					new String[] { inviteeEmail },
					templateGenerator.generateSubjectContent(),
					templateGenerator.generateBodyContent());
		}

	}
}
