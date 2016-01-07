/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */
public class TaskListDisplay
		extends
		DefaultPagedBeanList<ProjectTaskService, TaskSearchCriteria, SimpleTask> {

	private static final long serialVersionUID = 1469872908434812706L;

	public TaskListDisplay() {
		super(ApplicationContextUtil.getSpringBean(ProjectTaskService.class),
				new TaskRowDisplayHandler());
		this.addStyleName("task-list");
	}

	private static class TaskRowDisplayHandler implements
			RowDisplayHandler<SimpleTask> {

		@Override
		public Component generateRow(final SimpleTask task, int rowIndex) {
			VerticalLayout layout = new VerticalLayout();
			layout.setWidth("100%");
			layout.addStyleName("task-layout");
			layout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

				private static final long serialVersionUID = -8379115635911957713L;

				@Override
				public void layoutClick(LayoutEvents.LayoutClickEvent event) {
					EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, task.getId()));
				}
			});

			HorizontalLayout topRow = new HorizontalLayout();
			topRow.setWidth("100%");
			Label b = new Label(CurrentProjectVariables.getProject().getShortname() + "-" + task.getTaskkey());
			b.setWidth("100%");
			b.setStyleName("task-key");
			topRow.addComponent(b);
			topRow.setExpandRatio(b, 1.0f);

			if (!task.getPriority().equals(AppContext.getMessage(TaskPriority.None))) {
				Label priorityLbl = new Label(task.getPriority());
				if (task.getPriority().equals(AppContext.getMessage(TaskPriority.High))) {
					priorityLbl.setStyleName(UIConstants.LBL_HIGH);
				} else if (task.getPriority().equals(AppContext.getMessage(TaskPriority.Urgent))) {
					priorityLbl.setStyleName(UIConstants.LBL_URGENT);
				} else if (task.getPriority().equals(AppContext.getMessage(TaskPriority.Medium))) {
					priorityLbl.setStyleName(UIConstants.LBL_MEDIUM);
				} else if (task.getPriority().equals(AppContext.getMessage(TaskPriority.Low))) {
					priorityLbl.setStyleName(UIConstants.LBL_LOW);
				}
				priorityLbl.setWidthUndefined();
				topRow.addComponent(priorityLbl);
			}
			layout.addComponent(topRow);

			Label taskName = new Label(task.getTaskname());
			taskName.setWidth("100%");
			taskName.setStyleName("task-name");
			layout.addComponent(taskName);

			return layout;
		}

	}

}
