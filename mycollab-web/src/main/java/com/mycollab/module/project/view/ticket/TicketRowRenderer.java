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
package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.milestone.ToggleTicketSummaryField;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class TicketRowRenderer extends MVerticalLayout {
    private ProjectTicket assignment;

    private ToggleTicketSummaryField toggleTaskField;

    public TicketRowRenderer(final ProjectTicket ticket) {
        this.assignment = ticket;
        withMargin(false).withFullWidth().addStyleName(WebUIConstants.BORDER_LIST_ROW);

//        taskSettingPopupBtn = new PopupButton();
//        taskSettingPopupBtn.setIcon(FontAwesome.COGS);
//        taskSettingPopupBtn.addStyleName(WebUIConstants.BUTTON_ICON_ONLY);
//        OptionPopupContent filterBtnLayout = createPopupContent();
//        taskSettingPopupBtn.setContent(filterBtnLayout);

        toggleTaskField = new ToggleTicketSummaryField(ticket);
        MHorizontalLayout headerLayout = new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset
                (ticket.getType())).withWidthUndefined(), toggleTaskField).expand(toggleTaskField).withFullWidth()
                .withMargin(new MarginInfo(false, true, false, false));

        TicketComponentFactory popupFieldFactory = AppContextUtil.getSpringBean(TicketComponentFactory.class);
        AbstractComponent assigneeField = popupFieldFactory.createAssigneePopupField(ticket);
        headerLayout.with(assigneeField, toggleTaskField).expand(toggleTaskField);

        CssLayout footer = new CssLayout();
        footer.addComponent(popupFieldFactory.createCommentsPopupField(ticket));
        footer.addComponent(popupFieldFactory.createPriorityPopupField(ticket));
        footer.addComponent(popupFieldFactory.createFollowersPopupField(ticket));
        footer.addComponent(popupFieldFactory.createStatusPopupField(ticket));
        footer.addComponent(popupFieldFactory.createStartDatePopupField(ticket));
        footer.addComponent(popupFieldFactory.createEndDatePopupField(ticket));
        footer.addComponent(popupFieldFactory.createDueDatePopupField(ticket));
//
//        AbstractComponent milestoneField = popupFieldFactory.createMilestonePopupField(assignment);
//        footer.addComponent(milestoneField);
//
//        String deadlineTooltip = String.format("%s: %s", UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE),
//                UserUIContext.formatDate(assignment.getDeadline()));
//        AbstractComponent deadlineField = popupFieldFactory.createDeadlinePopupField(assignment);
//        deadlineField.setDescription(deadlineTooltip);
//        footer.addComponent(deadlineField);
//
//        AbstractComponent startDateField = popupFieldFactory.createStartDatePopupField(assignment);
//        footer.addComponent(startDateField);
//
//        AbstractComponent endDateField = popupFieldFactory.createEndDatePopupField(assignment);
//        footer.addComponent(endDateField);
//
//        if (!SiteConfiguration.isCommunityEdition()) {
//            AbstractComponent billableHoursField = popupFieldFactory.createBillableHoursPopupField(assignment);
//            footer.addComponent(billableHoursField);
//
//            AbstractComponent nonBillableHours = popupFieldFactory.createNonBillableHoursPopupField(assignment);
//            footer.addComponent(nonBillableHours);
//        }

        this.with(headerLayout, footer);
    }


//    private void closeTask() {
//        toggleTaskField.closeTask();
//        OptionPopupContent filterBtnLayout = createPopupContent();
//        taskSettingPopupBtn.setContent(filterBtnLayout);
//    }
//
//    private void reOpenTask() {
//        toggleTaskField.reOpenTask();
//        OptionPopupContent filterBtnLayout = createPopupContent();
//        taskSettingPopupBtn.setContent(filterBtnLayout);
//    }
//
//    private void deleteTask() {
//        IGroupComponent root = UIUtils.getRoot(this, IGroupComponent.class);
//        ComponentContainer parent = (ComponentContainer) this.getParent();
//        if (parent != null) {
//            parent.removeComponent(this);
//            if (root != null) {
//                ComponentContainer parentRoot = (ComponentContainer) root.getParent();
//                if (parentRoot != null && parent.getComponentCount() == 0) {
//                    parentRoot.removeComponent(root);
//                }
//            }
//        }
//    }

//    private OptionPopupContent createPopupContent() {
//        OptionPopupContent filterBtnLayout = new OptionPopupContent();
//
//        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
//            MButton editButton = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
//                taskSettingPopupBtn.setPopupVisible(false);
//                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(TicketRowRenderer.this, assignment));
//            }).withIcon(FontAwesome.EDIT);
//            filterBtnLayout.addOption(editButton);
//            filterBtnLayout.addSeparator();
//        }
//
//        if (!assignment.isCompleted()) {
//            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
//                MButton closeBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLOSE), clickEvent -> {
//                    assignment.setStatus(OptionI18nEnum.StatusI18nEnum.Closed.name());
//                    assignment.setPercentagecomplete(100d);
//                    ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
//                    projectTaskService.updateSelectiveWithSession(assignment, UserUIContext.getUsername());
//                    taskSettingPopupBtn.setPopupVisible(false);
//                    closeTask();
//                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TicketRowRenderer.this, null));
//                }).withIcon(FontAwesome.CHECK_CIRCLE_O);
//                filterBtnLayout.addOption(closeBtn);
//            }
//        } else {
//            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
//                MButton reOpenBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN), clickEvent -> {
//                    taskSettingPopupBtn.setPopupVisible(false);
//                    assignment.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
//                    assignment.setPercentagecomplete(0d);
//
//                    ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
//                    projectTaskService.updateSelectiveWithSession(assignment, UserUIContext.getUsername());
//                    reOpenTask();
//                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TicketRowRenderer.this, null));
//                }).withIcon(FontAwesome.UNLOCK);
//                filterBtnLayout.addOption(reOpenBtn);
//            }
//        }
//
//        filterBtnLayout.addSeparator();
//
//        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.TASKS)) {
//            MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
//                taskSettingPopupBtn.setPopupVisible(false);
//                ConfirmDialogExt.show(UI.getCurrent(),
//                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
//                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
//                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
//                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
//                        confirmDialog -> {
//                            if (confirmDialog.isConfirmed()) {
//                                ProjectTaskService projectTaskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
//                                projectTaskService.removeWithSession(assignment, UserUIContext.getUsername(), MyCollabUI.getAccountId());
//                                deleteTask();
//                            }
//                        });
//            }).withIcon(FontAwesome.TRASH_O);
//            filterBtnLayout.addDangerOption(deleteBtn);
//        }
//
//        return filterBtnLayout;
//    }
}
