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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.dao.ProjectMemberMapper;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ComponentUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberListViewImpl extends AbstractPageView implements ProjectMemberListView {
    private static final long serialVersionUID = 1L;
    private CssLayout contentLayout;

    public ProjectMemberListViewImpl() {
        super();
        this.setMargin(new MarginInfo(false, true, false, true));
        MHorizontalLayout viewHeader = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withWidth("100%").withStyleName("hdr-view");
        viewHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        HeaderWithFontAwesome headerText = ComponentUtils.headerH2(ProjectTypeConstants.MEMBER,
                AppContext.getMessage(ProjectMemberI18nEnum.VIEW_LIST_TITLE));

        viewHeader.with(headerText).expand(headerText);

        Button createBtn = new Button(AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null));
            }
        });
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));
        createBtn.setStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setIcon(FontAwesome.SEND);

        viewHeader.addComponent(createBtn);

        this.addComponent(viewHeader);

        contentLayout = new CssLayout();
        contentLayout.setWidth("100%");

        this.addComponent(contentLayout);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSearchCriteria(ProjectMemberSearchCriteria searchCriteria) {
        contentLayout.removeAllComponents();
        ProjectMemberService prjMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
        List<SimpleProjectMember> memberLists = prjMemberService
                .findPagableListByCriteria(new SearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));

        for (SimpleProjectMember member : memberLists) {
            contentLayout.addComponent(generateMemberBlock(member));
        }
    }

    private Component generateMemberBlock(final SimpleProjectMember member) {
        CssLayout memberBlock = new CssLayout();
        memberBlock.addStyleName("member-block");

        VerticalLayout blockContent = new VerticalLayout();
        MHorizontalLayout blockTop = new MHorizontalLayout().withWidth("100%");
        Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 100);
        blockTop.addComponent(memberAvatar);


        MHorizontalLayout buttonControls = new MHorizontalLayout();
        Button editBtn = new Button("", FontAwesome.EDIT);
        editBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoEdit(ProjectMemberListViewImpl.this, member));
            }
        });
        editBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));
        editBtn.setDescription("Edit user '" + member.getDisplayName() + "' information");
        editBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        blockContent.addComponent(editBtn);
        blockContent.setComponentAlignment(editBtn, Alignment.TOP_RIGHT);

        Button deleteBtn = new Button("", FontAwesome.TRASH_O);
        deleteBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
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
                                    ProjectMemberService prjMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
                                    prjMemberService.removeWithSession(member, AppContext.getUsername(), AppContext.getAccountId());

                                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(ProjectMemberListViewImpl.this, null));
                                }
                            }
                        });
            }
        });
        deleteBtn.setDescription("Remove user '" + member.getDisplayName() + "' out of this project");
        deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        deleteBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));

        buttonControls.with(editBtn, deleteBtn);
        blockContent.addComponent(buttonControls);
        blockContent.setComponentAlignment(buttonControls, Alignment.TOP_RIGHT);

        A memberLink = new A(ProjectLinkBuilder.generateProjectMemberFullLink(member.getProjectid(), member
                .getUsername())).appendText(member.getMemberFullName());
        ELabel memberNameLbl = new ELabel(memberLink.write(), ContentMode.HTML).withStyleName(ValoTheme.LABEL_H3);
        memberNameLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        memberNameLbl.setWidth("100%");

        MVerticalLayout memberInfo = new MVerticalLayout().withMargin(false);
        memberInfo.addComponent(memberNameLbl);
        memberInfo.addComponent(new Hr());

        String roleLink = String.format("<a href=\"%s%s%s\"", AppContext.getSiteUrl(), GenericLinkUtils.URL_PREFIX_PARAM,
                ProjectLinkGenerator.generateRolePreviewLink(member.getProjectid(), member.getProjectRoleId()));
        Label memberRole = new Label();
        memberRole.setContentMode(ContentMode.HTML);
        if (member.isProjectOwner()) {
            memberRole.setValue(roleLink + "style=\"color: #B00000;\">" + "Project Owner" + "</a>");
        } else {
            memberRole.setValue(roleLink + "style=\"color:gray;font-size:12px;\">" + member.getRoleName() + "</a>");
        }
        memberRole.setSizeUndefined();
        memberInfo.addComponent(memberRole);

        Label memberEmailLabel = new Label(String.format("<a href='mailto:%s'>%s</a>", member.getUsername(),
                member.getUsername()), ContentMode.HTML);
        memberEmailLabel.addStyleName(UIConstants.LABEL_META_INFO);
        memberEmailLabel.setWidth("100%");
        memberInfo.addComponent(memberEmailLabel);

        ELabel memberSinceLabel = new ELabel(String.format("Member since: %s", AppContext.formatPrettyTime(member.getJoindate())))
                .withDescription(AppContext.formatDateTime(member.getJoindate()));
        memberSinceLabel.addStyleName(UIConstants.LABEL_META_INFO);
        memberSinceLabel.setWidth("100%");
        memberInfo.addComponent(memberSinceLabel);

        if (RegisterStatusConstants.SENT_VERIFICATION_EMAIL.equals(member.getStatus())) {
            final VerticalLayout waitingNotLayout = new VerticalLayout();
            Label infoStatus = new Label(AppContext.getMessage(ProjectMemberI18nEnum.WAITING_ACCEPT_INVITATION));
            waitingNotLayout.addComponent(infoStatus);

            ButtonLink resendInvitationLink = new ButtonLink(AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_RESEND_INVITATION), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    ProjectMemberMapper projectMemberMapper = ApplicationContextUtil.getSpringBean(ProjectMemberMapper.class);
                    member.setStatus(RegisterStatusConstants.VERIFICATING);
                    projectMemberMapper.updateByPrimaryKeySelective(member);
                    waitingNotLayout.removeAllComponents();
                    Label statusEmail = new Label(AppContext.getMessage(ProjectMemberI18nEnum.SENDING_EMAIL_INVITATION));
                    statusEmail.addStyleName(UIConstants.LABEL_META_INFO);
                    waitingNotLayout.addComponent(statusEmail);
                }
            });
            resendInvitationLink.setStyleName(UIConstants.BUTTON_LINK);
            resendInvitationLink.addStyleName("member-email");
            waitingNotLayout.addComponent(resendInvitationLink);
            memberInfo.addComponent(waitingNotLayout);
        } else if (RegisterStatusConstants.ACTIVE.equals(member.getStatus())) {
            ELabel lastAccessTimeLbl = new ELabel(String.format("Logged in %s", AppContext.formatPrettyTime(member.getLastAccessTime())))
                    .withDescription(AppContext.formatDateTime(member.getLastAccessTime()));
            lastAccessTimeLbl.addStyleName(UIConstants.LABEL_META_INFO);
            memberInfo.addComponent(lastAccessTimeLbl);
        } else if (RegisterStatusConstants.VERIFICATING.equals(member.getStatus())) {
            Label infoStatus = new Label(AppContext.getMessage(ProjectMemberI18nEnum.SENDING_EMAIL_INVITATION));
            memberInfo.addComponent(infoStatus);
        }

        String memberWorksInfo = ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml() + " " + new Span
                ().appendText("" + member.getNumOpenTasks()).setTitle("Open tasks") + "  " + ProjectAssetsManager.getAsset
                (ProjectTypeConstants.BUG).getHtml() + " " + new Span().appendText("" + member.getNumOpenBugs())
                .setTitle("Open bugs") + " " +
                " " + FontAwesome.MONEY.getHtml() + " " + new Span().appendText("" + NumberUtils.roundDouble(2,
                member.getTotalBillableLogTime())).setTitle("Billable hours") + "  " + FontAwesome.GIFT.getHtml() +
                " " + new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime())).setTitle("Non billable hours");

        Label memberWorkStatus = new Label(memberWorksInfo, ContentMode.HTML);
        memberWorkStatus.addStyleName(UIConstants.LABEL_META_INFO);
        memberInfo.addComponent(memberWorkStatus);
        memberInfo.setWidth("100%");

        blockTop.addComponent(memberInfo);
        blockTop.setExpandRatio(memberInfo, 1.0f);
        blockContent.addComponent(blockTop);

        blockContent.setWidth("100%");

        memberBlock.addComponent(blockContent);
        return memberBlock;
    }
}