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
package com.esofthead.mycollab.module.project.view.task.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.task.TaskPopupFieldFactory;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class TaskRowRenderer extends MVerticalLayout {
    private SimpleTask task;

    private PopupButton taskSettingPopupBtn;
    private ToogleTaskSummaryField toogleTaskField;

    TaskRowRenderer(final SimpleTask task) {
        this.task = task;
        withSpacing(true).withMargin(false).withWidth("100%").addStyleName("taskrow");

        taskSettingPopupBtn = new PopupButton();
        taskSettingPopupBtn.setIcon(FontAwesome.COGS);
        taskSettingPopupBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);

        toogleTaskField = new ToogleTaskSummaryField(task);
        MHorizontalLayout headerLayout = new MHorizontalLayout().withWidth("100%").withMargin(new MarginInfo(false,
                true, false, false));

        TaskPopupFieldFactory popupFieldFactory = ViewManager.getCacheComponent(TaskPopupFieldFactory.class);
        PopupView priorityField = popupFieldFactory.createPriorityPopupField(task);
        PopupView assigneeField = popupFieldFactory.createAssigneePopupField(task);
        headerLayout.with(taskSettingPopupBtn, priorityField, assigneeField, toogleTaskField).expand(toogleTaskField);

        CssLayout footer = new CssLayout();

        PopupView commentField = popupFieldFactory.createCommentsPopupField(task);
        footer.addComponent(commentField);

        PopupView followerView = popupFieldFactory.createFollowersPopupField(task);
        footer.addComponent(followerView);

        PopupView statusField = popupFieldFactory.createStatusPopupField(task);
        footer.addComponent(statusField);

        PopupView milestoneField = popupFieldFactory.createMilestonePopupField(task);
        footer.addComponent(milestoneField);

        PopupView percentageField = popupFieldFactory.createPercentagePopupField(task);
        footer.addComponent(percentageField);

        String deadlineTooltip = String.format("%s: %s", AppContext.getMessage(TaskI18nEnum.FORM_DEADLINE),
                AppContext.formatDate(task.getDeadline()));
        PopupView deadlineField = popupFieldFactory.createDeadlinePopupField(task);
        deadlineField.setDescription(deadlineTooltip);
        footer.addComponent(deadlineField);

        PopupView startdateField = popupFieldFactory.createStartDatePopupField(task);
        footer.addComponent(startdateField);

        PopupView enddateField = popupFieldFactory.createEndDatePopupField(task);
        footer.addComponent(enddateField);

        PopupView billableHoursField = popupFieldFactory.createBillableHoursPopupField(task);
        footer.addComponent(billableHoursField);

        PopupView nonBillableHours = popupFieldFactory.createNonBillableHoursPopupField(task);
        footer.addComponent(nonBillableHours);

        this.with(headerLayout, footer);
    }


    private void closeTask() {
        toogleTaskField.closeTask();
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void reOpenTask() {
        toogleTaskField.reOpenTask();
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void pendingTask() {
        toogleTaskField.pendingTask();
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void deleteTask() {
        ComponentContainer parent = (ComponentContainer) this.getParent();
        if (parent != null) {
            parent.removeComponent(this);
        }
    }

    private OptionPopupContent createPopupContent() {
        OptionPopupContent filterBtnLayout = new OptionPopupContent();

        Button editButton = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                taskSettingPopupBtn.setPopupVisible(false);
                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(TaskRowRenderer.this, task));
            }
        });
        editButton.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
        editButton.setIcon(FontAwesome.EDIT);
        filterBtnLayout.addOption(editButton);
        filterBtnLayout.addSeparator();

        if (!task.isCompleted()) {
            Button closeBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    task.setStatus(OptionI18nEnum.StatusI18nEnum.Closed.name());
                    task.setPercentagecomplete(100d);
                    ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                    taskSettingPopupBtn.setPopupVisible(false);
                    closeTask();
                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
                }
            });
            closeBtn.setIcon(FontAwesome.CHECK_CIRCLE_O);
            closeBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            filterBtnLayout.addOption(closeBtn);
        } else {
            Button reOpenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    taskSettingPopupBtn.setPopupVisible(false);
                    task.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
                    task.setPercentagecomplete(0d);

                    ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                    reOpenTask();
                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
                }
            });
            reOpenBtn.setIcon(FontAwesome.UNLOCK);
            reOpenBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            filterBtnLayout.addOption(reOpenBtn);
        }

        if (!OptionI18nEnum.StatusI18nEnum.Pending.name().equals(task.getStatus())) {
            if (!OptionI18nEnum.StatusI18nEnum.Closed.name().equals(task.getStatus())) {
                Button pendingBtn = new Button(AppContext.getMessage(OptionI18nEnum.StatusI18nEnum.Pending), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        taskSettingPopupBtn.setPopupVisible(false);
                        task.setStatus(OptionI18nEnum.StatusI18nEnum.Pending.name());
                        task.setPercentagecomplete(0d);

                        ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                        projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                        pendingTask();
                        EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
                    }
                });
                pendingBtn.setIcon(FontAwesome.HDD_O);
                pendingBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
                filterBtnLayout.addOption(pendingBtn);
            }
        } else {
            Button reOpenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    taskSettingPopupBtn.setPopupVisible(false);
                    task.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
                    task.setPercentagecomplete(0d);

                    ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());

                    reOpenTask();
                }
            });
            reOpenBtn.setIcon(FontAwesome.UNLOCK);
            reOpenBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            filterBtnLayout.addOption(reOpenBtn);
        }

        filterBtnLayout.addSeparator();
        Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                taskSettingPopupBtn.setPopupVisible(false);
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    ProjectTaskService projectTaskService = ApplicationContextUtil.
                                            getSpringBean(ProjectTaskService.class);
                                    projectTaskService.removeWithSession(task, AppContext.getUsername(), AppContext.getAccountId());
                                    deleteTask();
                                }
                            }
                        });
            }
        });
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.TASKS));
        filterBtnLayout.addDangerOption(deleteBtn);
        return filterBtnLayout;
    }
}
