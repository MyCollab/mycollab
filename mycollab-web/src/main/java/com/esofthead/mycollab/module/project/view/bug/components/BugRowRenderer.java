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
import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.teemu.VaadinIcons;

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
            Label statusBtn = new Label(FontAwesome.INFO_CIRCLE.getHtml() + " " + AppContext.getMessage(OptionI18nEnum.BugStatus
                    .class, bug.getStatus()), ContentMode.HTML);
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
            Label deadlineBtn = new Label(String.format(" %s %s", VaadinIcons.USER_CLOCK.getHtml(),
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

    private void deleteBug() {
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

        Button editButton = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                taskSettingPopupBtn.setPopupVisible(false);
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
        filterBtnLayout.addOption(deleteBtn);
        return filterBtnLayout;
    }
}
