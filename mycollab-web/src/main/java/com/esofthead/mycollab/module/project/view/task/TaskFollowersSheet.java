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
package com.esofthead.mycollab.module.project.view.task;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.domain.RelayEmailNotification;
import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.ui.components.CompFollowersSheet;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskFollowersSheet extends CompFollowersSheet<SimpleTask> {
	private static final long serialVersionUID = 1L;

	protected TaskFollowersSheet(SimpleTask task) {
		super(task);
	}

	@Override
	protected void loadMonitorItems() {
		MonitorSearchCriteria searchCriteria = new MonitorSearchCriteria();
		searchCriteria.setTypeId(new NumberSearchField(bean.getId()));
		searchCriteria
				.setType(new StringSearchField(ProjectTypeConstants.TASK));
		tableItem.setSearchCriteria(searchCriteria);
	}

	@Override
	protected boolean saveMonitorItem(String username) {
		if (!monitorItemService.isUserWatchingItem(username,
				ProjectTypeConstants.TASK, bean.getId())) {

			MonitorItem monitorItem = new MonitorItem();
			monitorItem.setMonitorDate(new GregorianCalendar().getTime());
			monitorItem.setType(ProjectTypeConstants.TASK);
			monitorItem.setTypeid(bean.getId());
			monitorItem.setUser(username);
			monitorItem.setSaccountid(AppContext.getAccountId());
			monitorItemService.saveWithSession(monitorItem,
					AppContext.getUsername());
			return true;

		}
		return false;
	}

	@Override
	protected void saveRelayNotification() {
		RelayEmailNotification relayNotification = new RelayEmailNotification();
		relayNotification.setChangeby(AppContext.getUsername());
		relayNotification.setChangecomment("");
		relayNotification.setSaccountid(AppContext.getAccountId());
		relayNotification.setType(ProjectTypeConstants.TASK);
		relayNotification.setTypeid(bean.getId());
		relayNotification
				.setEmailhandlerbean(ProjectTaskRelayEmailNotificationAction.class
						.getName());
		relayNotification.setAction(MonitorTypeConstants.CREATE_ACTION);

		RelayEmailNotificationService relayEmailNotificationService = ApplicationContextUtil
				.getSpringBean(RelayEmailNotificationService.class);
		relayEmailNotificationService.saveWithSession(relayNotification,
				AppContext.getUsername());
	}

	@Override
	protected boolean isEnableAdd() {
		return CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS);
	}

}