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
package com.mycollab.module.project.view.task.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.events.TaskEvent;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.components.IGroupComponent;
import com.mycollab.module.project.view.task.TaskPopupFieldFactory;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class TaskRowRenderer extends MVerticalLayout {
    private SimpleTask task;

    private PopupButton taskSettingPopupBtn;
    private ToggleTaskSummaryField toggleTaskField;

    public TaskRowRenderer(final SimpleTask task) {
        this.task = task;
        withSpacing(true).withMargin(false).withFullWidth().addStyleName(UIConstants.BORDER_LIST_ROW);

        taskSettingPopupBtn = new PopupButton();
        taskSettingPopupBtn.setIcon(FontAwesome.COGS);
        taskSettingPopupBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);

        toggleTaskField = new ToggleTaskSummaryField(task);
        MHorizontalLayout headerLayout = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(false,
                true, false, false));

        TaskPopupFieldFactory popupFieldFactory = ViewManager.getCacheComponent(TaskPopupFieldFactory.class);
        AbstractComponent priorityField = popupFieldFactory.createPriorityPopupField(task);
        AbstractComponent assigneeField = popupFieldFactory.createAssigneePopupField(task);
        headerLayout.with(taskSettingPopupBtn, priorityField, assigneeField, toggleTaskField).expand(toggleTaskField);

        CssLayout footer = new CssLayout();

        AbstractComponent commentField = popupFieldFactory.createCommentsPopupField(task);
        footer.addComponent(commentField);

        AbstractComponent followerView = popupFieldFactory.createFollowersPopupField(task);
        footer.addComponent(followerView);

        AbstractComponent statusField = popupFieldFactory.createStatusPopupField(task);
        footer.addComponent(statusField);

        AbstractComponent milestoneField = popupFieldFactory.createMilestonePopupField(task);
        footer.addComponent(milestoneField);

        AbstractComponent percentageField = popupFieldFactory.createPercentagePopupField(task);
        footer.addComponent(percentageField);

        String deadlineTooltip = String.format("%s: %s", AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE),
                AppContext.formatDate(task.getDeadline()));
        AbstractComponent deadlineField = popupFieldFactory.createDeadlinePopupField(task);
        deadlineField.setDescription(deadlineTooltip);
        footer.addComponent(deadlineField);

        AbstractComponent startDateField = popupFieldFactory.createStartDatePopupField(task);
        footer.addComponent(startDateField);

        AbstractComponent endDateField = popupFieldFactory.createEndDatePopupField(task);
        footer.addComponent(endDateField);

        if (!SiteConfiguration.isCommunityEdition()) {
            AbstractComponent billableHoursField = popupFieldFactory.createBillableHoursPopupField(task);
            footer.addComponent(billableHoursField);

            AbstractComponent nonBillableHours = popupFieldFactory.createNonBillableHoursPopupField(task);
            footer.addComponent(nonBillableHours);
        }

        this.with(headerLayout, footer);
    }


    private void closeTask() {
        toggleTaskField.closeTask();
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void reOpenTask() {
        toggleTaskField.reOpenTask();
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void deleteTask() {
        IGroupComponent root = UIUtils.getRoot(this, IGroupComponent.class);
        ComponentContainer parent = (ComponentContainer) this.getParent();
        if (parent != null) {
            parent.removeComponent(this);
            if (root != null) {
                ComponentContainer parentRoot = (ComponentContainer) root.getParent();
                if (parentRoot != null && parent.getComponentCount() == 0) {
                    parentRoot.removeComponent(root);
                }
            }
        }
    }

    private OptionPopupContent createPopupContent() {
        OptionPopupContent filterBtnLayout = new OptionPopupContent();

        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            MButton editButton = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
                taskSettingPopupBtn.setPopupVisible(false);
                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(TaskRowRenderer.this, task));
            }).withIcon(FontAwesome.EDIT);
            filterBtnLayout.addOption(editButton);
            filterBtnLayout.addSeparator();
        }

        if (!task.isCompleted()) {
            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                MButton closeBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE), clickEvent -> {
                    task.setStatus(OptionI18nEnum.StatusI18nEnum.Closed.name());
                    task.setPercentagecomplete(100d);
                    ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                    projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                    taskSettingPopupBtn.setPopupVisible(false);
                    closeTask();
                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
                }).withIcon(FontAwesome.CHECK_CIRCLE_O);
                filterBtnLayout.addOption(closeBtn);
            }
        } else {
            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                MButton reOpenBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), clickEvent -> {
                    taskSettingPopupBtn.setPopupVisible(false);
                    task.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
                    task.setPercentagecomplete(0d);

                    ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                    projectTaskService.updateSelectiveWithSession(task, AppContext.getUsername());
                    reOpenTask();
                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
                }).withIcon(FontAwesome.UNLOCK);
                filterBtnLayout.addOption(reOpenBtn);
            }
        }

        filterBtnLayout.addSeparator();

        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.TASKS)) {
            MButton deleteBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                taskSettingPopupBtn.setPopupVisible(false);
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                                projectTaskService.removeWithSession(task, AppContext.getUsername(), AppContext.getAccountId());
                                deleteTask();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O);
            filterBtnLayout.addDangerOption(deleteBtn);
        }

        return filterBtnLayout;
    }
}
