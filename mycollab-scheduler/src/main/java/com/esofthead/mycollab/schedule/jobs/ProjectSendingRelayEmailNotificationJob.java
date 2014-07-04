/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.jobs;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectSendingRelayEmailNotificationJob extends
		GenericQuartzJobBean {
	private static Logger log = LoggerFactory
			.getLogger(ProjectSendingRelayEmailNotificationJob.class);

	@Override
	protected void executeJob(JobExecutionContext context) {
		ProjectService projectService = (ProjectService) ApplicationContextUtil
				.getSpringBean(ProjectService.class);
		List<ProjectRelayEmailNotification> relayEmaiNotifications = projectService
				.findProjectRelayEmailNotifications();

		RelayEmailNotificationService relayNotificationService = ApplicationContextUtil
				.getSpringBean(RelayEmailNotificationService.class);
		log.debug("Get " + relayEmaiNotifications.size()
				+ " relay email notifications");
		SendingRelayEmailNotificationAction emailNotificationAction = null;

		for (ProjectRelayEmailNotification notification : relayEmaiNotifications) {
			try {
				if (notification.getEmailhandlerbean() != null) {
					emailNotificationAction = (SendingRelayEmailNotificationAction) ApplicationContextUtil
							.getSpringBean(Class.forName(notification
									.getEmailhandlerbean()));

					if (emailNotificationAction != null) {
						try {
							if (MonitorTypeConstants.CREATE_ACTION
									.equals(notification.getAction())) {
								emailNotificationAction
										.sendNotificationForCreateAction(notification);
							} else if (MonitorTypeConstants.UPDATE_ACTION
									.equals(notification.getAction())) {
								emailNotificationAction
										.sendNotificationForUpdateAction(notification);
							} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
									.equals(notification.getAction())) {
								emailNotificationAction
										.sendNotificationForCommentAction(notification);
							}

							relayNotificationService.removeWithSession(
									notification.getId(), "",
									notification.getSaccountid());

						} catch (Exception e) {
							log.error("Error when sending notification email",
									e);
						}
					}
				}

			} catch (ClassNotFoundException ex) {
				throw new MyCollabException("no class found toget spring bean "
						+ ex.getMessage());
			}

		}
	}
}
