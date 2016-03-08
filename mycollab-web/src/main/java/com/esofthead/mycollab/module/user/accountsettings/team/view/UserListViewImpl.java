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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.AccountLinkBuilder;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.web.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserListViewImpl extends AbstractPageView implements UserListView {
    private static final long serialVersionUID = 1L;

    public UserListViewImpl() {
        super();
        this.setMargin(new MarginInfo(false, true, false, true));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSearchCriteria(UserSearchCriteria searchCriteria) {
        this.removeAllComponents();
        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        List<SimpleUser> userAccountList = userService
                .findPagableListByCriteria(new SearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));

        MHorizontalLayout header = new MHorizontalLayout().withSpacing(false).withMargin(new MarginInfo(true, false, true, false))
                .withStyleName(UIConstants.HEADER_VIEW).withWidth("100%");
        Button createBtn = new Button("Invite user", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new UserEvent.GotoAdd(this, null));
            }
        });
        createBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.ACCOUNT_USER));
        createBtn.setStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setIcon(FontAwesome.PLUS);

        HeaderWithFontAwesome headerLbl = HeaderWithFontAwesome.h2(FontAwesome.USERS, "Users");

        header.with(headerLbl, createBtn).expand(headerLbl).withAlign(createBtn, Alignment.MIDDLE_RIGHT);
        this.addComponent(header);

        CssLayout contentLayout = new CssLayout();
        contentLayout.setWidth("100%");
        for (SimpleUser userAccount : userAccountList) {
            contentLayout.addComponent(generateMemberBlock(userAccount));
        }
        this.addComponent(contentLayout);
    }

    private Component generateMemberBlock(final SimpleUser member) {
        CssLayout memberBlock = new CssLayout();
        memberBlock.addStyleName("member-block");

        VerticalLayout blockContent = new VerticalLayout();
        MHorizontalLayout blockTop = new MHorizontalLayout().withWidth("100%");
        Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getAvatarid(), 100);
        blockTop.addComponent(memberAvatar);

        MVerticalLayout memberInfo = new MVerticalLayout().withMargin(false);

        MHorizontalLayout buttonControls = new MHorizontalLayout();
        buttonControls.setVisible(AppContext.canWrite(RolePermissionCollections.ACCOUNT_USER));

        Button editBtn = new Button("", FontAwesome.EDIT);
        editBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new UserEvent.GotoEdit(UserListViewImpl.this, member));
            }
        });
        editBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);

        Button deleteBtn = new Button();
        deleteBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
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
                                    UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
                                    userService.pendingUserAccounts(Arrays.asList(member.getUsername()),
                                            AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new UserEvent.GotoList(UserListViewImpl.this, null));
                                }
                            }
                        });
            }
        });
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        buttonControls.with(editBtn, deleteBtn);

        memberInfo.addComponent(buttonControls);
        memberInfo.setComponentAlignment(buttonControls, Alignment.TOP_RIGHT);

        A memberLink = new A(AccountLinkGenerator.generatePreviewFullUserLink(AppContext.getSiteUrl(),
                member.getUsername())).appendText(member.getDisplayName());
        ELabel memberLinkLbl = new ELabel(memberLink.write(), ContentMode.HTML).withStyleName(ValoTheme.LABEL_H3);
        memberLinkLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        memberInfo.addComponent(memberLinkLbl);
        memberInfo.addComponent(ELabel.Hr());

        Label memberEmailLabel = new Label(String.format("<a href='mailto:%s'>%s</a>", member.getUsername(),
                member.getUsername()), ContentMode.HTML);
        memberEmailLabel.addStyleName(UIConstants.LABEL_META_INFO);
        memberEmailLabel.setWidth("100%");
        memberInfo.addComponent(memberEmailLabel);

        ELabel memberSinceLabel = new ELabel("Member since: " + AppContext.formatPrettyTime(member.getRegisteredtime()))
                .withDescription(AppContext.formatDateTime(member.getRegisteredtime()));
        memberSinceLabel.addStyleName(UIConstants.LABEL_META_INFO);
        memberSinceLabel.setWidth("100%");
        memberInfo.addComponent(memberSinceLabel);

        if (RegisterStatusConstants.SENT_VERIFICATION_EMAIL.equals(member.getRegisterstatus())) {
            final VerticalLayout waitingNotLayout = new VerticalLayout();
            Label infoStatus = new Label("Waiting for accept invitation");
            infoStatus.addStyleName(UIConstants.LABEL_META_INFO);
            waitingNotLayout.addComponent(infoStatus);

            ButtonLink resendInvitationLink = new ButtonLink("Resend Invitation", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    ExtMailService mailService = ApplicationContextUtil.getSpringBean(ExtMailService.class);
                    if (!mailService.isMailSetupValid()) {
                        UI.getCurrent().addWindow(new GetStartedInstructionWindow(member));
                    } else {
                        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
                        userService.updateUserAccountStatus(member.getUsername(), member.getAccountId(),
                                RegisterStatusConstants.VERIFICATING);
                        waitingNotLayout.removeAllComponents();
                        Label statusEmail = new Label("Sending invitation email");
                        statusEmail.addStyleName(UIConstants.LABEL_META_INFO);
                        waitingNotLayout.addComponent(statusEmail);
                    }
                }
            });
            resendInvitationLink.addStyleName(UIConstants.BUTTON_LINK);
            waitingNotLayout.addComponent(resendInvitationLink);
            memberInfo.addComponent(waitingNotLayout);
        } else if (RegisterStatusConstants.ACTIVE.equals(member.getRegisterstatus())) {
            ELabel lastAccessTimeLbl = new ELabel("Logged in "
                    + AppContext.formatPrettyTime(member.getLastaccessedtime())).withDescription(AppContext
                    .formatDateTime(member.getLastaccessedtime()));
            lastAccessTimeLbl.addStyleName(UIConstants.LABEL_META_INFO);
            memberInfo.addComponent(lastAccessTimeLbl);
        } else if (RegisterStatusConstants.VERIFICATING.equals(member.getRegisterstatus())) {
            Label infoStatus = new Label("Sending invitation email");
            infoStatus.addStyleName(UIConstants.LABEL_META_INFO);
            memberInfo.addComponent(infoStatus);
        }

        blockTop.with(memberInfo).expand(memberInfo);
        blockContent.addComponent(blockTop);

        if (member.getRoleid() != null) {
            String memberRoleLinkPrefix = String.format("<a href=\"%s\"", AccountLinkBuilder.generatePreviewFullRoleLink(member.getRoleid()));
            Label memberRole = new Label();
            memberRole.setContentMode(ContentMode.HTML);
            if (Boolean.TRUE.equals(member.getIsAccountOwner())) {
                memberRole.setValue(String.format("%sstyle=\"color: #B00000;\">Account Owner</a>", memberRoleLinkPrefix));
            } else {
                memberRole.setValue(String.format("%sstyle=\"color:gray;font-size:12px;\">%s</a>",
                        memberRoleLinkPrefix, member.getRoleName()));
            }
            memberRole.setSizeUndefined();
            blockContent.addComponent(memberRole);
            blockContent.setComponentAlignment(memberRole, Alignment.MIDDLE_RIGHT);
        } else if (Boolean.TRUE.equals(member.getIsAccountOwner())) {
            Label memberRole = new Label("<a style=\"color: #B00000;\">Account Owner</a>", ContentMode.HTML);
            memberRole.setSizeUndefined();
            blockContent.addComponent(memberRole);
            blockContent.setComponentAlignment(memberRole, Alignment.MIDDLE_RIGHT);
        } else {
            Label lbl = new Label();
            lbl.setHeight("10px");
            blockContent.addComponent(lbl);
        }
        blockContent.setWidth("100%");
        memberBlock.addComponent(blockContent);
        return memberBlock;
    }
}
