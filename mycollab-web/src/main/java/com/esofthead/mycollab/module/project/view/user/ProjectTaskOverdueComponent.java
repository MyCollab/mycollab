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
package com.esofthead.mycollab.module.project.view.user;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class ProjectTaskOverdueComponent extends Depot {

	private static final long serialVersionUID = 1L;
	private final DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> taskList;

	public ProjectTaskOverdueComponent() {
		super(AppContext
				.getMessage(ProjectCommonI18nEnum.WIDGET_OVERDUE_TASKS_TITLE),
				new VerticalLayout());

		taskList = new DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask>(
				ApplicationContextUtil
						.getSpringBean(ProjectGenericTaskService.class),
				new TaskRowDisplayHandler(), 10);
		addStyleName("activity-panel");
		((VerticalLayout) bodyContent).setMargin(false);
	}

	public void showOverdueTasks() {
		bodyContent.removeAllComponents();
		bodyContent.addComponent(taskList);
		final ProjectGenericTaskSearchCriteria searchCriteria = new ProjectGenericTaskSearchCriteria();
		searchCriteria.setIsOpenned(new SearchField());
		searchCriteria.setDueDate(new DateSearchField(DateSearchField.AND,
				new GregorianCalendar().getTime()));
		searchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		taskList.setSearchCriteria(searchCriteria);
	}

	public static class TaskRowDisplayHandler implements
			DefaultBeanPagedList.RowDisplayHandler<ProjectGenericTask> {

		@Override
		public Component generateRow(final ProjectGenericTask genericTask,
				final int rowIndex) {
			final CssLayout layout = new CssLayout();
			layout.setWidth("100%");
			layout.setStyleName("activity-stream");

			if ((rowIndex + 1) % 2 != 0) {
				layout.addStyleName("odd");
			}

			final CssLayout header = new CssLayout();
			header.setStyleName("stream-content");

			final LabelLink taskLink = new LabelLink(genericTask.getName(),
					ProjectLinkBuilder.generateProjectItemLink(
							genericTask.getProjectId(), genericTask.getType(),
							genericTask.getTypeId()));
			taskLink.setIconLink(ProjectResources.getResourceLink(genericTask
					.getType()));
			taskLink.setStyleName("overdue");
			taskLink.addStyleName(UIConstants.THEME_LINK);

			header.addComponent(taskLink);

			layout.addComponent(header);

			final HorizontalLayout body = new HorizontalLayout();
			body.setStyleName("activity-date");
			body.setSpacing(true);

			final Label dateLbl = new Label("Due date: "
					+ DateTimeUtils.formatDate(genericTask.getDueDate()));
			body.addComponent(dateLbl);

			final Label assigneeLabel = new Label(
					"&nbsp;&nbsp;&nbsp;&nbsp;Assignee: ", ContentMode.HTML);
			final LabelLink assignee = new LabelLink(
					genericTask.getAssignUserFullName(),
					ProjectLinkBuilder.generateProjectMemberFullLink(
							genericTask.getProjectId(),
							genericTask.getAssignUser()));
			if (genericTask.getAssignUser() != null) {
				assignee.setIconLink(SiteConfiguration.getAvatarLink(
						genericTask.getAssignUserAvatarId(), 16));
				body.addComponent(assigneeLabel);
			}
			body.addComponent(assignee);

			layout.addComponent(body);

			return layout;
		}
	}

}
