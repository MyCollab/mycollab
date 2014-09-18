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
import java.util.List;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTaskCount;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
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
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskStatusComponent extends Depot {
	private static final long serialVersionUID = 1L;

	private TreeTable taskTree;

	private HierarchicalContainer dataContainer;

	public TaskStatusComponent() {
		super(AppContext.getMessage(ProjectCommonI18nEnum.TASKS_TITLE),
				new VerticalLayout());

		this.addStyleName("activity-panel");
		((VerticalLayout) this.bodyContent).setMargin(false);
	}

	public void showProjectTasksByStatus() {
		this.bodyContent.removeAllComponents();
		taskTree = new TreeTable();
		dataContainer = new HierarchicalContainer();
		dataContainer.addContainerProperty(
				AppContext.getMessage(TaskI18nEnum.FORM_TASK_NAME),
				String.class, "");
		dataContainer.addContainerProperty(
				AppContext.getMessage(TaskI18nEnum.FORM_DEADLINE),
				String.class, "");

		taskTree.setContainerDataSource(dataContainer);

		this.bodyContent.addComponent(taskTree);
		taskTree.setSizeFull();

		taskTree.addExpandListener(new Tree.ExpandListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void nodeExpand(ExpandEvent event) {
				Object itemId = event.getItemId();
				Hierarchical containerDataSource = taskTree
						.getContainerDataSource();

				if ((itemId instanceof ProjectGenericTaskCount)
						&& (containerDataSource.getChildren(itemId) == null)) {

					ProjectGenericTaskCount taskCount = (ProjectGenericTaskCount) itemId;

					ProjectGenericTaskSearchCriteria searchCriteria = new ProjectGenericTaskSearchCriteria();
					searchCriteria.setAssignUser(new StringSearchField(
							AppContext.getUsername()));
					searchCriteria.setIsOpenned(new SearchField());
					searchCriteria.setProjectId(new NumberSearchField(taskCount
							.getProjectId()));
					ProjectGenericTaskService prjGenericTaskService = ApplicationContextUtil
							.getSpringBean(ProjectGenericTaskService.class);
					List<ProjectGenericTask> genericTasks = prjGenericTaskService
							.findPagableListByCriteria(new SearchRequest<ProjectGenericTaskSearchCriteria>(
									searchCriteria, 0, Integer.MAX_VALUE));

					for (ProjectGenericTask task : genericTasks) {
						dataContainer.addItem(task);
						dataContainer.setParent(task, taskCount);
						dataContainer.setChildrenAllowed(task, false);

					}
				}
			}
		});

		ProjectGenericTaskSearchCriteria searchCriteria = new ProjectGenericTaskSearchCriteria();
		searchCriteria.setIsOpenned(new SearchField());
		searchCriteria.setAssignUser(new StringSearchField(SearchField.AND,
				AppContext.getUsername()));

		ProjectGenericTaskService prjGenericTaskService = ApplicationContextUtil
				.getSpringBean(ProjectGenericTaskService.class);

		List<ProjectGenericTaskCount> taskCountList = prjGenericTaskService
				.findPagableTaskCountListByCriteria(new SearchRequest<ProjectGenericTaskSearchCriteria>(
						searchCriteria, 0, Integer.MAX_VALUE));

		for (ProjectGenericTaskCount taskCount : taskCountList) {
			taskTree.addItem(new Object[] { taskCount.getProjectName(), "" },
					taskCount);
		}

		taskTree.addGeneratedColumn(
				AppContext.getMessage(TaskI18nEnum.FORM_TASK_NAME),
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						if (itemId instanceof ProjectGenericTaskCount) {
							final ProjectGenericTaskCount taskCount = (ProjectGenericTaskCount) itemId;
							ButtonLink projectLink = new ButtonLink(taskCount
									.getProjectName()
									+ " - "
									+ taskCount.getTaskCount() + " open tasks",
									new Button.ClickListener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void buttonClick(ClickEvent event) {
											int projectId = taskCount
													.getProjectId();
											PageActionChain chain = new PageActionChain(
													new ProjectScreenData.Goto(
															projectId));
											EventBusFactory
													.getInstance()
													.post(new ProjectEvent.GotoMyProject(
															this, chain));

										}
									});
							projectLink.setIcon(MyCollabResource
									.newResource("icons/16/project/project.png"));

							return projectLink;
						} else {
							final ProjectGenericTask task = (ProjectGenericTask) itemId;
							ButtonLink taskLink = new ButtonLink(
									task.getName(), new Button.ClickListener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void buttonClick(ClickEvent event) {
											if (ProjectTypeConstants.BUG
													.equals(task.getType())) {
												int projectId = task
														.getProjectId();
												int bugId = task.getTypeId();
												PageActionChain chain = new PageActionChain(
														new ProjectScreenData.Goto(
																projectId),
														new BugScreenData.Read(
																bugId));
												EventBusFactory
														.getInstance()
														.post(new ProjectEvent.GotoMyProject(
																this, chain));
											} else if (ProjectTypeConstants.TASK
													.equals(task.getType())) {
												int projectId = task
														.getProjectId();
												int taskId = task.getTypeId();
												PageActionChain chain = new PageActionChain(
														new ProjectScreenData.Goto(
																projectId),
														new TaskScreenData.Read(
																taskId));
												EventBusFactory
														.getInstance()
														.post(new ProjectEvent.GotoMyProject(
																this, chain));
											} else if (ProjectTypeConstants.PROBLEM
													.equals(task.getType())) {
												int projectId = task
														.getProjectId();
												int problemId = task
														.getTypeId();
												PageActionChain chain = new PageActionChain(
														new ProjectScreenData.Goto(
																projectId),
														new ProblemScreenData.Read(
																problemId));
												EventBusFactory
														.getInstance()
														.post(new ProjectEvent.GotoMyProject(
																this, chain));
											} else if (ProjectTypeConstants.RISK
													.equals(task.getType())) {
												int projectId = task
														.getProjectId();
												int riskId = task.getTypeId();
												PageActionChain chain = new PageActionChain(
														new ProjectScreenData.Goto(
																projectId),
														new RiskScreenData.Read(
																riskId));
												EventBusFactory
														.getInstance()
														.post(new ProjectEvent.GotoMyProject(
																this, chain));
											}
										}
									});
							taskLink.setIcon(new ExternalResource(
									ProjectResources.getResourceLink(task
											.getType())));
							if (task.getDueDate() != null
									&& task.getDueDate().before(
											new GregorianCalendar().getTime())) {
								taskLink.addStyleName(UIConstants.LINK_OVERDUE);
							}
							return taskLink;
						}
					}
				});

		taskTree.addGeneratedColumn(
				AppContext.getMessage(TaskI18nEnum.FORM_DEADLINE),
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						if (itemId instanceof ProjectGenericTask) {
							return new Label(AppContext
									.formatDate(((ProjectGenericTask) itemId)
											.getDueDate()));
						} else {
							return new Label();
						}
					}
				});

		taskTree.setColumnWidth(
				AppContext.getMessage(TaskI18nEnum.FORM_DEADLINE),
				UIConstants.TABLE_DATE_WIDTH);
		taskTree.setColumnExpandRatio(
				AppContext.getMessage(TaskI18nEnum.FORM_TASK_NAME), 1.0f);
	}

}