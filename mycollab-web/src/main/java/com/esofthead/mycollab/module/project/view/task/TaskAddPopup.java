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

import org.vaadin.easyuploads.MultiFileUploadExt;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.common.localization.WindowI18nEnum;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.TaskPriorityStatusContants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.TaskPercentageCompleteComboBox;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.AttachmentPanel;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
class TaskAddPopup extends CustomComponent {
	private static final long serialVersionUID = 1L;
	private final TabSheet taskContainer;
	private final SimpleTask task;
	private final TaskNoteLayout taskNoteComponent;

	public TaskAddPopup(final TaskDisplayComponent taskDisplayComp,
			final TaskList taskList) {

		final VerticalLayout taskLayout = new VerticalLayout();
		taskLayout.addStyleName("taskadd-popup");

		final VerticalLayout popupHeader = new VerticalLayout();
		popupHeader.setWidth("100%");
		popupHeader.setMargin(true);
		popupHeader.addStyleName("popup-header");

		final Label titleLbl = new Label("Add New Task");
		titleLbl.addStyleName("bold");
		popupHeader.addComponent(titleLbl);
		taskLayout.addComponent(popupHeader);

		this.task = new SimpleTask();
		this.taskContainer = new TabSheet();
		final TaskInputForm taskInputForm = new TaskInputForm();
		taskInputForm.setWidth("100%");
		this.taskContainer.addTab(taskInputForm,
				AppContext.getMessage(WindowI18nEnum.INFORMATION_WINDOW_TITLE));

		this.taskNoteComponent = new TaskNoteLayout();
		this.taskContainer.addTab(this.taskNoteComponent, "Note & Attachments");

		taskLayout.addComponent(this.taskContainer);

		final HorizontalLayout controlsLayout = new HorizontalLayout();
		controlsLayout.setSpacing(true);

		final Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						taskDisplayComp.closeTaskAdd();
					}
				});

		cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
		controlsLayout.addComponent(cancelBtn);
		controlsLayout
				.setComponentAlignment(cancelBtn, Alignment.MIDDLE_CENTER);

		final Button saveBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						if (taskInputForm.validateForm()) {
							final ProjectTaskService taskService = ApplicationContextUtil
									.getSpringBean(ProjectTaskService.class);

							task.setTasklistid(taskList.getId());
							task.setProjectid(CurrentProjectVariables
									.getProjectId());
							task.setSaccountid(AppContext.getAccountId());
							task.setNotes(taskNoteComponent.getNote());

							taskService.saveWithSession(task,
									AppContext.getUsername());
							taskNoteComponent.saveContentsToRepo(task.getId());
							taskDisplayComp.saveTaskSuccess(task);
							taskDisplayComp.closeTaskAdd();
						}
					}
				});
		saveBtn.setIcon(MyCollabResource.newResource("icons/16/save.png"));
		saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		controlsLayout.addComponent(saveBtn);
		controlsLayout.setComponentAlignment(saveBtn, Alignment.MIDDLE_CENTER);
		controlsLayout.addStyleName("popup-footer");
		controlsLayout.setMargin(true);

		taskLayout.addComponent(controlsLayout);
		taskLayout
				.setComponentAlignment(controlsLayout, Alignment.MIDDLE_RIGHT);

		this.setCompositionRoot(taskLayout);
	}

	private class TaskInputForm extends AdvancedEditBeanForm<Task> {
		private static final long serialVersionUID = 1L;

		public TaskInputForm() {
			this.setFormLayoutFactory(new TaskLayout());
			this.setBeanFormFieldFactory(new EditFormFieldFactory(
					TaskInputForm.this));
			this.setBean(task);
		}
	}

	private static class TaskLayout implements IFormLayoutFactory {
		private static final long serialVersionUID = 1L;
		private GridFormLayoutHelper informationLayout;

		@Override
		public Layout getLayout() {
			this.informationLayout = new GridFormLayoutHelper(2, 5, "100%",
					"180px", Alignment.TOP_LEFT);

			final VerticalLayout layout = new VerticalLayout();
			this.informationLayout.getLayout().addStyleName(
					"colored-gridlayout");
			this.informationLayout.getLayout().setMargin(false);
			this.informationLayout.getLayout().setWidth("100%");
			layout.addComponent(this.informationLayout.getLayout());
			return layout;
		}

		@Override
		public boolean attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("taskname")) {
				this.informationLayout.addComponent(field, "Task Name", 0, 0,
						2, "100%");
			} else if (propertyId.equals("startdate")) {
				this.informationLayout.addComponent(field, "Start Date", 0, 1);
			} else if (propertyId.equals("enddate")) {
				this.informationLayout.addComponent(field, "End Date", 0, 2);
			} else if (propertyId.equals("actualstartdate")) {
				this.informationLayout.addComponent(field, "Actual Start Date",
						1, 1);
			} else if (propertyId.equals("actualenddate")) {
				this.informationLayout.addComponent(field, "Actual End Date",
						1, 2);
			} else if (propertyId.equals("deadline")) {
				this.informationLayout.addComponent(field, "Deadline", 0, 3);
			} else if (propertyId.equals("priority")) {
				this.informationLayout.addComponent(field, "Priority", 1, 3);
			} else if (propertyId.equals("assignuser")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD), 0, 4);
			} else if (propertyId.equals("percentagecomplete")) {
				this.informationLayout.addComponent(field, "Complete(%)", 1, 4);
			} else {
				return false;
			}

			return true;
		}
	}

	private class TaskNoteLayout extends VerticalLayout {
		private static final long serialVersionUID = 1L;
		private final RichTextArea noteArea;
		private final AttachmentPanel attachmentPanel;

		public TaskNoteLayout() {
			this.setSpacing(true);
			this.setMargin(true);
			this.noteArea = new RichTextArea();
			this.noteArea.setWidth("100%");
			this.noteArea.setHeight("200px");
			this.addComponent(this.noteArea);

			this.attachmentPanel = new AttachmentPanel();
			this.addComponent(this.attachmentPanel);
			final MultiFileUploadExt uploadExt = new MultiFileUploadExt(
					this.attachmentPanel);
			uploadExt.addComponent(this.attachmentPanel);
			this.addComponent(uploadExt);
			this.setComponentAlignment(uploadExt, Alignment.MIDDLE_LEFT);
		}

		public String getNote() {
			return this.noteArea.getValue();
		}

		void saveContentsToRepo(final Integer typeid) {
			String attachmentPath = AttachmentUtils
					.getProjectEntityAttachmentPath(AppContext.getAccountId(),
							CurrentProjectVariables.getProjectId(),
							AttachmentType.PROJECT_TASK_TYPE, typeid);
			this.attachmentPanel.saveContentsToRepo(attachmentPath);
		}
	}

	private class EditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<Task> {
		private static final long serialVersionUID = 1L;

		public EditFormFieldFactory(GenericBeanForm<Task> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {
			if (propertyId.equals("assignuser")) {
				return new ProjectMemberSelectionField();
			} else if (propertyId.equals("taskname")) {
				final TextField tf = new TextField();
				if (isValidateForm) {
					tf.setNullRepresentation("");
					tf.setRequired(true);
					tf.setRequiredError("Please enter a Task Name");
				}
				return tf;
			} else if (propertyId.equals("percentagecomplete")) {
				if (task.getPercentagecomplete() == null) {
					task.setPercentagecomplete(0d);
				}

				return new TaskPercentageCompleteComboBox();
			} else if ("priority".equals(propertyId)) {
				if (task.getPriority() == null) {
					task.setPriority(TaskPriorityStatusContants.PRIORITY_MEDIUM);
				}
				return new TaskPriorityComboBox();
			}
			return null;
		}
	}
}
