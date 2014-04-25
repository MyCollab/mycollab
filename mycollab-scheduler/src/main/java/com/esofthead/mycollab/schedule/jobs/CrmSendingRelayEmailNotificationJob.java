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
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.schedule.email.crm.impl.CrmDefaultSendingRelayEmailAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CrmSendingRelayEmailNotificationJob extends QuartzJobBean {
	private static Logger log = LoggerFactory
			.getLogger(CrmSendingRelayEmailNotificationJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context) {
		RelayEmailNotificationService relayEmailService = (RelayEmailNotificationService) ApplicationContextUtil
				.getSpringBean(RelayEmailNotificationService.class);

		RelayEmailNotificationSearchCriteria criteria = new RelayEmailNotificationSearchCriteria();
		criteria.setTypes(new SetSearchField<String>(new String[] {
				CrmTypeConstants.ACCOUNT, CrmTypeConstants.CONTACT,
				CrmTypeConstants.CAMPAIGN, CrmTypeConstants.LEAD,
				CrmTypeConstants.OPPORTUNITY, CrmTypeConstants.CASE,
				CrmTypeConstants.TASK, CrmTypeConstants.MEETING,
				CrmTypeConstants.CALL }));

		List<SimpleRelayEmailNotification> relayEmaiNotifications = relayEmailService
				.findPagableListByCriteria(new SearchRequest<RelayEmailNotificationSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));

		CrmDefaultSendingRelayEmailAction emailNotificationAction = null;

		for (SimpleRelayEmailNotification notification : relayEmaiNotifications) {
			try {
				if (notification.getEmailhandlerbean() != null) {
					emailNotificationAction = (CrmDefaultSendingRelayEmailAction) ApplicationContextUtil
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

							log.debug("Finish process notification {}",
									BeanUtility.printBeanObj(notification));
							relayEmailService.removeWithSession(
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
