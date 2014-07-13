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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskDisplayWidget extends
		BeanList<ProjectTaskService, TaskSearchCriteria, SimpleTask> {
	private static final long serialVersionUID = 1L;

	public TaskDisplayWidget() {
		super(null, ApplicationContextUtil
				.getSpringBean(ProjectTaskService.class),
				TaskRowDisplayHandler.class);
	}

	public static class TaskRowDisplayHandler implements
			BeanList.RowDisplayHandler<SimpleTask> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleTask task, int rowIndex) {
			HorizontalLayout layout = new HorizontalLayout();
			Button taskLink = new Button("Task #" + task.getTaskkey() + ": ",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							EventBusFactory.getInstance().post(
									new TaskEvent.GotoRead(this, task.getId()));
						}
					});
			taskLink.setStyleName("link");
			String taskStatus = task.getStatus();
			if ("Closed".equalsIgnoreCase(taskStatus)) {
				taskLink.addStyleName(UIConstants.LINK_COMPLETED);
			} else if ("Pending".equalsIgnoreCase(taskStatus)) {
				taskLink.addStyleName(UIConstants.LINK_PENDING);
			} else if (task.isOverdue()) {
				taskLink.addStyleName(UIConstants.LINK_OVERDUE);
			}
			layout.addComponent(taskLink);
			Label taskName = new Label(task.getTaskname());
			layout.addComponent(taskName);
			layout.setExpandRatio(taskName, 1);
			layout.setSpacing(true);
			layout.setComponentAlignment(taskName, Alignment.TOP_LEFT);
			layout.setDescription("Task Information");
			return layout;
		}
	}
}
