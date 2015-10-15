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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.form.field.AttachmentUploadField;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class TaskAddWindow extends Window {
    TaskAddWindow(SimpleTask task) {
        if (task.getId() == null) {
            setCaption("New task");
        } else {
            setCaption("Edit task");
        }
        this.setWidth("800px");
        this.setModal(true);
        this.setResizable(false);

        EditForm editForm = new EditForm();
        task.setLogby(AppContext.getUsername());
        task.setSaccountid(AppContext.getAccountId());
        task.setProjectid(CurrentProjectVariables.getProjectId());
        editForm.setBean(task);
        this.setContent(editForm);
    }

    private class EditForm extends AdvancedEditBeanForm<SimpleTask> {

        @Override
        public void setBean(final SimpleTask item) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new TaskEditFormFieldFactory(EditForm.this));
            super.setBean(item);
        }

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
                informationLayout.getLayout().setMargin(false);
                informationLayout.getLayout().setSpacing(false);
                layout.addComponent(informationLayout.getLayout());

                MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
                buttonControls.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

                Button updateAllBtn = new Button("Update other fields", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(TaskAddWindow.this, EditForm.this.bean));
                        close();
                    }
                });
                updateAllBtn.addStyleName(UIConstants.THEME_LINK);

                Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        if (EditForm.this.validateForm()) {
                            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                            Integer taskId;
                            if (bean.getId() == null) {
                                taskId = taskService.saveWithSession(bean, AppContext.getUsername());
                            } else {
                                taskService.updateWithSession(bean, AppContext.getUsername());
                                taskId = bean.getId();
                            }

                            AttachmentUploadField uploadField = ((TaskEditFormFieldFactory) EditForm.this
                                    .getFieldFactory()).getAttachmentUploadField();
                            String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(
                                    AppContext.getAccountId(), CurrentProjectVariables.getProjectId(),
                                    ProjectTypeConstants.TASK, "" + taskId);
                            uploadField.saveContentsToRepo(attachPath);
                            EventBusFactory.getInstance().post(new TaskEvent.NewTaskAdded(TaskAddWindow.this, taskId));
                            close();
                        }
                    }
                });
                saveBtn.setIcon(FontAwesome.SAVE);
                saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        TaskAddWindow.this.close();
                    }
                });
                cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
                buttonControls.with(updateAllBtn, cancelBtn, saveBtn);

                layout.addComponent(buttonControls);
                layout.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected void onAttachField(Object propertyId, Field<?> field) {
                if (Task.Field.taskname.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(TaskI18nEnum.FORM_TASK_NAME), 0, 0, 2, "100%");
                } else if (Task.Field.startdate.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(TaskI18nEnum.FORM_START_DATE), 0, 1);
                } else if (Task.Field.enddate.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(TaskI18nEnum.FORM_END_DATE), 1, 1);
                } else if (Task.Field.deadline.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(TaskI18nEnum.FORM_DEADLINE), 0, 2);
                } else if (Task.Field.assignuser.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 2);
                } else if (Task.Field.duration.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(TaskI18nEnum.FORM_DURATION), 0, 3);
                } else if (Task.Field.isestimated.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(TaskI18nEnum.FORM_IS_ESTIMATED), 1, 3);
                } else if (Task.Field.notes.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 4, 2, "100%");
                } else if (Task.Field.id.equalTo(propertyId)) {
                    informationLayout.addComponent(field, AppContext.getMessage(TaskI18nEnum.FORM_ATTACHMENT), 0, 5, 2, "100%");
                }
            }
        }
    }
}
