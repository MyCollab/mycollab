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

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskGroupDisplayWidget
		extends
		BeanList<ProjectTaskListService, TaskListSearchCriteria, SimpleTaskList> {
	private static final long serialVersionUID = 1L;

	public TaskGroupDisplayWidget() {
		super(null, ApplicationContextUtil
				.getSpringBean(ProjectTaskListService.class),
				TaskListRowDisplayHandler.class);
		this.setDisplayEmptyListText(false);
	}

	public static class TaskListRowDisplayHandler extends
			BeanList.RowDisplayHandler<SimpleTaskList> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleTaskList taskList,
				final int rowIndex) {
			return new TaskListDepot(taskList);
		}
	}

	static class TaskListDepot extends Depot {
		private static final long serialVersionUID = 1L;
		private final SimpleTaskList taskList;
		private SplitButton taskListFilterControl;
		private PopupButton taskListActionControl;
		private TaskDisplayComponent taskDisplayComponent;

		public TaskListDepot(final SimpleTaskList taskListParam) {
			super(taskListParam.getName(), null, new TaskDisplayComponent(
					taskListParam, true));
			if ("Closed".equals(taskListParam.getStatus())) {
				this.headerLbl.addStyleName(UIConstants.LINK_COMPLETED);
			}
			this.taskList = taskListParam;
			this.addStyleName("task-list");
			this.initHeader();
			this.setHeaderColor(true);
			this.taskDisplayComponent = (TaskDisplayComponent) this.bodyContent;
		}

		private void initHeader() {
			final HorizontalLayout headerElement = new HorizontalLayout();
			final Button parentTaskListFilterButton = new Button(
					AppContext.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASKS),

					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListFilterControl
									.setPopupVisible(false);
							TaskListDepot.this.displayActiveTasksOnly();
						}

					});
			parentTaskListFilterButton.setIcon(MyCollabResource
					.newResource("icons/12/project/task_filter.png"));

			taskListFilterControl = new SplitButton(parentTaskListFilterButton);
			taskListFilterControl.addStyleName(UIConstants.THEME_BLANK_LINK);

			headerElement.setSpacing(true);
			headerElement.setMargin(false);
			UiUtils.addComponent(headerElement, this.taskListFilterControl,
					Alignment.TOP_CENTER);

			final VerticalLayout filterBtnLayout = new VerticalLayout();
			filterBtnLayout.setMargin(true);
			filterBtnLayout.setSpacing(true);
			filterBtnLayout.setWidth("200px");

			final Button allTasksFilterBtn = new Button(
					AppContext.getMessage(TaskGroupI18nEnum.FILTER_ALL_TASKS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListFilterControl
									.setPopupVisible(false);
							parentTaskListFilterButton.setCaption(event
									.getButton().getCaption());
							TaskListDepot.this.displayAllTasks();
						}
					});
			allTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(allTasksFilterBtn);

			final Button activeTasksFilterBtn = new Button(
					AppContext
							.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASKS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListFilterControl
									.setPopupVisible(false);
							parentTaskListFilterButton.setCaption(event
									.getButton().getCaption());
							TaskListDepot.this.displayActiveTasksOnly();
						}
					});
			activeTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(activeTasksFilterBtn);

			final Button pendingTasksFilterBtn = new Button(
					AppContext
							.getMessage(TaskGroupI18nEnum.FILTER_PENDING_TASKS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListFilterControl
									.setPopupVisible(false);
							parentTaskListFilterButton.setCaption(event
									.getButton().getCaption());
							TaskListDepot.this.displayPendingTasksOnly();
						}
					});
			pendingTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(pendingTasksFilterBtn);

			final Button archievedTasksFilterBtn = new Button(
					AppContext
							.getMessage(TaskGroupI18nEnum.FILTER_ARCHIEVED_TASKS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListFilterControl
									.setPopupVisible(false);
							parentTaskListFilterButton.setCaption(event
									.getButton().getCaption());
							TaskListDepot.this.displayInActiveTasks();
						}
					});
			archievedTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(archievedTasksFilterBtn);
			this.taskListFilterControl.setContent(filterBtnLayout);

			this.taskListActionControl = new PopupButton();
			this.taskListActionControl
					.addStyleName(UIConstants.THEME_BLANK_LINK);
			this.taskListActionControl.setIcon(MyCollabResource
					.newResource("icons/16/option.png"));
			taskListActionControl.setWidth(Sizeable.SIZE_UNDEFINED,
					Sizeable.Unit.PIXELS);
			UiUtils.addComponent(headerElement, this.taskListActionControl,
					Alignment.MIDDLE_CENTER);
			this.addHeaderElement(headerElement);

			final VerticalLayout actionBtnLayout = new VerticalLayout();
			actionBtnLayout.setMargin(true);
			actionBtnLayout.setSpacing(true);
			actionBtnLayout.setWidth("200px");
			this.taskListActionControl.setContent(actionBtnLayout);

			final Button readBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_VIEW),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListActionControl
									.setPopupVisible(false);
							EventBusFactory
									.getInstance()
									.post(new TaskListEvent.GotoRead(event,
											TaskListDepot.this.taskList.getId()));
						}
					});
			readBtn.setEnabled(CurrentProjectVariables
					.canRead(ProjectRolePermissionCollections.TASKS));
			readBtn.setStyleName("link");
			actionBtnLayout.addComponent(readBtn);

			final Button editBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListActionControl
									.setPopupVisible(false);
							EventBusFactory.getInstance().post(
									new TaskListEvent.GotoEdit(event,
											TaskListDepot.this.taskList));
						}
					});
			editBtn.setEnabled(CurrentProjectVariables
					.canWrite(ProjectRolePermissionCollections.TASKS));
			editBtn.setStyleName("link");
			actionBtnLayout.addComponent(editBtn);

			final Button closeBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListActionControl
									.setPopupVisible(false);
							TaskListDepot.this.taskList.setStatus("Closed");
							final ProjectTaskListService taskListService = ApplicationContextUtil
									.getSpringBean(ProjectTaskListService.class);
							taskListService.updateWithSession(
									TaskListDepot.this.taskList,
									AppContext.getUsername());
							final Component parentComp = TaskListDepot.this
									.getParent();
							if (parentComp instanceof CssLayout) {
								((CssLayout) parentComp)
										.removeComponent(TaskListDepot.this);
							} else {
								((TaskGroupDisplayWidget) parentComp)
										.removeRow(TaskListDepot.this);
							}
						}
					});
			closeBtn.setEnabled(CurrentProjectVariables
					.canWrite(ProjectRolePermissionCollections.TASKS));
			closeBtn.setStyleName("link");
			actionBtnLayout.addComponent(closeBtn);

			final Button deleteBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TaskListDepot.this.taskListActionControl
									.setPopupVisible(false);
							ConfirmDialogExt.show(
									UI.getCurrent(),
									AppContext.getMessage(
											GenericI18Enum.DIALOG_DELETE_TITLE,
											SiteConfiguration.getSiteName()),
									AppContext
											.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
									AppContext
											.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
									AppContext
											.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
									new ConfirmDialog.Listener() {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClose(
												final ConfirmDialog dialog) {
											if (dialog.isConfirmed()) {
												final ProjectTaskListService taskListService = ApplicationContextUtil
														.getSpringBean(ProjectTaskListService.class);
												taskListService
														.removeWithSession(
																TaskListDepot.this.taskList
																		.getId(),
																AppContext
																		.getUsername(),
																AppContext
																		.getAccountId());
												final Component parentComp = TaskListDepot.this
														.getParent();
												if (parentComp instanceof CssLayout) {
													((CssLayout) parentComp)
															.removeComponent(TaskListDepot.this);
												} else {
													((TaskGroupDisplayWidget) parentComp)
															.removeRow(TaskListDepot.this);
												}

											}
										}
									});
						}
					});
			deleteBtn.setEnabled(CurrentProjectVariables
					.canAccess(ProjectRolePermissionCollections.TASKS));
			deleteBtn.setStyleName("link");
			actionBtnLayout.addComponent(deleteBtn);
		}

		private TaskSearchCriteria createBaseSearchCriteria() {
			final TaskSearchCriteria criteria = new TaskSearchCriteria();
			criteria.setProjectid(new NumberSearchField(CurrentProjectVariables
					.getProjectId()));
			criteria.setTaskListId(new NumberSearchField(this.taskList.getId()));
			return criteria;
		}

		private void displayActiveTasksOnly() {
			final TaskSearchCriteria criteria = this.createBaseSearchCriteria();
			criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
					new String[] { "Open" }));
			this.taskDisplayComponent.setSearchCriteria(criteria);
		}

		private void displayPendingTasksOnly() {
			final TaskSearchCriteria criteria = this.createBaseSearchCriteria();
			criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
					new String[] { "Pending" }));
			this.taskDisplayComponent.setSearchCriteria(criteria);
		}

		private void displayAllTasks() {
			final TaskSearchCriteria criteria = this.createBaseSearchCriteria();
			this.taskDisplayComponent.setSearchCriteria(criteria);
		}

		private void displayInActiveTasks() {
			final TaskSearchCriteria criteria = this.createBaseSearchCriteria();
			criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
					new String[] { "Closed" }));
			this.taskDisplayComponent.setSearchCriteria(criteria);
		}
	}

}
