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
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class UnresolvedTaskByAssigneeWidget extends Depot {
	private static final long serialVersionUID = 1L;

	private TaskSearchCriteria searchCriteria;

	public UnresolvedTaskByAssigneeWidget() {
		super(AppContext
				.getMessage(TaskI18nEnum.UNRESOLVED_BY_ASSIGNEE_WIDGET_TITLE),
				new VerticalLayout());
		this.setContentBorder(true);
		((VerticalLayout) this.bodyContent).setSpacing(true);
		((VerticalLayout) this.bodyContent).setMargin(true);
	}

	public void setSearchCriteria(final TaskSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
		this.bodyContent.removeAllComponents();
		ProjectTaskService projectTaskService = ApplicationContextUtil
				.getSpringBean(ProjectTaskService.class);
		int totalCountItems = projectTaskService.getTotalCount(searchCriteria);
		final List<GroupItem> groupItems = projectTaskService
				.getAssignedDefectsSummary(searchCriteria);

		if (!groupItems.isEmpty()) {
			for (final GroupItem item : groupItems) {
				final HorizontalLayout assigneeLayout = new HorizontalLayout();
				assigneeLayout.setSpacing(true);
				assigneeLayout.setWidth("100%");

				final String assignUser = item.getGroupid();
				String assignUserFullName = item.getGroupid() == null ? ""
						: item.getGroupname();

				if (assignUserFullName == null
						|| assignUserFullName.trim().equals("")) {
					String displayName = item.getGroupid();
					int index = displayName != null ? displayName.indexOf("@")
							: 0;
					if (index > 0) {
						assignUserFullName = displayName.substring(0, index);
					} else {
						assignUserFullName = AppContext
								.getMessage(TaskI18nEnum.UNDEFINED_USER);
					}
				}

				final TaskAssigneeLink userLbl = new TaskAssigneeLink(
						assignUser, item.getExtraValue(), assignUserFullName);
				assigneeLayout.addComponent(userLbl);
				final ProgressBarIndicator indicator = new ProgressBarIndicator(
						totalCountItems, totalCountItems - item.getValue(),
						false);
				indicator.setWidth("100%");
				assigneeLayout.addComponent(indicator);
				assigneeLayout.setExpandRatio(indicator, 1.0f);
				this.bodyContent.addComponent(assigneeLayout);
			}

		}
	}

	class TaskAssigneeLink extends Button {
		private static final long serialVersionUID = 1L;

		public TaskAssigneeLink(final String assignee,
				final String assigneeAvatarId, final String assigneeFullName) {
			super(assigneeFullName, new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					searchCriteria.setAssignUser(new StringSearchField(
							SearchField.AND, assignee));
					TaskFilterParameter filterParam = new TaskFilterParameter(
							searchCriteria, AppContext.getMessage(
									TaskI18nEnum.FILTER_TASK_BY_ASSIGNEE,
									assigneeFullName));
					EventBus.getInstance().fireEvent(
							new TaskEvent.Filter(this, filterParam));
				}
			});

			this.setStyleName("link");
			this.setWidth("110px");
			this.addStyleName(UIConstants.TEXT_ELLIPSIS);
			this.setIcon(UserAvatarControlFactory.createAvatarResource(
					assigneeAvatarId, 16));
		}
	}
}
