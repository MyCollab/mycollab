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
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMembersWidget extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private Label titleLbl;
    private DefaultBeanPagedList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> memberList;

    public ProjectMembersWidget() {
        withSpacing(false).withMargin(new MarginInfo(true, false, true, false));

        MButton inviteMemberBtn = new MButton("+").withStyleName("add-project-btn").withListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(
                        new ProjectMemberEvent.GotoInviteMembers(this, null));
            }
        });
        inviteMemberBtn.setWidth("20px");
        inviteMemberBtn.setHeight("20px");

        titleLbl = new Label();
        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, true,
                false, true)).withHeight("34px").withWidth("100%").with(titleLbl, inviteMemberBtn).withAlign(titleLbl, Alignment
                .MIDDLE_CENTER).withAlign(inviteMemberBtn, Alignment.MIDDLE_CENTER).expand(titleLbl);
        header.addStyleName("panel-header");

        memberList = new DefaultBeanPagedList<>(
                ApplicationContextUtil.getSpringBean(ProjectMemberService.class),
                new MemberRowDisplayHandler());
        this.with(header, memberList);
    }

    public void showInformation() {
        ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        searchCriteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE));
        memberList.setSearchCriteria(searchCriteria);
        titleLbl.setValue(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_MEMBERS_TITLE, memberList.getTotalCount()));
    }

    public static class MemberRowDisplayHandler implements
            AbstractBeanPagedList.RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(final SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%").withStyleName("list-row");
            layout.addStyleName("odd");
            layout.addComponent(new Image(null, UserAvatarControlFactory
                    .createAvatarResource(member.getMemberAvatarId(), 48)));

            VerticalLayout content = new VerticalLayout();
            content.addComponent(new Label(buildAssigneeValue(member), ContentMode.HTML));
            layout.with(content).expand(content);

            CssLayout footer = new CssLayout();
            footer.setStyleName("activity-date");

            Label memberRole = new Label();
            memberRole.setContentMode(ContentMode.HTML);
            String joinDateMsg;
            if (member.isAdmin()) {
                joinDateMsg = AppContext.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY);
            } else {
                joinDateMsg = member.getRoleName();
            }
            joinDateMsg += AppContext.getMessage(
                    ProjectMemberI18nEnum.OPT_MEMBER_JOIN_DATE, AppContext.formatPrettyTime(member.getJoindate()));
            memberRole.setValue(joinDateMsg);
            memberRole.setDescription(AppContext.formatDateTime(member.getJoindate()));

            footer.addComponent(memberRole);
            content.addComponent(footer);
            return layout;
        }

        private String buildAssigneeValue(SimpleProjectMember member) {
            String uid = UUID.randomUUID().toString();
            Div div = new DivLessFormatter();
            A userLink = new A();
            userLink.setId("tag" + uid);
            userLink.setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    member.getProjectid(),
                    member.getUsername()));

            userLink.setAttribute("onmouseover", TooltipHelper.buildUserHtmlTooltip(uid, member.getUsername()));
            userLink.appendText(member.getMemberFullName());

            return div.appendChild(userLink,
                    DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid)).write();
        }
    }
}
