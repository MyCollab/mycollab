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

import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class BugRowRenderer extends MHorizontalLayout {
    private Label bugLinkLbl;
    private SimpleBug bug;

    private PopupButton taskSettingPopupBtn;

    BugRowRenderer(SimpleBug bug) {
        this.bug = bug;
        withSpacing(false).withMargin(false).withWidth("100%").addStyleName("taskrow");
        this.with(createTaskActionControl());

        VerticalLayout wrapBugInfoLayout = new VerticalLayout();
        wrapBugInfoLayout.setSpacing(true);
        bugLinkLbl = new Label(buildBugLink(), ContentMode.HTML);

        if (bug.isCompleted()) {
            bugLinkLbl.addStyleName("completed");
            bugLinkLbl.removeStyleName("overdue pending");
        } else if (bug.isOverdue()) {
            bugLinkLbl.addStyleName("overdue");
            bugLinkLbl.removeStyleName("completed pending");
        }

        bugLinkLbl.addStyleName("wordWrap");
        wrapBugInfoLayout.addComponent(bugLinkLbl);

        MHorizontalLayout footer = new MHorizontalLayout();
        footer.addStyleName(UIConstants.FOOTER_NOTE);
        if (bug.getNumComments() != null && bug.getNumComments() > 0) {
            Div comment = new Div().appendText(FontAwesome.COMMENT_O.getHtml() + " " + bug.getNumComments()).setTitle("Comment");
            footer.addComponent(new ELabel(comment.write(), ContentMode.HTML).withDescription("Comment"));
        }
        if (bug.getStatus() != null) {
            Label statusBtn = new Label(FontAwesome.INFO_CIRCLE.getHtml() + " " + bug.getStatus(), ContentMode.HTML);
            statusBtn.setDescription(AppContext.getMessage(BugI18nEnum.FORM_STATUS));
            footer.addComponent(statusBtn);
        }
        if (bug.getMilestoneid() != null) {
            Label milestoneLbl = new Label(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml() +
                    " " + bug.getMilestoneName(), ContentMode.HTML);
            milestoneLbl.setDescription(AppContext.getMessage(BugI18nEnum.FORM_PHASE));
            footer.addComponent(milestoneLbl);
        }

        if (bug.getDuedate() != null) {
            String deadlineTooltip = String.format("%s: %s", AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE),
                    AppContext.formatDate(bug.getDuedate()));
            Label deadlineBtn = new Label(String.format(" %s %s", FontAwesome.CLOCK_O.getHtml(),
                    AppContext.formatPrettyTime(bug.getDueDateRoundPlusOne())), ContentMode.HTML);
            deadlineBtn.setDescription(deadlineTooltip);
            footer.addComponent(deadlineBtn);
        }

        if (footer.getComponentCount() > 0) {
            wrapBugInfoLayout.addComponent(footer);
        }
        this.with(wrapBugInfoLayout).expand(wrapBugInfoLayout);
    }

    private String buildBugLink() {
        String uid = UUID.randomUUID().toString();
        String priority = bug.getPriority();
        Img priorityLink = new Img(priority, ProjectResources.getIconResourceLink12ByBugPriority(
                (priority))).setTitle(priority);

        String linkName = String.format("[#%d] - %s", bug.getBugkey(), bug.getSummary());
        A taskLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(),
                CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");

        taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.BUG,
                bug.getId() + ""));
        taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));

        String avatarLink = Storage.getAvatarPath(bug.getAssignUserAvatarId(), 16);
        Img avatarImg = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName());

        Div resultDiv = new DivLessFormatter().appendChild(priorityLink, DivLessFormatter.EMPTY_SPACE(),
                avatarImg, DivLessFormatter.EMPTY_SPACE(), taskLink, DivLessFormatter.EMPTY_SPACE(),
                TooltipHelper.buildDivTooltipEnable(uid));
        return resultDiv.write();
    }

    private void closeTask() {
        bugLinkLbl.removeStyleName("overdue");
        bugLinkLbl.addStyleName("completed");
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void reOpenTask() {
        bugLinkLbl.removeStyleName("overdue pending completed");
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void pendingTask() {
        bugLinkLbl.removeStyleName("overdue completed");
        bugLinkLbl.addStyleName("pending");
        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
    }

    private void deleteTask() {
        ComponentContainer parent = (ComponentContainer) this.getParent();
        if (parent != null) {
            parent.removeComponent(this);
        }
    }

    private PopupButton createTaskActionControl() {
        taskSettingPopupBtn = new PopupButton();
        taskSettingPopupBtn.setIcon(FontAwesome.COGS);

        taskSettingPopupBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);

        OptionPopupContent filterBtnLayout = createPopupContent();
        taskSettingPopupBtn.setContent(filterBtnLayout);
        return taskSettingPopupBtn;
    }

    private OptionPopupContent createPopupContent() {
        OptionPopupContent filterBtnLayout = new OptionPopupContent().withWidth("100px");

//        Button editButton = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                taskSettingPopupBtn.setPopupVisible(false);
//                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(TaskRowRenderer.this, bug));
//            }
//        });
//        editButton.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
//        editButton.setIcon(FontAwesome.EDIT);
//        filterBtnLayout.addOption(editButton);
//
//        if (!bug.isCompleted()) {
//            Button closeBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE), new Button.ClickListener() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void buttonClick(Button.ClickEvent event) {
//                    bug.setStatus(OptionI18nEnum.StatusI18nEnum.Closed.name());
//                    bug.setPercentagecomplete(100d);
//                    ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
//                    projectTaskService.updateSelectiveWithSession(bug, AppContext.getUsername());
//                    taskSettingPopupBtn.setPopupVisible(false);
//                    closeTask();
//                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
//                }
//            });
//            closeBtn.setIcon(FontAwesome.CHECK_CIRCLE_O);
//            closeBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
//            filterBtnLayout.addOption(closeBtn);
//        } else {
//            Button reOpenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void buttonClick(Button.ClickEvent event) {
//                    taskSettingPopupBtn.setPopupVisible(false);
//                    bug.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
//                    bug.setPercentagecomplete(0d);
//
//                    ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
//                    projectTaskService.updateSelectiveWithSession(bug, AppContext.getUsername());
//                    reOpenTask();
//                    EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
//                }
//            });
//            reOpenBtn.setIcon(FontAwesome.UNLOCK);
//            reOpenBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
//            filterBtnLayout.addOption(reOpenBtn);
//        }
//
//        if (!OptionI18nEnum.StatusI18nEnum.Pending.name().equals(bug.getStatus())) {
//            if (!OptionI18nEnum.StatusI18nEnum.Closed.name().equals(bug.getStatus())) {
//                Button pendingBtn = new Button(AppContext.getMessage(OptionI18nEnum.StatusI18nEnum.Pending), new Button.ClickListener() {
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public void buttonClick(Button.ClickEvent event) {
//                        taskSettingPopupBtn.setPopupVisible(false);
//                        bug.setStatus(OptionI18nEnum.StatusI18nEnum.Pending.name());
//                        bug.setPercentagecomplete(0d);
//
//                        ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
//                        projectTaskService.updateSelectiveWithSession(bug, AppContext.getUsername());
//                        pendingTask();
//                        EventBusFactory.getInstance().post(new TaskEvent.HasTaskChange(TaskRowRenderer.this, null));
//                    }
//                });
//                pendingBtn.setIcon(FontAwesome.HDD_O);
//                pendingBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
//                filterBtnLayout.addOption(pendingBtn);
//            }
//        } else {
//            Button reOpenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void buttonClick(Button.ClickEvent event) {
//                    taskSettingPopupBtn.setPopupVisible(false);
//                    bug.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
//                    bug.setPercentagecomplete(0d);
//
//                    ProjectTaskService projectTaskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
//                    projectTaskService.updateSelectiveWithSession(bug, AppContext.getUsername());
//
//                    reOpenTask();
//                }
//            });
//            reOpenBtn.setIcon(FontAwesome.UNLOCK);
//            reOpenBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
//            filterBtnLayout.addOption(reOpenBtn);
//        }
//
//        Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                taskSettingPopupBtn.setPopupVisible(false);
//                ConfirmDialogExt.show(UI.getCurrent(),
//                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
//                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
//                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
//                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
//                        new ConfirmDialog.Listener() {
//                            private static final long serialVersionUID = 1L;
//
//                            @Override
//                            public void onClose(ConfirmDialog dialog) {
//                                if (dialog.isConfirmed()) {
//                                    ProjectTaskService projectTaskService = ApplicationContextUtil.
//                                            getSpringBean(ProjectTaskService.class);
//                                    projectTaskService.removeWithSession(bug, AppContext.getUsername(), AppContext.getAccountId());
//                                    deleteTask();
//                                }
//                            }
//                        });
//            }
//        });
//        deleteBtn.setIcon(FontAwesome.TRASH_O);
//        deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.TASKS));
//        filterBtnLayout.addOption(deleteBtn);
        return filterBtnLayout;
    }
}
