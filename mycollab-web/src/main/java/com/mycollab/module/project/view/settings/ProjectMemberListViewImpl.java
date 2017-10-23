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
package com.mycollab.module.project.view.settings;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.GenericLinkUtils;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.module.project.*;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.SearchTextField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
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
public class ProjectMemberListViewImpl extends AbstractVerticalPageView implements ProjectMemberListView {
    private static final long serialVersionUID = 1L;
    private CssLayout contentLayout;
    private HeaderWithFontAwesome headerText;
    private boolean sortAsc = true;
    private ProjectMemberSearchCriteria searchCriteria;

    public ProjectMemberListViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));
        MHorizontalLayout viewHeader = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false)).withFullWidth();
        viewHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        headerText = ComponentUtils.headerH2(ProjectTypeConstants.MEMBER, UserUIContext.getMessage(ProjectMemberI18nEnum.LIST));
        viewHeader.with(headerText).expand(headerText);

        final MButton sortBtn = new MButton().withIcon(FontAwesome.SORT_ALPHA_ASC).withStyleName(WebThemes.BUTTON_ICON_ONLY);
        sortBtn.addClickListener(clickEvent -> {
            sortAsc = !sortAsc;
            if (sortAsc) {
                sortBtn.setIcon(FontAwesome.SORT_ALPHA_ASC);
                displayMembers();
            } else {
                sortBtn.setIcon(FontAwesome.SORT_ALPHA_DESC);
                displayMembers();
            }
        });
        viewHeader.addComponent(sortBtn);

        final SearchTextField searchTextField = new SearchTextField() {
            @Override
            public void doSearch(String value) {
                searchCriteria.setMemberFullName(StringSearchField.and(value));
                displayMembers();
            }

            @Override
            public void emptySearch() {
                searchCriteria.setMemberFullName(null);
                displayMembers();
            }
        };
        searchTextField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        viewHeader.addComponent(searchTextField);

        MButton printBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(new ProjectMemberCustomizeReportOutputWindow(new LazyValueInjector() {
            @Override
            protected Object doEval() {
                return searchCriteria;
            }
        }))).withIcon(FontAwesome.PRINT).withStyleName(WebThemes.BUTTON_OPTION).withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        viewHeader.addComponent(printBtn);

        MButton createBtn = new MButton(UserUIContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES),
                clickEvent -> EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null)))
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(FontAwesome.SEND);
        createBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));
        viewHeader.addComponent(createBtn);

        addComponent(viewHeader);

        contentLayout = new CssLayout();
        contentLayout.setWidth("100%");
        addComponent(contentLayout);
    }

    @Override
    public void setSearchCriteria(ProjectMemberSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        displayMembers();
    }

    private void displayMembers() {
        contentLayout.removeAllComponents();
        if (sortAsc) {
            searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC)));
        } else {
            searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("memberFullName",
                    SearchCriteria.DESC)));
        }
        ProjectMemberService prjMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleProjectMember> memberLists = (List<SimpleProjectMember>) prjMemberService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));

        headerText.updateTitle(String.format("%s (%d)", UserUIContext.getMessage(ProjectMemberI18nEnum.LIST), memberLists.size()));
        memberLists.forEach(member -> contentLayout.addComponent(generateMemberBlock(member)));
    }

    private Component generateMemberBlock(final SimpleProjectMember member) {
        MHorizontalLayout blockContent = new MHorizontalLayout().withSpacing(false)
                .withStyleName("member-block").withWidth("350px");
        blockContent.setStyleName("member-block");
        if (ProjectMemberStatusConstants.NOT_ACCESS_YET.equals(member.getStatus())) {
            blockContent.addStyleName("inactive");
        }

        Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 100);
        memberAvatar.addStyleName(UIConstants.CIRCLE_BOX);
        memberAvatar.setWidthUndefined();
        blockContent.addComponent(memberAvatar);

        MVerticalLayout blockTop = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true)).withFullWidth();

        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoEdit(this, member)))
                .withIcon(FontAwesome.EDIT).withStyleName(WebThemes.BUTTON_LINK)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));
        editBtn.setDescription("Edit user '" + member.getDisplayName() + "' information");

        MButton deleteBtn = new MButton("", clickEvent -> {
            ConfirmDialogExt.show(UI.getCurrent(),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                    confirmDialog -> {
                        if (confirmDialog.isConfirmed()) {
                            ProjectMemberService prjMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                            prjMemberService.removeWithSession(member, UserUIContext.getUsername(), AppUI.getAccountId());
                            EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(ProjectMemberListViewImpl.this, CurrentProjectVariables.getProjectId()));
                        }
                    });
        }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_LINK)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS))
                .withDescription("Remove user '" + member.getDisplayName() + "' out of this project");

        MHorizontalLayout buttonControls = new MHorizontalLayout(editBtn, deleteBtn);
        blockTop.addComponent(buttonControls);
        blockTop.setComponentAlignment(buttonControls, Alignment.TOP_RIGHT);

        A memberLink = new A(ProjectLinkGenerator.generateProjectMemberLink(member.getProjectid(), member
                .getUsername())).appendText(member.getMemberFullName()).setTitle(member.getMemberFullName());
        ELabel memberNameLbl = ELabel.h3(memberLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS).withFullWidth();

        blockTop.with(memberNameLbl, ELabel.hr());

        String roleLink = String.format("<a href=\"%s%s%s\"", AppUI.getSiteUrl(), GenericLinkUtils.URL_PREFIX_PARAM,
                ProjectLinkGenerator.generateRolePreviewLink(member.getProjectid(), member.getProjectroleid()));
        ELabel memberRole = new ELabel("", ContentMode.HTML).withFullWidth().withStyleName(UIConstants.TEXT_ELLIPSIS);
        if (member.isProjectOwner()) {
            memberRole.setValue(String.format("%sstyle=\"color: #B00000;\">%s</a>", roleLink, UserUIContext.getMessage
                    (ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY)));
        } else {
            memberRole.setValue(String.format("%sstyle=\"color:gray;font-size:12px;\">%s</a>", roleLink, member.getRoleName()));
        }
        blockTop.addComponent(memberRole);

        if (Boolean.TRUE.equals(AppUI.showEmailPublicly())) {
            Label memberEmailLabel = ELabel.html(String.format("<a href='mailto:%s'>%s</a>", member.getUsername(), member.getUsername()))
                    .withStyleName(UIConstants.META_INFO).withFullWidth();
            blockTop.addComponent(memberEmailLabel);
        }

        ELabel memberSinceLabel = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_SINCE,
                UserUIContext.formatPrettyTime(member.getJoindate()))).withDescription(UserUIContext.formatDateTime(member.getJoindate()))
                .withFullWidth();
        blockTop.addComponent(memberSinceLabel);

        if (ProjectMemberStatusConstants.ACTIVE.equals(member.getStatus())) {
            ELabel lastAccessTimeLbl = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_LOGGED_IN, UserUIContext
                    .formatPrettyTime(member.getLastAccessTime())))
                    .withDescription(UserUIContext.formatDateTime(member.getLastAccessTime()));
            blockTop.addComponent(lastAccessTimeLbl);
        }

        String memberWorksInfo = ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml() + " " + new Span()
                .appendText("" + member.getNumOpenTasks()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_TASKS)) +
                "  " + ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml() + " " + new Span()
                .appendText("" + member.getNumOpenBugs()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_BUGS)) +
                " " + FontAwesome.MONEY.getHtml() + " " + new Span().appendText("" + NumberUtils.roundDouble(2,
                member.getTotalBillableLogTime())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)) +
                "  " + FontAwesome.GIFT.getHtml() +
                " " + new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime()))
                .setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS));

        blockTop.addComponent(ELabel.html(memberWorksInfo).withStyleName(UIConstants.META_INFO));

        blockContent.with(blockTop);
        blockContent.setExpandRatio(blockTop, 1.0f);
        return blockContent;
    }
}