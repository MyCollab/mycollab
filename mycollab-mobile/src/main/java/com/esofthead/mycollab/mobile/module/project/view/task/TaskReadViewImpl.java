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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectAttachmentDisplayComp;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectCommentListDisplay;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormDetectAndDisplayUrlViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
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
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */
@ViewComponent
public class TaskReadViewImpl extends AbstractPreviewItemComp<SimpleTask>
		implements TaskReadView {

	private static final long serialVersionUID = 9021783098267883004L;

	private Button quickActionStatusBtn;

	private ProjectCommentListDisplay associateComments;
	private Button relatedComments;

	private TaskTimeLogComp taskTimeLogComp;

	private ProjectAttachmentDisplayComp attachmentComp;

	public TaskReadViewImpl() {
		super();
		taskTimeLogComp = new TaskTimeLogComp();
	}

	@Override
	public HasPreviewFormHandlers<SimpleTask> getPreviewFormHandlers() {
		return this.previewForm;
	}

	@Override
	protected void afterPreviewItem() {
		if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
			quickActionStatusBtn.setCaption(AppContext
					.getMessage(GenericI18Enum.BUTTON_CLOSE));
			this.removeStyleName(UIConstants.STATUS_DISABLED);
		} else {
			quickActionStatusBtn.setCaption(AppContext
					.getMessage(GenericI18Enum.BUTTON_REOPEN));
			this.addStyleName(UIConstants.STATUS_DISABLED);
		}
		associateComments.loadComments("" + beanItem.getId());
		if (associateComments.getNumComments() > 0) {
			relatedComments
					.setCaption("<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_MESSAGE
							+ "\" data-count=\""
							+ associateComments.getNumComments()
							+ "\"></span><div class=\"screen-reader-text\">"
							+ AppContext
									.getMessage(ProjectCommonI18nEnum.TAB_COMMENT)
							+ "</div>");
		} else {
			relatedComments
					.setCaption("<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_MESSAGE
							+ "\"></span><div class=\"screen-reader-text\">"
							+ AppContext
									.getMessage(ProjectCommonI18nEnum.TAB_COMMENT)
							+ "</div>");
		}

		taskTimeLogComp.displayTime(beanItem);

		this.previewForm.addComponent(taskTimeLogComp);

		ResourceService resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		List<Content> attachments = resourceService.getContents(AttachmentUtils
				.getProjectEntityAttachmentPath(AppContext.getAccountId(),
						beanItem.getProjectid(),
						AttachmentType.PROJECT_TASK_TYPE, beanItem.getId()));
		if (CollectionUtils.isNotEmpty(attachments)) {
			attachmentComp = new ProjectAttachmentDisplayComp(attachments);
			this.previewForm.addComponent(attachmentComp);
		} else if (attachmentComp != null
				&& attachmentComp.getParent().equals(this.previewForm)) {
			this.previewForm.removeComponent(attachmentComp);
		}
	}

	@Override
	protected String initFormTitle() {
		return this.beanItem.getTaskname();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleTask> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleTask>();
	}

	@Override
	protected void initRelatedComponents() {
		associateComments = new ProjectCommentListDisplay(CommentType.PRJ_TASK,
				CurrentProjectVariables.getProjectId(), true, true,
				ProjectTaskRelayEmailNotificationAction.class);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleTask> initBeanFormFieldFactory() {
		return new ReadFormFieldFactory(this.previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		ProjectPreviewFormControlsGenerator<SimpleTask> taskPreviewForm = new ProjectPreviewFormControlsGenerator<SimpleTask>(
				previewForm);
		final VerticalLayout topPanel = taskPreviewForm
				.createButtonControls(
						ProjectPreviewFormControlsGenerator.ASSIGN_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
								| ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
						ProjectRolePermissionCollections.TASKS);

		quickActionStatusBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (beanItem.getStatus() != null
						&& beanItem.getStatus().equals(
								StatusI18nEnum.Closed.name())) {
					beanItem.setStatus(StatusI18nEnum.Open.name());
					beanItem.setPercentagecomplete(0d);
					TaskReadViewImpl.this
							.removeStyleName(UIConstants.STATUS_DISABLED);
					quickActionStatusBtn.setCaption(AppContext
							.getMessage(GenericI18Enum.BUTTON_CLOSE));
				} else {
					beanItem.setStatus(StatusI18nEnum.Closed.name());
					beanItem.setPercentagecomplete(100d);
					TaskReadViewImpl.this
							.addStyleName(UIConstants.STATUS_DISABLED);
					quickActionStatusBtn.setCaption(AppContext
							.getMessage(GenericI18Enum.BUTTON_REOPEN));
				}

				ProjectTaskService service = ApplicationContextUtil
						.getSpringBean(ProjectTaskService.class);
				service.updateWithSession(beanItem, AppContext.getUsername());

			}
		});
		quickActionStatusBtn.setWidth("100%");

		taskPreviewForm.insertToControlBlock(quickActionStatusBtn);

		if (!CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS)) {
			quickActionStatusBtn.setEnabled(false);
		}

		return topPanel;
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		toolbarLayout.setSpacing(true);

		relatedComments = new Button();
		relatedComments.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.PROJECT_MESSAGE
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(ProjectCommonI18nEnum.TAB_COMMENT)
				+ "</div>");
		relatedComments.setHtmlContentAllowed(true);
		relatedComments.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 4889821151518627676L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new ShellEvent.PushView(this, associateComments));
			}
		});
		toolbarLayout.addComponent(relatedComments);

		return toolbarLayout;
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
				return new FormViewField(beanItem.getAssignUserFullName());
			} else if (propertyId.equals("taskListName")) {
				return new FormViewField(beanItem.getTaskListName());
			} else if (propertyId.equals("startdate")) {
				return new FormViewField(AppContext.formatDate(beanItem
						.getStartdate()));
			} else if (propertyId.equals("enddate")) {
				return new FormViewField(AppContext.formatDate(beanItem
						.getEnddate()));
			} else if (propertyId.equals("actualstartdate")) {
				return new FormViewField(AppContext.formatDate(beanItem
						.getActualstartdate()));
			} else if (propertyId.equals("actualenddate")) {
				return new FormViewField(AppContext.formatDate(beanItem
						.getActualenddate()));
			} else if (propertyId.equals("deadline")) {
				return new FormViewField(AppContext.formatDate(beanItem
						.getDeadline()));
			} else if (propertyId.equals("tasklistid")) {
				return new FormViewField(beanItem.getTaskListName());
			} else if (propertyId.equals("priority")) {
				if (StringUtils.isNotBlank(beanItem.getPriority())) {
					final Resource iconPriority = new ExternalResource(
							ProjectResources
									.getIconResourceLink12ByTaskPriority(beanItem
											.getPriority()));
					final Embedded iconEmbedded = new Embedded(null,
							iconPriority);
					final Label lbPriority = new Label(AppContext.getMessage(
							TaskPriority.class, beanItem.getPriority()));

					final FormContainerHorizontalViewField containerField = new FormContainerHorizontalViewField();
					containerField.addComponentField(iconEmbedded);
					containerField.getLayout().setComponentAlignment(
							iconEmbedded, Alignment.MIDDLE_LEFT);
					lbPriority.setWidthUndefined();
					containerField.addComponentField(lbPriority);
					containerField.getLayout().setExpandRatio(lbPriority, 1.0f);
					return containerField;
				}
			} else if (propertyId.equals("notes")) {
				return new FormDetectAndDisplayUrlViewField(beanItem.getNotes());
				// } else if (propertyId.equals("id")) {
				// return new ProjectFormAttachmentDisplayField(
				// beanItem.getProjectid(),
				// AttachmentType.PROJECT_TASK_TYPE, beanItem.getId());
			}
			return null;
		}
	}

}
