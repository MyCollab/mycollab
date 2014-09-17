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
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.TaskEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
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
	}

	private static class TaskRowDisplayHandler implements
			RowDisplayHandler<SimpleTask> {

		@Override
		public Component generateRow(final SimpleTask task, int rowIndex) {
			VerticalLayout layout = new VerticalLayout();
			layout.setWidth("100%");
			layout.setStyleName("list-item");
			layout.addStyleName("task-layout");

			HorizontalLayout topRow = new HorizontalLayout();
			topRow.setWidth("100%");
			Button b = new Button(CurrentProjectVariables.getProject()
					.getShortname() + "-" + task.getTaskkey());
			b.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 5483571157334944410L;

				@Override
				public void buttonClick(Button.ClickEvent event) {
					EventBusFactory.getInstance().post(
							new TaskEvent.GotoRead(this, task.getId()));
				}
			});
			b.setWidth("100%");
			topRow.addComponent(b);
			topRow.setExpandRatio(b, 1.0f);

			if (!task.getPriority().equals(TaskPriority.None.name())) {
				Label priorityLbl = new Label(task.getPriority());
				if (task.getPriority().equals(TaskPriority.High.name())) {
					priorityLbl.setStyleName(UIConstants.LBL_HIGH);
				} else if (task.getPriority()
						.equals(TaskPriority.Urgent.name())) {
					priorityLbl.setStyleName(UIConstants.LBL_URGENT);
				} else if (task.getPriority()
						.equals(TaskPriority.Medium.name())) {
					priorityLbl.setStyleName(UIConstants.LBL_MEDIUM);
				} else if (task.getPriority().equals(TaskPriority.Low.name())) {
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
