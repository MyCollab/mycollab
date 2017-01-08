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

package com.mycollab.module.project.view.user;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.html.DivLessFormatter;
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
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

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
        final MButton sortBtn = new MButton().withIcon(FontAwesome.SORT_ALPHA_ASC).withStyleName(WebThemes.BUTTON_ICON_ONLY);
        sortBtn.addClickListener(clickEvent -> {
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
            setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.WIDGET_MEMBERS_TITLE, memberList.getTotalCount()));
        });
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

        MButton inviteMemberBtn = new MButton(UserUIContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEE), clickEvent ->
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));
        addHeaderElement(inviteMemberBtn);

        memberList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectMemberService.class),
                new MemberRowDisplayHandler(), 7);
        bodyContent.addComponent(memberList);
    }

    void showInformation() {
        searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        searchCriteria.setStatuses(new SetSearchField<>(ProjectMemberStatusConstants.ACTIVE,
                ProjectMemberStatusConstants.NOT_ACCESS_YET));
        searchCriteria.addOrderField(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC));
        showMembers();
    }

    private void showMembers() {
        memberList.setSearchCriteria(searchCriteria);
        this.setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.WIDGET_MEMBERS_TITLE, memberList.getTotalCount()));
    }

    private static class MemberRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(IBeanList<SimpleProjectMember> host, SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withStyleName("list-row");
            Image userAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48);
            userAvatar.addStyleName(UIConstants.CIRCLE_BOX);
            layout.addComponent(userAvatar);

            VerticalLayout content = new VerticalLayout();
            content.addComponent(new ELabel(buildAssigneeValue(member), ContentMode.HTML).withStyleName(UIConstants.TEXT_ELLIPSIS));
            layout.with(content).expand(content);

            CssLayout footer = new CssLayout();

            String roleVal;
            if (member.isProjectOwner()) {
                roleVal = UserUIContext.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY);
            } else {
                roleVal = member.getRoleName();
            }
            ELabel memberRole = ELabel.html(roleVal).withDescription(UserUIContext.getMessage(ProjectRoleI18nEnum.SINGLE))
                    .withStyleName(UIConstants.META_INFO);
            footer.addComponent(memberRole);

            String memberWorksInfo = ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml() + "&nbsp;" +
                    new Span().appendText("" + member.getNumOpenTasks()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_TASKS)) +
                    "&nbsp;&nbsp;" +
                    ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml() + "&nbsp;" +
                    new Span().appendText("" + member.getNumOpenBugs()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_BUGS)) + "&nbsp;&nbsp;"
                    + FontAwesome.MONEY.getHtml() + "&nbsp;" + new Span().appendText("" + NumberUtils.roundDouble(2,
                    member.getTotalBillableLogTime())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)) + "&nbsp;&nbsp;" +
                    FontAwesome.GIFT.getHtml() +
                    "&nbsp;" + new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime()))
                    .setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS));

            ELabel memberWorkStatus = ELabel.html(memberWorksInfo).withStyleName(UIConstants.META_INFO);
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
