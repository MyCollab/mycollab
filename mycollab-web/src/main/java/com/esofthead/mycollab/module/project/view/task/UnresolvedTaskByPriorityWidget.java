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

import java.util.List;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.ProjectDataTypeFactory;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class UnresolvedTaskByPriorityWidget extends Depot {
	private static final long serialVersionUID = 1L;

	private TaskSearchCriteria searchCriteria;

	public UnresolvedTaskByPriorityWidget() {
		super(AppContext
				.getMessage(TaskI18nEnum.UNRESOLVED_BY_PRIORITY_WIDGET_TITLE),
				new VerticalLayout());
		this.setContentBorder(true);
		((VerticalLayout) this.bodyContent).setSpacing(true);
		((VerticalLayout) this.bodyContent).setMargin(true);
	}

	public void setSearchCriteria(final TaskSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
		this.bodyContent.removeAllComponents();
		final ProjectTaskService taskService = ApplicationContextUtil
				.getSpringBean(ProjectTaskService.class);
		final int totalCount = taskService.getTotalCount(searchCriteria);
		final List<GroupItem> groupItems = taskService
				.getPrioritySummary(searchCriteria);
		final TaskPriorityClickListener listener = new TaskPriorityClickListener();

		if (!groupItems.isEmpty()) {
			for (final String priority : ProjectDataTypeFactory
					.getTaskPriorityList()) {
				boolean isFound = false;
				for (final GroupItem item : groupItems) {
					if (priority.equals(item.getGroupid())) {
						isFound = true;
						final HorizontalLayout priorityLayout = new HorizontalLayout();
						priorityLayout.setSpacing(true);
						priorityLayout.setWidth("100%");
						final Button userLbl = new Button(priority, listener);
						final Resource iconPriority = new ExternalResource(
								ProjectResources
										.getIconResourceLink12ByTaskPriority(priority));
						userLbl.setIcon(iconPriority);
						userLbl.setWidth("110px");
						userLbl.setStyleName("link");

						priorityLayout.addComponent(userLbl);
						final ProgressBarIndicator indicator = new ProgressBarIndicator(
								totalCount, totalCount - item.getValue(), false);
						indicator.setWidth("100%");
						priorityLayout.addComponent(indicator);
						priorityLayout.setExpandRatio(indicator, 1.0f);

						this.bodyContent.addComponent(priorityLayout);
						continue;
					}
				}

				if (!isFound) {
					final HorizontalLayout priorityLayout = new HorizontalLayout();
					priorityLayout.setSpacing(true);
					priorityLayout.setWidth("100%");
					final Button userLbl = new Button(priority, listener);
					final Resource iconPriority = new ExternalResource(
							ProjectResources
									.getIconResourceLink12ByTaskPriority(priority));
					userLbl.setIcon(iconPriority);
					userLbl.setWidth("110px");
					userLbl.setStyleName("link");
					priorityLayout.addComponent(userLbl);
					final ProgressBarIndicator indicator = new ProgressBarIndicator(
							totalCount, totalCount, false);
					indicator.setWidth("100%");
					priorityLayout.addComponent(indicator);
					priorityLayout.setExpandRatio(indicator, 1.0f);

					this.bodyContent.addComponent(priorityLayout);
				}
			}

		}
	}

	private class TaskPriorityClickListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(final ClickEvent event) {
			final String caption = event.getButton().getCaption();
			searchCriteria.setPriorities(new SetSearchField<String>(
					new String[] { caption }));
			TaskFilterParameter filterParam = new TaskFilterParameter(
					searchCriteria, "Task Filter by Priority: " + caption);
			EventBus.getInstance().fireEvent(
					new TaskEvent.Filter(this, filterParam));

		}
	}
}
