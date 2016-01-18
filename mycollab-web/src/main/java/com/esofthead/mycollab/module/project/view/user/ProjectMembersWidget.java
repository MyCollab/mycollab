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
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.web.ui.Depot;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMembersWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private DefaultBeanPagedList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> memberList;

    public ProjectMembersWidget() {
        super("", new CssLayout());

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
                new MemberRowDisplayHandler());
        bodyContent.addComponent(memberList);
    }

    public void showInformation() {
        ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        searchCriteria.setStatus(StringSearchField.and(ProjectMemberStatusConstants.ACTIVE));
        memberList.setSearchCriteria(searchCriteria);
        this.setTitle(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_MEMBERS_TITLE, memberList.getTotalCount()));
    }

    public static class MemberRowDisplayHandler implements AbstractBeanPagedList.RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%").withStyleName("list-row");
            layout.addComponent(UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48));

            VerticalLayout content = new VerticalLayout();
            content.addComponent(new Label(buildAssigneeValue(member), ContentMode.HTML));
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
            String uid = UUID.randomUUID().toString();
            Div div = new DivLessFormatter();
            A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    member.getProjectid(), member.getUsername()));

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, member.getUsername()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            userLink.appendText(member.getMemberFullName());

            return div.appendChild(userLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid)).write();
        }
    }
}
