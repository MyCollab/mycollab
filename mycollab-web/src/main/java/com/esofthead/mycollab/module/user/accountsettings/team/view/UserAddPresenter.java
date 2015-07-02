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
package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.LinkUtils;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.B;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_USER, impliedPermissionVal = AccessPermissionFlag.READ_WRITE)
public class UserAddPresenter extends AbstractPresenter<UserAddView> {
    private static final long serialVersionUID = 1L;

    public UserAddPresenter() {
        super(UserAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(
                new EditFormHandler<SimpleUser>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onSave(final SimpleUser item) {
                        save(item);
                        ExtMailService mailService = ApplicationContextUtil.getSpringBean(ExtMailService.class);
                        if (mailService.isMailSetupValid()) {
                            ViewState viewState = HistoryViewManager.back();
                            if (viewState instanceof NullViewState) {
                                EventBusFactory.getInstance().post(
                                        new UserEvent.GotoList(this, null));
                            }
                        } else {
                            UI.getCurrent().addWindow(new GetStartedInstructionWindow(item));
                        }

                    }

                    @Override
                    public void onCancel() {
                        ViewState viewState = HistoryViewManager.back();
                        if (viewState instanceof NullViewState) {
                            EventBusFactory.getInstance().post(
                                    new UserEvent.GotoList(this, null));
                        }
                    }

                    @Override
                    public void onSaveAndNew(final SimpleUser item) {
                        save(item);
                        EventBusFactory.getInstance().post(new UserEvent.GotoAdd(this, null));
                    }
                });
    }

    public void save(SimpleUser item) {
        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        item.setAccountId(AppContext.getAccountId());

        if (item.getStatus() == null) {
            item.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST);
        }

        if (item.getUsername() == null) {
            userService.saveUserAccount(item, AppContext.getAccountId(),
                    AppContext.getUsername());
        } else {
            userService.updateUserAccount(item, AppContext.getAccountId());
        }

    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        UserContainer userContainer = (UserContainer) container;
        userContainer.removeAllComponents();
        userContainer.addComponent(view.getWidget());

        SimpleUser user = (SimpleUser) data.getParams();
        if (user.getUsername() != null) {
            view.editItem(user, false);
        } else {
            view.editItem(user);
        }

        AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);

        if (user.getUsername() == null) {
            breadcrumb.gotoUserAdd();
        } else {
            breadcrumb.gotoUserEdit(user);
        }
    }

    private static class GetStartedInstructionWindow extends Window {
        private MVerticalLayout contentLayout;

        public GetStartedInstructionWindow(SimpleUser user) {
            super("Getting started instructions");
            this.setResizable(false);
            this.setModal(true);
            contentLayout = new MVerticalLayout().withWidth("600px");
            this.setContent(contentLayout);
            center();
            displayInfo(user);
        }

        private void displayInfo(SimpleUser user) {
            Div infoDiv = new Div().appendText("You have not setup SMTP account properly. So we can not send the invitation by email automatically. Please copy/paste below paragraph and inform to the user by yourself").setStyle("font-weight:bold;color:red");
            Label infoLbl = new Label(infoDiv.write(), ContentMode.HTML);

            Div userInfoDiv = new Div().appendText("Your username is ").appendChild(new B().appendText(user.getEmail()));
            Label userInfoLbl = new Label(userInfoDiv.write(), ContentMode.HTML);

            Div roleInfoDiv = new Div().appendText("Your role is ").appendChild(new B().appendText(user.getRoleName()));
            Label roleInfoLbl = new Label(roleInfoDiv.write(), ContentMode.HTML);
            contentLayout.with(infoLbl, userInfoLbl, roleInfoLbl);

            String acceptLinkVal = LinkUtils.generateUserAcceptLink(AppContext.getSubDomain(), AppContext.getAccountId(), user.getUsername());
            Div acceptLinkDiv = new Div().appendText("Accept: ").appendChild(new A().setHref(acceptLinkVal).appendText(acceptLinkVal));
            Label acceptLink = new Label(acceptLinkDiv.write(), ContentMode.HTML);

            String denyLinkVal = LinkUtils.generateUserDenyLink(AppContext.getSubDomain(), AppContext.getAccountId(), user.getUsername(), AppContext.getUserDisplayName(), AppContext.getUsername());
            Div denyLinkDiv = new Div().appendText("Deny: ").appendChild(new A().setHref(denyLinkVal).appendText(denyLinkVal));
            Label denyLink = new Label(denyLinkDiv.write(), ContentMode.HTML);
            contentLayout.with(acceptLink, denyLink);

            final MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
            final Button addNewBtn = new Button("Create another user", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(
                        final Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new UserEvent.GotoAdd(GetStartedInstructionWindow.this, null));
                    GetStartedInstructionWindow.this.close();
                }
            });
            addNewBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
            Button doneBtn = new Button("Done", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(
                        final Button.ClickEvent event) {
                    ViewState viewState = HistoryViewManager.back();
                    if (viewState instanceof NullViewState) {
                        EventBusFactory.getInstance().post(
                                new UserEvent.GotoList(this, null));
                    }
                    GetStartedInstructionWindow.this.close();
                }
            });
            doneBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            controlsBtn.with(addNewBtn, doneBtn);
            contentLayout.with(controlsBtn).withAlign(controlsBtn,
                    Alignment.MIDDLE_RIGHT);
        }
    }
}