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

import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DefaultProjectFormViewFieldFactory.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class TaskReadViewImpl extends AbstractPreviewItemComp<SimpleTask>
		implements TaskReadView {

	private static final long serialVersionUID = 1L;

	private CommentDisplay commentList;

	private TaskHistoryList historyList;

	private TaskFollowersSheet followerSheet;

	private TaskTimeLogSheet timesheet;

	private Button quickActionStatusBtn;

	public TaskReadViewImpl() {
		super("Task Detail", MyCollabResource
				.newResource("icons/24/project/task.png"));
	}

	@Override
	public SimpleTask getItem() {
		return beanItem;
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@Override
	public void addViewListener(
			ApplicationEventListener<? extends ApplicationEvent> listener) {

	}

	@Override
	public HasPreviewFormHandlers<SimpleTask> getPreviewFormHandlers() {
		return this.previewForm;
	}

	@Override
	protected void initRelatedComponents() {
		commentList = new CommentDisplay(CommentType.PRJ_TASK,
				CurrentProjectVariables.getProjectId(), true, true,
				ProjectTaskRelayEmailNotificationAction.class);
		commentList.setMargin(true);

		historyList = new TaskHistoryList();
		historyList.setMargin(true);

		followerSheet = new TaskFollowersSheet(beanItem);
		timesheet = new TaskTimeLogSheet(beanItem);
	}

	@Override
	protected void onPreviewItem() {
		previewLayout.clearTitleStyleName();
		if (beanItem.getPercentagecomplete() != null
				&& 100d == beanItem.getPercentagecomplete()) {
			addLayoutStyleName(UIConstants.LINK_COMPLETED);
		} else {
			if ("Pending".equals(beanItem.getStatus())) {
				addLayoutStyleName(UIConstants.LINK_PENDING);
			} else if ((beanItem.getEnddate() != null && (beanItem.getEnddate()
					.before(new GregorianCalendar().getTime())))
					|| (beanItem.getActualenddate() != null && (beanItem
							.getActualenddate().before(new GregorianCalendar()
							.getTime())))
					|| (beanItem.getDeadline() != null && (beanItem
							.getDeadline().before(new GregorianCalendar()
							.getTime())))) {
				addLayoutStyleName(UIConstants.LINK_OVERDUE);
			}
		}

		if (beanItem.getStatus() == null || beanItem.getStatus().equals("Open")) {
			quickActionStatusBtn.setCaption(AppContext
					.getMessage(GenericI18Enum.BUTTON_CLOSE_LABEL));
			quickActionStatusBtn.setIcon(MyCollabResource
					.newResource("icons/16/project/closeTask.png"));
		} else {
			quickActionStatusBtn.setCaption("ReOpen");
			quickActionStatusBtn.setIcon(MyCollabResource
					.newResource("icons/16/project/reopenTask.png"));

		}

		commentList.loadComments(beanItem.getId());

		historyList.loadHistory(beanItem.getId());

		followerSheet.setBean(beanItem);
		followerSheet.displayMonitorItems();

		timesheet.loadTimeValue(beanItem);
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getTaskname();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleTask> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleTask>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleTask> initBeanFormFieldFactory() {
		return new ReadFormFieldFactory(previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		ProjectPreviewFormControlsGenerator<SimpleTask> taskPreviewForm = new ProjectPreviewFormControlsGenerator<SimpleTask>(
				previewForm);
		final HorizontalLayout topPanel = taskPreviewForm
				.createButtonControls(
						ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.ASSIGN_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
						ProjectRolePermissionCollections.TASKS);

		quickActionStatusBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (beanItem.getStatus() != null
						&& beanItem.getStatus().equals("Closed")) {
					beanItem.setStatus("Open");
					beanItem.setPercentagecomplete(0d);
					TaskReadViewImpl.this
							.removeLayoutStyleName(UIConstants.LINK_COMPLETED);
					quickActionStatusBtn.setCaption(AppContext
							.getMessage(GenericI18Enum.BUTTON_CLOSE_LABEL));
					quickActionStatusBtn.setIcon(MyCollabResource
							.newResource("icons/16/project/closeTask.png"));
				} else {
					beanItem.setStatus("Closed");
					beanItem.setPercentagecomplete(100d);
					TaskReadViewImpl.this
							.addLayoutStyleName(UIConstants.LINK_COMPLETED);
					quickActionStatusBtn.setCaption(AppContext
							.getMessage(GenericI18Enum.BUTTON_REOPEN_LABEL));
					quickActionStatusBtn.setIcon(MyCollabResource
							.newResource("icons/16/project/reopenTask.png"));
				}

				ProjectTaskService service = ApplicationContextUtil
						.getSpringBean(ProjectTaskService.class);
				service.updateWithSession(beanItem, AppContext.getUsername());

			}
		});

		quickActionStatusBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		taskPreviewForm.insertToControlBlock(quickActionStatusBtn);

		if (!CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS)) {
			quickActionStatusBtn.setEnabled(false);
		}

		return topPanel;
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabTaskDetail = new TabsheetLazyLoadComp();
		tabTaskDetail.setWidth("100%");

		tabTaskDetail.addTab(commentList, AppContext
				.getMessage(ProjectCommonI18nEnum.COMMENT_TAB),
				MyCollabResource
						.newResource("icons/16/project/gray/comment.png"));

		tabTaskDetail.addTab(historyList, AppContext
				.getMessage(ProjectCommonI18nEnum.HISTORY_TAB),
				MyCollabResource
						.newResource("icons/16/project/gray/history.png"));

		tabTaskDetail.addTab(followerSheet, AppContext
				.getMessage(TaskI18nEnum.FOLLOWERS_TAB), MyCollabResource
				.newResource("icons/16/project/gray/follow.png"));

		tabTaskDetail.addTab(timesheet,
				AppContext.getMessage(TaskI18nEnum.TIME_TAB),
				MyCollabResource.newResource("icons/16/project/gray/time.png"));

		return tabTaskDetail;
	}

	private class ReadFormFieldFactory extends
			AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
		private static final long serialVersionUID = 1L;

		public ReadFormFieldFactory(GenericBeanForm<SimpleTask> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {

			if (propertyId.equals("assignuser")) {
				return new ProjectUserFormLinkField(beanItem.getAssignuser(),
						beanItem.getAssignUserAvatarId(),
						beanItem.getAssignUserFullName());
			} else if (propertyId.equals("taskListName")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						beanItem.getTaskListName());
			} else if (propertyId.equals("startdate")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						AppContext.formatDate(beanItem.getStartdate()));
			} else if (propertyId.equals("enddate")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						AppContext.formatDate(beanItem.getEnddate()));
			} else if (propertyId.equals("actualstartdate")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						AppContext.formatDate(beanItem.getActualstartdate()));
			} else if (propertyId.equals("actualenddate")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						AppContext.formatDate(beanItem.getActualenddate()));
			} else if (propertyId.equals("deadline")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						AppContext.formatDate(beanItem.getDeadline()));
			} else if (propertyId.equals("tasklistid")) {
				return new DefaultFormViewFieldFactory.FormLinkViewField(
						beanItem.getTaskListName(),
						ProjectLinkBuilder.generateTaskGroupPreviewFullLink(
								beanItem.getProjectid(),
								beanItem.getTasklistid()),
						MyCollabResource
								.newResourceLink("icons/16/crm/task_group.png"));
			} else if (propertyId.equals("id")) {
				return new ProjectFormAttachmentDisplayField(
						beanItem.getProjectid(),
						AttachmentType.PROJECT_TASK_TYPE, beanItem.getId());
			} else if (propertyId.equals("priority")) {
				if (StringUtils.isNotNullOrEmpty(beanItem.getPriority())) {
					final Resource iconPriority = new ExternalResource(
							ProjectResources
									.getIconResourceLink12ByTaskPriority(beanItem
											.getPriority()));
					final Embedded iconEmbedded = new Embedded(null,
							iconPriority);
					final Label lbPriority = new Label(beanItem.getPriority());

					final FormContainerHorizontalViewField containerField = new FormContainerHorizontalViewField();
					containerField.addComponentField(iconEmbedded);
					containerField.getLayout().setComponentAlignment(
							iconEmbedded, Alignment.MIDDLE_LEFT);
					lbPriority.setWidth("220px");
					containerField.addComponentField(lbPriority);
					containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
					return containerField;
				}
			} else if (propertyId.equals("notes")) {
				return new FormDetectAndDisplayUrlViewField(beanItem.getNotes());
			}
			return null;
		}
	}
}
