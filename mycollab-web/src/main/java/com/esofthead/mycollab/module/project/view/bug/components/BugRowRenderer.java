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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.view.bug.BugPopupFieldFactory;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
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
public class BugRowRenderer extends MVerticalLayout {
    private SimpleBug bug;

    private PopupButton bugSettingPopupBtn;

    BugRowRenderer(final SimpleBug bug) {
        this.bug = bug;
        withSpacing(true).withMargin(false).withWidth("100%").addStyleName("taskrow");

        bugSettingPopupBtn = new PopupButton();
        bugSettingPopupBtn.setIcon(FontAwesome.COGS);
        bugSettingPopupBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        OptionPopupContent filterBtnLayout = createPopupContent();
        bugSettingPopupBtn.setContent(filterBtnLayout);


        final ToogleBugSummaryField bugWrapper = new ToogleBugSummaryField(bug);

        BugPopupFieldFactory popupFieldFactory = ViewManager.getCacheComponent(BugPopupFieldFactory.class);
        MHorizontalLayout headerLayout = new MHorizontalLayout().withWidth("100%").withMargin(new MarginInfo(false,
                true, false, false));
        PopupView priorityField = popupFieldFactory.createBugPriorityPopupField(bug);
        PopupView assigneeField = popupFieldFactory.createBugAssigneePopupField(bug);
        headerLayout.with(bugSettingPopupBtn, priorityField, assigneeField, bugWrapper).expand(bugWrapper);

        CssLayout footer = new CssLayout();

        PopupView commentsField = popupFieldFactory.createBugCommentsPopupField(bug);
        footer.addComponent(commentsField);

        PopupView statusField = popupFieldFactory.createBugStatusPopupField(bug);
        footer.addComponent(statusField);

        PopupView milestoneField = popupFieldFactory.createBugMilestonePopupField(bug);
        footer.addComponent(milestoneField);

        String deadlineTooltip = String.format("%s: %s", AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE),
                AppContext.formatDate(bug.getDuedate()));
        PopupView deadlineField = popupFieldFactory.createBugDeadlinePopupField(bug);
        deadlineField.setDescription(deadlineTooltip);
        footer.addComponent(deadlineField);

        PopupView billableHoursView = popupFieldFactory.createBugBillableHoursPopupField(bug);
        footer.addComponent(billableHoursView);

        PopupView nonBillableHoursView = popupFieldFactory.createBugNonbillableHoursPopupField(bug);
        footer.addComponent(nonBillableHoursView);

        this.with(headerLayout, footer);
    }


    private void deleteBug() {
        ComponentContainer parent = (ComponentContainer) this.getParent();
        if (parent != null) {
            parent.removeComponent(this);
        }
    }

    private OptionPopupContent createPopupContent() {
        OptionPopupContent filterBtnLayout = new OptionPopupContent().withWidth("100px");

        Button editButton = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                bugSettingPopupBtn.setPopupVisible(false);
                EventBusFactory.getInstance().post(new BugEvent.GotoEdit(BugRowRenderer.this, bug));
            }
        });
        editButton.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
        editButton.setIcon(FontAwesome.EDIT);
        filterBtnLayout.addOption(editButton);
        Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                bugSettingPopupBtn.setPopupVisible(false);
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
                                    BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                                    bugService.removeWithSession(bug, AppContext.getUsername(), AppContext.getAccountId());
                                    deleteBug();
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
