/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.esb;

import org.apache.camel.ExchangePattern;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.spring.ApplicationContextUtil;

@Component
public class ProjectRouteBuilder extends SpringRouteBuilder {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProjectRouteBuilder.class);

	@Override
	public void configure() throws Exception {
		LOG.debug("Configure project remove route");
		from(ProjectEndPoints.PROJECT_REMOVE_ENDPOINT).setExchangePattern(
				ExchangePattern.InOnly).to("seda:projectDelete.queue");
		from("seda:projectDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectCommand.class),
						"projectRemoved(int, int)");

		LOG.debug("Configure project member remove route");
		from(ProjectEndPoints.PROJECT_MEMBER_DELETE_ENDPOINT)
				.setExchangePattern(ExchangePattern.InOnly).to(
						"seda:projectMemberDelete.queue");
		from("seda:projectMemberDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectMemberCommand.class),
						"projectMemberRemoved(String,int, int, int)");

		LOG.debug("Configure project message remove route");
		from(ProjectEndPoints.PROJECT_MESSAGE_REMOVE_ENDPOINT)
				.setExchangePattern(ExchangePattern.InOnly).to(
						"seda:projectMessageDelete.queue");
		from("seda:projectMessageDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectMessageCommand.class),
						"messageRemoved(String,int, int, int)");

		LOG.debug("Configure project bug remove route");
		from(ProjectEndPoints.PROJECT_BUG_REMOVE_ENDPOINT).setExchangePattern(
				ExchangePattern.InOnly).to("seda:projectBugDelete.queue");
		from("seda:projectBugDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectBugCommand.class),
						"bugRemoved(String,int, int, int)");

		LOG.debug("Configure project component remove route");
		from(ProjectEndPoints.PROJECT_COMPONENT_REMOVE_ENDPOINT)
				.setExchangePattern(ExchangePattern.InOnly).to(
						"seda:projectComponentDelete.queue");
		from("seda:projectComponentDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectComponentCommand.class),
						"componentRemoved(String,int, int, int)");

		LOG.debug("Configure project version remove route");
		from(ProjectEndPoints.PROJECT_VERSION_REMOVE_ENDPOINT)
				.setExchangePattern(ExchangePattern.InOnly).to(
						"seda:projectVersionDelete.queue");
		from("seda:projectVersionDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectVersionCommand.class),
						"versionRemoved(String,int, int, int)");

		LOG.debug("Configure project task remove route");
		from(ProjectEndPoints.PROJECT_TASK_REMOVE_ENDPOINT).setExchangePattern(
				ExchangePattern.InOnly).to("seda:projectTaskDelete.queue");
		from("seda:projectTaskDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectTaskCommand.class),
						"taskRemoved(String,int, int, int)");

		LOG.debug("Configure project task list remove route");
		from(ProjectEndPoints.PROJECT_TASKLIST_REMOVE_ENDPOINT)
				.setExchangePattern(ExchangePattern.InOnly).to(
						"seda:projectTaskListDelete.queue");
		from("seda:projectTaskListDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectTaskListCommand.class),
						"taskListRemoved(String,int, int, int)");

		LOG.debug("Configure project milestone remove route");
		from(ProjectEndPoints.PROJECT_MILESTONE_REMOVE_ENDPOINT)
				.setExchangePattern(ExchangePattern.InOnly).to(
						"seda:projectMilestoneDelete.queue");
		from("seda:projectMilestoneDelete.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(DeleteProjectMilestoneCommand.class),
						"milestoneRemoved(String,int, int, int)");

		LOG.debug("Configure project member invitation route");
		from(ProjectEndPoints.PROJECT_SEND_INVITATION_USER).setExchangePattern(
				ExchangePattern.InOnly)
				.to("seda:projectMemberInvitation.queue");
		from("seda:projectMemberInvitation.queue")
				.threads()
				.bean(ApplicationContextUtil
						.getSpringBean(InviteProjectMembersCommand.class),
						"inviteUsers(String[],int, int, String, int)");
	}

}
