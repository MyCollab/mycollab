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

package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.*;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.web.ui.*;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;

import static com.esofthead.mycollab.utils.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMembersWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private DefaultBeanPagedList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> memberList;
    private boolean sortAsc = true;
    private ProjectMemberSearchCriteria searchCriteria;

    public ProjectMembersWidget() {
        super("", new CssLayout());
        final Button sortBtn = new Button();
        sortBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                sortAsc = !sortAsc;
                if (sortAsc) {
                    sortBtn.setIcon(FontAwesome.SORT_ALPHA_ASC);
                    searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC)));
                } else {
                    sortBtn.setIcon(FontAwesome.SORT_ALPHA_DESC);
                    searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("memberFullName",
                            SearchCriteria.DESC)));
                }
                memberList.setSearchCriteria(searchCriteria);
                setTitle(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_MEMBERS_TITLE, memberList.getTotalCount()));
            }
        });
        sortBtn.setIcon(FontAwesome.SORT_ALPHA_ASC);
        sortBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
        addHeaderElement(sortBtn);

        final SearchTextField searchTextField = new SearchTextField() {
            @Override
            public void doSearch(String value) {
                searchCriteria.setMemberFullName(StringSearchField.and(value));
                showMembers();
            }

            @Override
            public void emptySearch() {
                searchCriteria.setMemberFullName(null);
                showMembers();
            }
        };
        searchTextField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        addHeaderElement(searchTextField);

        MButton inviteMemberBtn = new MButton("Invite").withListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null));
            }
        });
        inviteMemberBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));
        inviteMemberBtn.setIcon(FontAwesome.PLUS);
        inviteMemberBtn.addStyleName(UIConstants.BUTTON_LINK);
        addHeaderElement(inviteMemberBtn);

        memberList = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectMemberService.class),
                new MemberRowDisplayHandler(), 7);
        bodyContent.addComponent(memberList);
    }

    public void showInformation() {
        searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        searchCriteria.setStatus(StringSearchField.and(ProjectMemberStatusConstants.ACTIVE));
        searchCriteria.addOrderField(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC));
        showMembers();
    }

    private void showMembers() {
        memberList.setSearchCriteria(searchCriteria);
        this.setTitle(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_MEMBERS_TITLE, memberList.getTotalCount()));
    }

    public static class MemberRowDisplayHandler implements AbstractBeanPagedList.RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%").withStyleName("list-row");
            Image userAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48);
            userAvatar.addStyleName(UIConstants.CIRCLE_BOX);
            layout.addComponent(userAvatar);

            VerticalLayout content = new VerticalLayout();
            content.addComponent(new ELabel(buildAssigneeValue(member), ContentMode.HTML).withStyleName(UIConstants.TEXT_ELLIPSIS));
            layout.with(content).expand(content);

            CssLayout footer = new CssLayout();

            String roleVal;
            if (member.isProjectOwner()) {
                roleVal = AppContext.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY);
            } else {
                roleVal = member.getRoleName();
            }
            ELabel memberRole = new ELabel(roleVal, ContentMode.HTML).withDescription("Role").withStyleName(UIConstants.LABEL_META_INFO);
            footer.addComponent(memberRole);

            String memberWorksInfo = ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml() + "&nbsp;" + new Span
                    ().appendText("" + member.getNumOpenTasks()).setTitle("Open tasks") + "&nbsp;&nbsp;" + ProjectAssetsManager.getAsset
                    (ProjectTypeConstants.BUG).getHtml() + "&nbsp;" + new Span().appendText("" + member.getNumOpenBugs())
                    .setTitle("Open bugs") + "&nbsp;&nbsp;"
                    + FontAwesome.MONEY.getHtml() + "&nbsp;" + new Span().appendText("" + NumberUtils.roundDouble(2,
                    member.getTotalBillableLogTime())).setTitle("Billable hours") + "&nbsp;&nbsp;" + FontAwesome.GIFT.getHtml() +
                    "&nbsp;" + new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime())).setTitle("Non billable hours");

            ELabel memberWorkStatus = new ELabel(memberWorksInfo, ContentMode.HTML).withStyleName(UIConstants.LABEL_META_INFO);
            footer.addComponent(memberWorkStatus);

            content.addComponent(footer);
            return layout;
        }

        private String buildAssigneeValue(SimpleProjectMember member) {
            Div div = new DivLessFormatter();
            A userLink = new A().setId("tag" + TOOLTIP_ID).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    member.getProjectid(), member.getUsername()));

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(member.getUsername()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            userLink.appendText(member.getMemberFullName());

            if (member.getUsername().equals(CurrentProjectVariables.getProject().getLead())) {
                userLink.appendText(" (Lead)");
            }

            return div.appendChild(userLink).write();
        }
    }
}
