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
package com.esofthead.mycollab.module.project.ui.components;

import java.util.List;

import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.ProjectContants;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProblemScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.RiskScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class GenericTaskTableDisplay
		extends
		DefaultPagedBeanTable<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> {
	private static final long serialVersionUID = 1L;

	public GenericTaskTableDisplay(List<TableViewField> displayColumns) {
		super(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class),
				ProjectGenericTask.class, displayColumns);

		addGeneratedColumn("name", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				HorizontalLayout layout = new HorizontalLayout();

				final ProjectGenericTask task = GenericTaskTableDisplay.this
						.getBeanByIndex(itemId);

				if (task.getType() != null) {
					Embedded icon = new Embedded(null, new ExternalResource(
							ProjectResources.getResourceLink(task.getType())));
					layout.addComponent(icon);
					layout.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
				}
				final ButtonLink b = new ButtonLink(task.getName(),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(ClickEvent event) {
								String taskType = task.getType();
								PageActionChain chain = null;

								if (ProjectContants.BUG.equals(taskType)) {
									chain = new PageActionChain(
											new ProjectScreenData.Goto(task
													.getProjectId()),
											new BugScreenData.Read(task
													.getTypeId()));

								} else if (ProjectContants.TASK
										.equals(taskType)) {
									chain = new PageActionChain(
											new ProjectScreenData.Goto(task
													.getProjectId()),
											new TaskScreenData.Read(task
													.getTypeId()));
								} else if (ProjectContants.PROBLEM
										.equals(taskType)) {
									chain = new PageActionChain(
											new ProjectScreenData.Goto(task
													.getProjectId()),
											new ProblemScreenData.Read(task
													.getTypeId()));
								} else if (ProjectContants.RISK
										.equals(taskType)) {
									chain = new PageActionChain(
											new ProjectScreenData.Goto(task
													.getProjectId()),
											new RiskScreenData.Read(task
													.getTypeId()));
								}

								if (chain != null) {
									EventBus.getInstance().fireEvent(
											new ProjectEvent.GotoMyProject(
													this, chain));
								}

							}
						});

				layout.addComponent(b);
				b.setWidth("100%");
				layout.addComponent(b);
				layout.setExpandRatio(b, 1.0f);
				layout.setWidth("100%");
				return layout;
			}
		});

		addGeneratedColumn("dueDate", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final ProjectGenericTask task = GenericTaskTableDisplay.this
						.getBeanByIndex(itemId);
				final Label b = new Label(AppContext.formatDate(task
						.getDueDate()));
				return b;
			}
		});
	}
}
