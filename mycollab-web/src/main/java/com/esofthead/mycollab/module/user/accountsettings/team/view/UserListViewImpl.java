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
import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.AccountLinkBuilder;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.UserTableFieldDef;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.esb.SendUserInvitationEvent;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.ReportStreamSource;
import com.esofthead.mycollab.reporting.RpFieldsBuilder;
import com.esofthead.mycollab.reporting.SimpleReportTemplateExecutor;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.SearchTextField;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.google.common.eventbus.AsyncEventBus;
import com.hp.gagawa.java.elements.A;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserListViewImpl extends AbstractPageView implements UserListView {
    private static final long serialVersionUID = 1L;

    private CssLayout contentLayout;
    private UserSearchCriteria searchCriteria;
    private boolean sortAsc = true;
    private HeaderWithFontAwesome headerText;

    public UserListViewImpl() {
        super();
        this.setMargin(new MarginInfo(false, true, false, true));
        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withFullWidth();
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

        headerText = HeaderWithFontAwesome.h2(FontAwesome.USERS, AppContext.getMessage(UserI18nEnum.VIEW_LIST_TITLE, 0));

        final Button sortBtn = new Button();
        sortBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                sortAsc = !sortAsc;
                if (sortAsc) {
                    sortBtn.setIcon(FontAwesome.SORT_ALPHA_ASC);
                    displayUsers();
                } else {
                    sortBtn.setIcon(FontAwesome.SORT_ALPHA_DESC);
                    displayUsers();
                }
            }
        });
        sortBtn.setIcon(FontAwesome.SORT_ALPHA_ASC);
        sortBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
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

        MButton exportPdfBtn = new MButton("").withIcon(FontAwesome.FILE_PDF_O).withStyleName(UIConstants.BUTTON_OPTION)
                .withDescription("Export to PDF");
        FileDownloader pdfFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.PDF));
        pdfFileDownloader.extend(exportPdfBtn);

        MButton exportExcelBtn = new MButton("").withIcon(FontAwesome.FILE_EXCEL_O).withStyleName(UIConstants.BUTTON_OPTION).withDescription("Export to Excel");
        FileDownloader excelFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.EXCEL));
        excelFileDownloader.extend(exportExcelBtn);

        ButtonGroup exportButtonGroup = new ButtonGroup();
        exportButtonGroup.addButton(exportPdfBtn);
        exportButtonGroup.addButton(exportExcelBtn);

        header.with(headerText, sortBtn, searchTextField, exportButtonGroup, createBtn).alignAll(Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);

        contentLayout = new CssLayout();
        contentLayout.setWidth("100%");
        this.addComponent(contentLayout);
    }

    @SuppressWarnings("unchecked")
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
        List<SimpleUser> userAccountList = userService.findPagableListByCriteria(new BasicSearchRequest<>(searchCriteria, 0,
                Integer.MAX_VALUE));
        headerText.updateTitle(AppContext.getMessage(UserI18nEnum.VIEW_LIST_TITLE, userAccountList.size()));

        for (SimpleUser userAccount : userAccountList) {
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
        buttonControls.setVisible(AppContext.canWrite(RolePermissionCollections.ACCOUNT_USER));

        if (RegisterStatusConstants.NOT_LOG_IN_YET.equals(member.getRegisterstatus())) {
            Button resendBtn = new Button(AppContext.getMessage(UserI18nEnum.ACTION_RESEND_INVITATION), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    SendUserInvitationEvent invitationEvent = new SendUserInvitationEvent(member.getUsername(), null,
                            member.getInviteUser(), AppContext.getSubDomain(), AppContext.getAccountId());
                    AsyncEventBus asyncEventBus = AppContextUtil.getSpringBean(AsyncEventBus.class);
                    asyncEventBus.post(invitationEvent);
                    NotificationUtil.showNotification("Success!", "The invitation is sent to " + member.getDisplayName() + " successfully");
                }
            });
            resendBtn.addStyleName(UIConstants.BUTTON_LINK);
            buttonControls.with(resendBtn);
        }

        Button editBtn = new Button("", FontAwesome.EDIT);
        editBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new UserEvent.GotoEdit(UserListViewImpl.this, member));
            }
        });
        editBtn.addStyleName(UIConstants.BUTTON_LINK);
        buttonControls.with(editBtn);

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
                                    UserService userService = AppContextUtil.getSpringBean(UserService.class);
                                    userService.pendingUserAccounts(Collections.singletonList(member.getUsername()),
                                            AppContext.getAccountId());
                                    EventBusFactory.getInstance().post(new UserEvent.GotoList(UserListViewImpl.this, null));
                                }
                            }
                        });
            }
        });
        deleteBtn.setIcon(FontAwesome.TRASH_O);
        deleteBtn.addStyleName(UIConstants.BUTTON_LINK);
        buttonControls.with(deleteBtn);

        memberInfo.addComponent(buttonControls);
        memberInfo.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);

        A memberLink = new A(AccountLinkGenerator.generatePreviewFullUserLink(AppContext.getSiteUrl(),
                member.getUsername())).appendText(member.getDisplayName());
        ELabel memberLinkLbl = ELabel.h3(memberLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
        memberInfo.addComponent(memberLinkLbl);
        memberInfo.addComponent(ELabel.hr());

        if (member.getRoleid() != null) {
            String memberRoleLinkPrefix = String.format("<a href=\"%s\"", AccountLinkBuilder.generatePreviewFullRoleLink(member.getRoleid()));
            ELabel memberRole = new ELabel(ContentMode.HTML).withStyleName(UIConstants.TEXT_ELLIPSIS);
            if (Boolean.TRUE.equals(member.getIsAccountOwner())) {
                memberRole.setValue(String.format("%sstyle=\"color: #B00000;\">Account Owner</a>", memberRoleLinkPrefix));
            } else {
                memberRole.setValue(String.format("%sstyle=\"color:gray;font-size:12px;\">%s</a>",
                        memberRoleLinkPrefix, member.getRoleName()));
            }
            memberInfo.addComponent(memberRole);
        } else if (Boolean.TRUE.equals(member.getIsAccountOwner())) {
            Label memberRole = new Label("<a style=\"color: #B00000;\">Account Owner</a>", ContentMode.HTML);
            memberInfo.addComponent(memberRole);
        } else {
            Label lbl = new Label();
            lbl.setHeight("10px");
            memberInfo.addComponent(lbl);
        }

        if (Boolean.TRUE.equals(AppContext.showEmailPublicly())) {
            Label memberEmailLabel = new ELabel(String.format("<a href='mailto:%s'>%s</a>", member.getUsername(),
                    member.getUsername()), ContentMode.HTML).withStyleName(UIConstants.TEXT_ELLIPSIS, UIConstants
                    .LABEL_META_INFO).withFullWidth();
            memberInfo.addComponent(memberEmailLabel);
        }

        ELabel memberSinceLabel = new ELabel("Member since: " + AppContext.formatPrettyTime(member.getRegisteredtime()))
                .withDescription(AppContext.formatDateTime(member.getRegisteredtime())).withStyleName(UIConstants
                        .LABEL_META_INFO).withFullWidth();
        memberInfo.addComponent(memberSinceLabel);

        ELabel lastAccessTimeLbl = new ELabel("Logged in " + AppContext.formatPrettyTime(member.getLastaccessedtime()))
                .withDescription(AppContext.formatDateTime(member.getLastaccessedtime()));
        lastAccessTimeLbl.addStyleName(UIConstants.LABEL_META_INFO);
        memberInfo.addComponent(lastAccessTimeLbl);
        blockTop.with(memberInfo).expand(memberInfo);
        blockContent.addComponent(blockTop);
        return blockContent;
    }

    private StreamResource buildStreamSource(ReportExportType exportType) {
        List fields = Arrays.asList(UserTableFieldDef.username(), UserTableFieldDef.rolename(),
                UserTableFieldDef.email(), UserTableFieldDef.birthday(),
                UserTableFieldDef.officephone(), UserTableFieldDef.homephone(), UserTableFieldDef.company());
        SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>("Users",
                new RpFieldsBuilder(fields), exportType, SimpleUser.class, AppContextUtil.getSpringBean
                (UserService.class));
        ReportStreamSource streamSource = new ReportStreamSource(reportTemplateExecutor) {
            @Override
            protected void initReportParameters(Map<String, Object> parameters) {
                parameters.put(SimpleReportTemplateExecutor.CRITERIA, searchCriteria);
            }
        };
        return new StreamResource(streamSource, exportType.getDefaultFileName());
    }
}
