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
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.AccountLinkBuilder;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.layouts.MHorizontalLayout;

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
        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        List<SimpleUser> userAccountList = userService
                .findPagableListByCriteria(new SearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));

        this.removeAllComponents();
        MHorizontalLayout header = new MHorizontalLayout().withSpacing(false)
                .withMargin(new MarginInfo(true, false, true, false)).withStyleName(UIConstants.HEADER_VIEW)
                .withWidth("100%");
        Button createBtn = new Button("Invite user",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new UserEvent.GotoAdd(this, null));
                    }
                });
        createBtn.setEnabled(AppContext
                .canWrite(RolePermissionCollections.ACCOUNT_USER));
        createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createBtn.setIcon(FontAwesome.PLUS);

        header.with(createBtn).withAlign(createBtn, Alignment.MIDDLE_RIGHT);
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
        MHorizontalLayout blockTop = new MHorizontalLayout();
        Image memberAvatar = UserAvatarControlFactory
                .createUserAvatarEmbeddedComponent(member.getAvatarid(), 100);
        blockTop.addComponent(memberAvatar);

        VerticalLayout memberInfo = new VerticalLayout();

        MHorizontalLayout layoutButtonDelete = new MHorizontalLayout().withWidth("100%");
        layoutButtonDelete.setVisible(AppContext
                .canWrite(RolePermissionCollections.ACCOUNT_USER));

        Button deleteBtn = new Button();
        deleteBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialogExt.show(
                        UI.getCurrent(),
                        AppContext.getMessage(
                                GenericI18Enum.DIALOG_DELETE_TITLE,
                                SiteConfiguration.getSiteName()),
                        AppContext
                                .getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    UserService userService = ApplicationContextUtil
                                            .getSpringBean(UserService.class);
                                    userService.pendingUserAccounts(Arrays
                                                    .asList(member
                                                            .getUsername()),
                                            AppContext.getAccountId());
                                    EventBusFactory
                                            .getInstance()
                                            .post(new UserEvent.GotoList(
                                                    UserListViewImpl.this, null));
                                }
                            }
                        });
            }
        });
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        layoutButtonDelete.with(deleteBtn).withAlign(deleteBtn, Alignment.MIDDLE_RIGHT);

        memberInfo.addComponent(layoutButtonDelete);

        ButtonLink userAccountLink = new ButtonLink(member.getDisplayName());
        userAccountLink.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(
                        new UserEvent.GotoRead(UserListViewImpl.this, member
                                .getUsername()));
            }
        });

        memberInfo.addComponent(userAccountLink);

        Label memberEmailLabel = new Label("<a href='mailto:"
                + member.getUsername() + "'>" + member.getUsername() + "</a>",
                ContentMode.HTML);
        memberEmailLabel.addStyleName("member-email");
        memberEmailLabel.setWidth("100%");
        memberInfo.addComponent(memberEmailLabel);

        ELabel memberSinceLabel = new ELabel("Member since: "
                + AppContext.formatPrettyTime(member.getRegisteredtime())).withDescription(AppContext.formatDateTime
                (member.getRegisteredtime()));
        memberSinceLabel.addStyleName("member-email");
        memberSinceLabel.setWidth("100%");
        memberInfo.addComponent(memberSinceLabel);

        if (RegisterStatusConstants.SENT_VERIFICATION_EMAIL.equals(member
                .getRegisterstatus())) {
            final VerticalLayout waitingNotLayout = new VerticalLayout();
            Label infoStatus = new Label("Waiting for accept invitation");
            infoStatus.addStyleName("member-email");
            waitingNotLayout.addComponent(infoStatus);

            ButtonLink resendInvitationLink = new ButtonLink(
                    "Resend Invitation", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    UserService userService = ApplicationContextUtil
                            .getSpringBean(UserService.class);
                    userService.updateUserAccountStatus(
                            member.getUsername(),
                            member.getAccountId(),
                            RegisterStatusConstants.VERIFICATING);
                    waitingNotLayout.removeAllComponents();
                    Label statusEmail = new Label(
                            "Sending invitation email");
                    statusEmail.addStyleName("member-email");
                    waitingNotLayout.addComponent(statusEmail);
                }
            });
            resendInvitationLink.addStyleName("member-email");
            waitingNotLayout.addComponent(resendInvitationLink);
            memberInfo.addComponent(waitingNotLayout);
        } else if (RegisterStatusConstants.ACTIVE.equals(member
                .getRegisterstatus())) {
            ELabel lastAccessTimeLbl = new ELabel("Logged in "
                    + AppContext.formatPrettyTime(member.getLastaccessedtime())).withDescription(AppContext
                    .formatDateTime(member.getLastaccessedtime()));
            lastAccessTimeLbl.addStyleName("member-email");
            memberInfo.addComponent(lastAccessTimeLbl);
        } else if (RegisterStatusConstants.VERIFICATING.equals(member
                .getRegisterstatus())) {
            Label infoStatus = new Label("Sending invitation email");
            infoStatus.addStyleName("member-email");
            memberInfo.addComponent(infoStatus);
        }

        blockTop.addComponent(memberInfo);
        blockTop.setExpandRatio(memberInfo, 1.0f);
        blockTop.setWidth("100%");
        blockContent.addComponent(blockTop);

        if (member.getRoleid() != null) {
            String memberRoleLinkPrefix = "<a href=\""
                    + AccountLinkBuilder.generatePreviewFullRoleLink(member
                    .getRoleid()) + "\"";
            Label memberRole = new Label();
            memberRole.setContentMode(ContentMode.HTML);
            if (member.getIsAccountOwner() != null
                    && member.getIsAccountOwner()) {
                memberRole.setValue(memberRoleLinkPrefix
                        + "style=\"color: #B00000;\">" + "Account Owner"
                        + "</a>");
            } else {
                memberRole.setValue(memberRoleLinkPrefix
                        + "style=\"color:gray;font-size:12px;\">"
                        + member.getRoleName() + "</a>");
            }
            memberRole.setSizeUndefined();
            blockContent.addComponent(memberRole);
            blockContent.setComponentAlignment(memberRole,
                    Alignment.MIDDLE_RIGHT);
        } else if (member.getIsAccountOwner() != null
                && member.getIsAccountOwner() == Boolean.TRUE) {
            Label memberRole = new Label();
            memberRole.setContentMode(ContentMode.HTML);
            memberRole.setValue("<a style=\"color: #B00000;\">Account Owner</a>");
            memberRole.setSizeUndefined();
            blockContent.addComponent(memberRole);
            blockContent.setComponentAlignment(memberRole,
                    Alignment.MIDDLE_RIGHT);
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
