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
package com.mycollab.module.project.view.task;

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AssignTaskWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private final Task task;

    public AssignTaskWindow(Task task) {
        super(UserUIContext.getMessage(TaskI18nEnum.DIALOG_ASSIGN_TASK_TITLE, task.getName()));
        this.task = task;
        MVerticalLayout contentLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false));
        this.withWidth("750px").withModal(true).withResizable(false).withCenter().withContent(contentLayout);
        EditForm editForm = new EditForm();
        contentLayout.addComponent(editForm);
        editForm.setBean(task);
    }

    private class EditForm extends AdvancedEditBeanForm<Task> {
        private static final long serialVersionUID = 1L;
        private RichTextArea commentArea;

        @Override
        public void setBean(Task newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public AbstractComponent getLayout() {
                VerticalLayout layout = new VerticalLayout();
                this.informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 2);
                layout.addComponent(informationLayout.getLayout());

                MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                        .withStyleName(WebThemes.BUTTON_OPTION);

                MButton approveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ASSIGN), clickEvent -> {
                    if (EditForm.this.validateForm()) {
                        // Save task status and assignee
                        ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                        taskService.updateWithSession(task, UserUIContext.getUsername());

                        // Save comment
                        String commentValue = commentArea.getValue();
                        if (StringUtils.isNotBlank(commentValue)) {
                            CommentWithBLOBs comment = new CommentWithBLOBs();
                            comment.setComment(commentArea.getValue());
                            comment.setCreatedtime(new GregorianCalendar().getTime());
                            comment.setCreateduser(UserUIContext.getUsername());
                            comment.setSaccountid(MyCollabUI.getAccountId());
                            comment.setType(ProjectTypeConstants.TASK);
                            comment.setTypeid("" + task.getId());
                            comment.setExtratypeid(CurrentProjectVariables.getProjectId());

                            CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                            commentService.saveWithSession(comment, UserUIContext.getUsername());
                        }

                        close();
                        EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, task.getId()));
                    }
                }).withIcon(FontAwesome.SHARE).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);

                MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, approveBtn).withMargin(true);
                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (Task.Field.assignuser.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 0);
                } else if (propertyId.equals("comment")) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.OPT_COMMENT), 0, 1, 2, "100%");
                }
                return null;
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Task> {
            private static final long serialVersionUID = 1L;

            public EditFormFieldFactory(GenericBeanForm<Task> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if (propertyId.equals("assignuser")) {
                    return new ProjectMemberSelectionField();
                } else if (propertyId.equals("comment")) {
                    commentArea = new RichTextArea();
                    commentArea.setNullRepresentation("");
                    return commentArea;
                }
                return null;
            }
        }
    }
}
