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

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormLinkViewField;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProgressPercentageIndicator;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TaskGroupReadViewImpl extends
		AbstractPreviewItemComp<SimpleTaskList> implements TaskGroupReadView {

	private static final long serialVersionUID = 1L;

	private CommentDisplay commentList;

	private SubTasksDisplayComp taskDisplayComp;

	private TaskGroupHistoryLogList historyList;
	private SplitButton taskListFilterControl;

	public TaskGroupReadViewImpl() {
		super("Task Group", MyCollabResource
				.newResource("icons/22/project/task_group.png"));
	}

	@Override
	public HasPreviewFormHandlers<SimpleTaskList> getPreviewFormHandlers() {
		return this.previewForm;
	}

	@Override
	protected void initRelatedComponents() {
		commentList = new CommentDisplay(CommentType.PRJ_TASK_LIST,
				CurrentProjectVariables.getProjectId(), true, true,
				ProjectTaskGroupRelayEmailNotificationAction.class);
		commentList.setWidth("100%");
		commentList.setMargin(true);

		taskDisplayComp = new SubTasksDisplayComp();

		historyList = new TaskGroupHistoryLogList();
		historyList.setMargin(true);
	}

	@Override
	protected void onPreviewItem() {
		commentList.loadComments(beanItem.getId());
		historyList.loadHistory(beanItem.getId());
	}

	@Override
	protected String initFormTitle() {
		if ("Closed".equals(beanItem.getStatus())) {
			this.addLayoutStyleName(UIConstants.LINK_COMPLETED);
		}
		return beanItem.getName();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleTaskList> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleTaskList>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskGroupFormLayoutFactory();
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return (new ProjectPreviewFormControlsGenerator<SimpleTaskList>(
				previewForm)).createButtonControls(
				ProjectRolePermissionCollections.TASKS, true);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
		tabContainer.setWidth("100%");

		tabContainer.addTab(commentList, "Comments", MyCollabResource
				.newResource("icons/16/project/gray/comment.png"));

		tabContainer.addTab(taskDisplayComp, "Tasks",
				MyCollabResource.newResource("icons/16/project/gray/task.png"));

		tabContainer.addTab(historyList, "History", MyCollabResource
				.newResource("icons/16/project/gray/history.png"));
		return tabContainer;
	}

	@Override
	public SimpleTaskList getItem() {
		return beanItem;
	}

	class SubTasksDisplayComp extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		private final TaskDisplayComponent taskDisplayComponent;

		public SubTasksDisplayComp() {
			this.addStyleName("task-list");
			this.initHeader();
			this.taskDisplayComponent = new TaskDisplayComponent(beanItem,
					false);

			this.addComponent(taskDisplayComponent);
		}

		private void initHeader() {
			final CssLayout componentHeader = new CssLayout();
			componentHeader.setStyleName("comp-header");

			final Button parentTaskListFilterButton = new Button(
					"Active Tasks only", new Button.ClickListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							taskListFilterControl.setPopupVisible(false);
							SubTasksDisplayComp.this.displayActiveTasksOnly();
						}

					});

			taskListFilterControl = new SplitButton(parentTaskListFilterButton);
			taskListFilterControl.addStyleName(UIConstants.THEME_BLANK_LINK);
			taskListFilterControl.setWidth(Sizeable.SIZE_UNDEFINED,
					Sizeable.Unit.PIXELS);

			final VerticalLayout filterBtnLayout = new VerticalLayout();

			filterBtnLayout.setWidth("120px");

			filterBtnLayout.setMargin(true);
			filterBtnLayout.setSpacing(true);

			final Button allTasksFilterBtn = new Button("All Tasks",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							taskListFilterControl.setPopupVisible(false);
							taskListFilterControl.setCaption("All Tasks");
							SubTasksDisplayComp.this.displayAllTasks();
						}
					});
			allTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(allTasksFilterBtn);

			final Button activeTasksFilterBtn = new Button("Active Tasks Only",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							taskListFilterControl.setPopupVisible(false);
							taskListFilterControl.setCaption("Active Tasks");
							SubTasksDisplayComp.this.displayActiveTasksOnly();
						}
					});
			activeTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(activeTasksFilterBtn);

			final Button archievedTasksFilterBtn = new Button(
					"Archieved Tasks Only", new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							taskListFilterControl.setCaption("Archieved Tasks");
							taskListFilterControl.setPopupVisible(false);
							SubTasksDisplayComp.this.displayInActiveTasks();
						}
					});
			archievedTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(archievedTasksFilterBtn);
			taskListFilterControl.setContent(filterBtnLayout);

			componentHeader.addComponent(taskListFilterControl);
			this.addComponent(componentHeader);
		}

		private TaskSearchCriteria createBaseSearchCriteria() {
			final TaskSearchCriteria criteria = new TaskSearchCriteria();
			criteria.setProjectid(new NumberSearchField(CurrentProjectVariables
					.getProjectId()));
			criteria.setTaskListId(new NumberSearchField(beanItem.getId()));
			return criteria;
		}

		private void displayActiveTasksOnly() {
			final TaskSearchCriteria criteria = this.createBaseSearchCriteria();
			criteria.setStatuses(new SetSearchField<String>(SearchField.AND,
					new String[] { "Open", "Pending" }));
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

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList> initBeanFormFieldFactory() {
		return new AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList>(
				previewForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Field<?> onCreateField(final Object propertyId) {
				if (propertyId.equals("milestoneid")) {
					return new FormLinkViewField(
							beanItem.getMilestoneName(),
							ProjectLinkBuilder
									.generateMilestonePreviewFullLink(
											beanItem.getProjectid(),
											beanItem.getMilestoneid()),
							MyCollabResource
									.newResourceLink("icons/16/project/milestone.png"));
				} else if (propertyId.equals("owner")) {
					return new ProjectUserFormLinkField(beanItem.getOwner(),
							beanItem.getOwnerAvatarId(),
							beanItem.getOwnerFullName());
				} else if (propertyId.equals("percentageComplete")) {
					final FormContainerHorizontalViewField fieldContainer = new FormContainerHorizontalViewField();
					final ProgressPercentageIndicator progressField = new ProgressPercentageIndicator(
							beanItem.getPercentageComplete());
					fieldContainer.addComponentField(progressField);
					return fieldContainer;
				} else if (propertyId.equals("description")) {
					return new FormDetectAndDisplayUrlViewField(
							beanItem.getDescription());
				} else if (propertyId.equals("numOpenTasks")) {
					final FormContainerHorizontalViewField fieldContainer = new FormContainerHorizontalViewField();
					final Label numTaskLbl = new Label("("
							+ beanItem.getNumOpenTasks() + "/"
							+ beanItem.getNumAllTasks() + ")");
					fieldContainer.addComponentField(numTaskLbl);
					return fieldContainer;
				}

				return null;
			}
		};
	}
}
