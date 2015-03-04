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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.TaskPercentageCompleteComboBox;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class TaskAddPopup extends CustomComponent {
    private static final long serialVersionUID = 1L;
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

        final Label titleLbl = new Label(
                AppContext.getMessage(TaskI18nEnum.DIALOG_NEW_TASK_TITLE));
        titleLbl.addStyleName("bold");
        popupHeader.addComponent(titleLbl);
        taskLayout.addComponent(popupHeader);

        this.task = new SimpleTask();
        TabSheet taskContainer = new TabSheet();
        final TaskInputForm taskInputForm = new TaskInputForm();
        taskInputForm.setWidth("100%");
        taskContainer.addTab(taskInputForm,
                AppContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE));

        this.taskNoteComponent = new TaskNoteLayout();
        taskContainer.addTab(this.taskNoteComponent,
                AppContext.getMessage(TaskI18nEnum.FORM_NOTES_ATTACHMENT));

        taskLayout.addComponent(taskContainer);

        final MHorizontalLayout controlsLayout = new MHorizontalLayout().withMargin(true);
        controlsLayout.addStyleName("popup-footer");

        final Button cancelBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        taskDisplayComp.closeTaskAdd();
                    }
                });

        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        final Button saveBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
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
        saveBtn.setIcon(FontAwesome.SAVE);
        saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

        controlsLayout.with(saveBtn, cancelBtn).alignAll(Alignment.MIDDLE_CENTER);

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
        public ComponentContainer getLayout() {
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
        public void attachField(final Object propertyId, final Field<?> field) {
            if (propertyId.equals("taskname")) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(TaskI18nEnum.FORM_TASK_NAME), 0,
                        0, 2, "100%");
            } else if (propertyId.equals("startdate")) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(TaskI18nEnum.FORM_START_DATE), 0,
                        1);
            } else if (propertyId.equals("enddate")) {
                this.informationLayout
                        .addComponent(field, AppContext
                                .getMessage(TaskI18nEnum.FORM_END_DATE), 0, 2);
            } else if (propertyId.equals("actualstartdate")) {
                this.informationLayout.addComponent(field, AppContext
                        .getMessage(TaskI18nEnum.FORM_ACTUAL_START_DATE), 1, 1);
            } else if (propertyId.equals("actualenddate")) {
                this.informationLayout.addComponent(field, AppContext
                        .getMessage(TaskI18nEnum.FORM_ACTUAL_END_DATE), 1, 2);
            } else if (propertyId.equals("deadline")) {
                this.informationLayout
                        .addComponent(field, AppContext
                                .getMessage(TaskI18nEnum.FORM_DEADLINE), 0, 3);
            } else if (propertyId.equals("priority")) {
                this.informationLayout
                        .addComponent(field, AppContext
                                .getMessage(TaskI18nEnum.FORM_PRIORITY), 1, 3);
            } else if (propertyId.equals("assignuser")) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0,
                        4);
            } else if (propertyId.equals("percentagecomplete")) {
                this.informationLayout.addComponent(field, AppContext
                                .getMessage(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE), 1,
                        4);
            }
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
                    task.setPriority(TaskPriority.Medium.name());
                }
                return new TaskPriorityComboBox();
            }
            return null;
        }
    }
}
