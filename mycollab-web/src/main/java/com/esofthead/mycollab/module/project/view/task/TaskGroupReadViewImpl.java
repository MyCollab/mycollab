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

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.AppContext;
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
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TaskGroupReadViewImpl extends
		AbstractPreviewItemComp2<SimpleTaskList> implements TaskGroupReadView {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(TaskGroupReadViewImpl.class);

	private CommentDisplay commentList;

	private SubTasksDisplayComp taskDisplayComp;

	private TaskGroupHistoryLogList historyList;
	private SplitButton taskListFilterControl;

	private DateInfoComp dateInfoComp;

	private PeopleInfoComp peopleInfoComp;

	public TaskGroupReadViewImpl() {
		super(AppContext
				.getMessage(TaskGroupI18nEnum.FORM_VIEW_TASKGROUP_TITLE),
				MyCollabResource.newResource("icons/22/project/task_group.png"));
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

		dateInfoComp = new DateInfoComp();
		addToSideBar(dateInfoComp);

		peopleInfoComp = new PeopleInfoComp();
		addToSideBar(peopleInfoComp);
	}

	@Override
	protected void onPreviewItem() {
		commentList.loadComments("" + beanItem.getId());
		historyList.loadHistory(beanItem.getId());

		peopleInfoComp.displayEntryPeople(beanItem);
		dateInfoComp.displayEntryDateTime(beanItem);
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
				previewForm))
				.createButtonControls(
						ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.ASSIGN_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
						ProjectRolePermissionCollections.TASKS);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
		tabContainer.setWidth("100%");

		tabContainer.addTab(commentList, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_COMMENT),
				MyCollabResource
						.newResource("icons/16/project/gray/comment.png"));

		tabContainer.addTab(historyList, AppContext
				.getMessage(ProjectCommonI18nEnum.TAB_HISTORY),
				MyCollabResource
						.newResource("icons/16/project/gray/history.png"));

		tabContainer.addTab(taskDisplayComp,
				AppContext.getMessage(TaskGroupI18nEnum.TASKS_TAB),
				MyCollabResource.newResource("icons/16/project/gray/task.png"));

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
					AppContext
							.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASKS),
					new Button.ClickListener() {

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

			final Button allTasksFilterBtn = new Button(
					AppContext.getMessage(TaskGroupI18nEnum.FILTER_ALL_TASKS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							taskListFilterControl.setPopupVisible(false);
							taskListFilterControl.setCaption(AppContext
									.getMessage(TaskGroupI18nEnum.FILTER_ALL_TASKS));
							SubTasksDisplayComp.this.displayAllTasks();
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
							taskListFilterControl.setPopupVisible(false);
							taskListFilterControl.setCaption(AppContext
									.getMessage(TaskGroupI18nEnum.FILTER_ACTIVE_TASKS));
							SubTasksDisplayComp.this.displayActiveTasksOnly();
						}
					});
			activeTasksFilterBtn.setStyleName("link");
			filterBtnLayout.addComponent(activeTasksFilterBtn);

			final Button archievedTasksFilterBtn = new Button(
					AppContext
							.getMessage(TaskGroupI18nEnum.FILTER_ARCHIEVED_TASKS),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							taskListFilterControl.setCaption(AppContext
									.getMessage(TaskGroupI18nEnum.FILTER_ARCHIEVED_TASKS));
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

	private class PeopleInfoComp extends VerticalLayout {
		private static final long serialVersionUID = 1L;

		public void displayEntryPeople(ValuedBean bean) {
			this.removeAllComponents();
			this.setSpacing(true);
			this.setMargin(new MarginInfo(false, false, false, true));

			Label peopleInfoHeader = new Label(
					AppContext
							.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE));
			peopleInfoHeader.setStyleName("info-hdr");
			this.addComponent(peopleInfoHeader);

			GridLayout layout = new GridLayout(2, 2);
			layout.setSpacing(true);
			layout.setWidth("100%");
			layout.setMargin(new MarginInfo(false, false, false, true));
			try {
				Label createdLbl = new Label(
						AppContext
								.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
				createdLbl.setSizeUndefined();
				layout.addComponent(createdLbl, 0, 0);

				String createdUserName = (String) PropertyUtils.getProperty(
						bean, "createduser");
				String createdUserAvatarId = (String) PropertyUtils
						.getProperty(bean, "createdUserAvatarId");
				String createdUserDisplayName = (String) PropertyUtils
						.getProperty(bean, "createdUserFullName");

				UserLink createdUserLink = new UserLink(createdUserName,
						createdUserAvatarId, createdUserDisplayName);
				layout.addComponent(createdUserLink, 1, 0);
				layout.setColumnExpandRatio(1, 1.0f);

				Label assigneeLbl = new Label(
						AppContext
								.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
				assigneeLbl.setSizeUndefined();
				layout.addComponent(assigneeLbl, 0, 1);
				String assignUserName = (String) PropertyUtils.getProperty(
						bean, "owner");
				String assignUserAvatarId = (String) PropertyUtils.getProperty(
						bean, "ownerAvatarId");
				String assignUserDisplayName = (String) PropertyUtils
						.getProperty(bean, "ownerFullName");

				UserLink assignUserLink = new UserLink(assignUserName,
						assignUserAvatarId, assignUserDisplayName);
				layout.addComponent(assignUserLink, 1, 1);
			} catch (Exception e) {
				log.error("Can not build user link {} ",
						BeanUtility.printBeanObj(bean));
			}

			this.addComponent(layout);

		}
	}
}
