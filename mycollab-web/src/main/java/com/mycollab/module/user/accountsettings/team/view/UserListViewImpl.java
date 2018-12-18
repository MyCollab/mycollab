/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.google.common.eventbus.AsyncEventBus;
import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.esb.SendUserInvitationEvent;
import com.mycollab.module.user.event.UserEvent;
import com.mycollab.module.user.service.UserService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.SearchTextField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserListViewImpl extends AbstractVerticalPageView implements UserListView {
    private static final long serialVersionUID = 1L;

    private CssLayout contentLayout;
    private UserSearchCriteria searchCriteria;
    private boolean sortAsc = true;
    private HeaderWithIcon headerText;

    public UserListViewImpl() {
        this.setMargin(new MarginInfo(false, true, false, true));
        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withFullWidth();
        MButton createBtn = new MButton(UserUIContext.getMessage(UserI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new UserEvent.GotoAdd(this, null)))
                .withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.ACCOUNT_USER));

        headerText = HeaderWithIcon.h2(VaadinIcons.USERS, UserUIContext.getMessage(UserI18nEnum.LIST) + " " +
                UserUIContext.getMessage(GenericI18Enum.OPT_TOTAL_VALUE, 0));

        final MButton sortBtn = new MButton().withIcon(VaadinIcons.CARET_UP).withStyleName(WebThemes.BUTTON_ICON_ONLY);
        sortBtn.addClickListener(clickEvent -> {
            sortAsc = !sortAsc;
            if (sortAsc) {
                sortBtn.setIcon(VaadinIcons.CARET_UP);
                displayUsers();
            } else {
                sortBtn.setIcon(VaadinIcons.CARET_DOWN);
                displayUsers();
            }
        });
        header.addComponent(sortBtn);

        final SearchTextField searchTextField = new SearchTextField() {
            @Override
            public void doSearch(String value) {
                searchCriteria.setDisplayName(StringSearchField.and(value));
                displayUsers();
            }

            @Override
            public void emptySearch() {
                searchCriteria.setDisplayName(null);
                displayUsers();
            }
        };
        searchTextField.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        MButton printBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(new UserCustomizeReportOutputWindow(
                new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return searchCriteria;
                    }
                }))).withIcon(VaadinIcons.PRINT).withStyleName(WebThemes.BUTTON_OPTION)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));

        header.with(headerText, sortBtn, searchTextField, printBtn, createBtn).alignAll(Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);

        contentLayout = new CssLayout();
        contentLayout.setWidth("100%");
        this.addComponent(contentLayout);
    }

    @Override
    public void setSearchCriteria(UserSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        displayUsers();
    }

    private void displayUsers() {
        contentLayout.removeAllComponents();
        if (sortAsc) {
            searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("displayName", SearchCriteria.ASC)));
        } else {
            searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("displayName", SearchCriteria.DESC)));
        }

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        List<SimpleUser> users = (List<SimpleUser>) userService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
        headerText.updateTitle(String.format("%s %s", UserUIContext.getMessage(UserI18nEnum.LIST), UserUIContext.getMessage(GenericI18Enum.OPT_TOTAL_VALUE, users.size())));

        for (SimpleUser userAccount : users) {
            contentLayout.addComponent(generateMemberBlock(userAccount));
        }
    }

    private Component generateMemberBlock(final SimpleUser member) {
        VerticalLayout blockContent = new VerticalLayout();
        blockContent.setWidth("350px");
        blockContent.setStyleName("member-block");
        if (RegisterStatusConstants.NOT_LOG_IN_YET.equals(member.getRegisterstatus())) {
            blockContent.addStyleName("inactive");
        }
        MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
        Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getAvatarid(), 100);
        memberAvatar.addStyleName(UIConstants.CIRCLE_BOX);
        blockTop.addComponent(memberAvatar);

        MVerticalLayout memberInfo = new MVerticalLayout().withMargin(false);

        MHorizontalLayout buttonControls = new MHorizontalLayout();
        buttonControls.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
        buttonControls.setVisible(UserUIContext.canWrite(RolePermissionCollections.ACCOUNT_USER));

        if (RegisterStatusConstants.NOT_LOG_IN_YET.equals(member.getRegisterstatus())) {
            MButton resendBtn = new MButton(UserUIContext.getMessage(UserI18nEnum.ACTION_RESEND_INVITATION), clickEvent -> {
                SendUserInvitationEvent invitationEvent = new SendUserInvitationEvent(member.getUsername(), null,
                        member.getInviteUser(), AppUI.getSubDomain(), AppUI.getAccountId());
                AsyncEventBus asyncEventBus = AppContextUtil.getSpringBean(AsyncEventBus.class);
                asyncEventBus.post(invitationEvent);
                NotificationUtil.showNotification(UserUIContext.getMessage(GenericI18Enum.OPT_SUCCESS), UserUIContext
                        .getMessage(UserI18nEnum.OPT_SEND_INVITATION_SUCCESSFULLY, member.getDisplayName()));
            }).withStyleName(WebThemes.BUTTON_LINK);
            buttonControls.with(resendBtn);
        }

        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new UserEvent.GotoEdit(UserListViewImpl.this, member)))
                .withIcon(VaadinIcons.EDIT).withStyleName(WebThemes.BUTTON_LINK);
        buttonControls.with(editBtn);

        MButton deleteBtn = new MButton("", clickEvent ->
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                UserService userService = AppContextUtil.getSpringBean(UserService.class);
                                userService.pendingUserAccounts(Collections.singletonList(member.getUsername()), AppUI.getAccountId());
                                EventBusFactory.getInstance().post(new UserEvent.GotoList(UserListViewImpl.this, null));
                            }
                        })
        ).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_LINK);
        buttonControls.with(deleteBtn);

        memberInfo.addComponent(buttonControls);
        memberInfo.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);

        A memberLink = new A(AccountLinkGenerator.generateUserLink(
                member.getUsername())).appendText(member.getDisplayName());
        ELabel memberLinkLbl = ELabel.h3(memberLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
        memberInfo.addComponent(memberLinkLbl);
        memberInfo.addComponent(ELabel.hr());

        if (member.getRoleid() != null) {
            String memberRoleLinkPrefix = String.format("<a href=\"%s\"", AccountLinkGenerator.generateRoleLink(member.getRoleid()));
            ELabel memberRole = new ELabel(ContentMode.HTML).withStyleName(UIConstants.TEXT_ELLIPSIS);
            if (Boolean.TRUE.equals(member.isAccountOwner())) {
                memberRole.setValue(String.format("%sstyle=\"color: #B00000;\">%s</a>", memberRoleLinkPrefix,
                        UserUIContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER)));
            } else {
                memberRole.setValue(String.format("%sstyle=\"color:gray;font-size:12px;\">%s</a>",
                        memberRoleLinkPrefix, member.getRoleName()));
            }
            memberInfo.addComponent(memberRole);
        } else if (Boolean.TRUE.equals(member.isAccountOwner())) {
            Label memberRole = new Label(String.format("<a style=\"color: #B00000;\">%s</a>", UserUIContext.getMessage
                    (RoleI18nEnum.OPT_ACCOUNT_OWNER)), ContentMode.HTML);
            memberInfo.addComponent(memberRole);
        } else {
            Label lbl = new Label();
            lbl.setHeight("10px");
            memberInfo.addComponent(lbl);
        }

        if (Boolean.TRUE.equals(AppUI.showEmailPublicly())) {
            Label memberEmailLabel = ELabel.html(String.format("<a href='mailto:%s'>%s</a>", member.getUsername(), member.getUsername()))
                    .withStyleName(UIConstants.TEXT_ELLIPSIS, UIConstants.META_INFO).withFullWidth();
            memberInfo.addComponent(memberEmailLabel);
        }

        ELabel memberSinceLabel = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_SINCE, UserUIContext.formatPrettyTime(member.getRegisteredtime())))
                .withDescription(UserUIContext.formatDateTime(member.getRegisteredtime())).withFullWidth();
        memberInfo.addComponent(memberSinceLabel);

        ELabel lastAccessTimeLbl = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_LOGGED_IN, UserUIContext
                .formatPrettyTime(member.getLastaccessedtime()))).withDescription(UserUIContext.formatDateTime(member.getLastaccessedtime()));
        memberInfo.addComponent(lastAccessTimeLbl);
        blockTop.with(memberInfo).expand(memberInfo);
        blockContent.addComponent(blockTop);
        return blockContent;
    }
}
