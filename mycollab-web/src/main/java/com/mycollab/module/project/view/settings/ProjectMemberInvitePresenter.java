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
package com.mycollab.module.project.view.settings;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.B;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.event.ProjectMemberEvent.InviteProjectMembers;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.shell.view.SystemUIChecker;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PageView.ViewListener;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewEvent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberInvitePresenter extends AbstractPresenter<ProjectMemberInviteView> {
    private static final long serialVersionUID = 1L;

    public ProjectMemberInvitePresenter() {
        super(ProjectMemberInviteView.class);
    }

    @Override
    protected void postInitView() {
        view.addViewListener(new ViewListener<ProjectMemberEvent.InviteProjectMembers>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void receiveEvent(ViewEvent<InviteProjectMembers> event) {
                InviteProjectMembers inviteMembers = (InviteProjectMembers) event.getData();
                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                Collection<String> inviteEmails = inviteMembers.getEmails();
                if (CollectionUtils.isNotEmpty(inviteEmails)) {
                    projectMemberService.inviteProjectMembers(inviteEmails.toArray(new String[inviteEmails.size()]),
                            CurrentProjectVariables.getProjectId(), inviteMembers.getRoleId(),
                            UserUIContext.getUsername(), inviteMembers.getInviteMessage(), MyCollabUI.getAccountId());

                    ExtMailService mailService = AppContextUtil.getSpringBean(ExtMailService.class);
                    if (mailService.isMailSetupValid()) {
                        NotificationUtil.showNotification(UserUIContext.getMessage(ProjectMemberI18nEnum.OPT_INVITATION_SENT_SUCCESSFULLY),
                                UserUIContext.getMessage(GenericI18Enum.HELP_SPAM_FILTER_PREVENT_MESSAGE),
                                Notification.Type.HUMANIZED_MESSAGE);
                        EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                    } else {
                        UI.getCurrent().addWindow(new CanSendEmailInstructionWindow(inviteMembers));
                    }
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS)) {
            ProjectUserContainer userGroupContainer = (ProjectUserContainer) container;
            userGroupContainer.setContent(view);

            view.display();

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoUserAdd();
            SystemUIChecker.hasValidSmtpAccount();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private static class CanSendEmailInstructionWindow extends MWindow {
        private MVerticalLayout contentLayout;

        CanSendEmailInstructionWindow(InviteProjectMembers invitation) {
            super(UserUIContext.getMessage(ShellI18nEnum.OPT_SMTP_INSTRUCTIONS));
            this.withResizable(false).withModal(true).withWidth("600px").withCenter();
            contentLayout = new MVerticalLayout();
            this.setContent(contentLayout);
            displayInfo(invitation);
        }

        private void displayInfo(InviteProjectMembers invitation) {
            Div infoDiv = new Div().appendText(UserUIContext.getMessage(ProjectMemberI18nEnum.OPT_NO_SMTP_SEND_MEMBERS))
                    .setStyle("font-weight:bold;color:red");
            contentLayout.with(ELabel.html(infoDiv.write()));

            Div introDiv = new Div().appendText("Below users are invited to the project ")
                    .appendChild(new B().appendText(CurrentProjectVariables.getProject().getName()))
                    .appendText(" as role ").appendChild(new B().appendText(invitation.getRoleName()));
            contentLayout.with(ELabel.html(introDiv.write()));

            MVerticalLayout linksContainer = new MVerticalLayout().withStyleName(WebThemes.SCROLLABLE_CONTAINER);
            new Restrain(linksContainer).setMaxHeight("400px");
            contentLayout.with(linksContainer);

            Collection<String> inviteEmails = invitation.getEmails();
            for (String inviteEmail : inviteEmails) {
                Div userEmailDiv = new Div().appendText(String.format("&nbsp;&nbsp;&nbsp;&nbsp;%s %s: ", FontAwesome.MAIL_FORWARD.getHtml(),
                        UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL))).appendChild(new A().setHref("mailto:" + inviteEmail)
                        .appendText(inviteEmail));
                linksContainer.with(ELabel.html(userEmailDiv.write()), ELabel.hr());
            }

            MButton addNewBtn = new MButton(UserUIContext.getMessage(ProjectMemberI18nEnum.ACTION_INVITE_MORE_MEMBERS), clickEvent -> {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(CanSendEmailInstructionWindow.this, null));
                close();
            }).withStyleName(WebThemes.BUTTON_ACTION);

            MButton doneBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_DONE), clickEvent -> {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                close();
            }).withStyleName(WebThemes.BUTTON_ACTION);

            MHorizontalLayout controlsBtn = new MHorizontalLayout(addNewBtn, doneBtn).withMargin(true);
            contentLayout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
        }
    }
}
