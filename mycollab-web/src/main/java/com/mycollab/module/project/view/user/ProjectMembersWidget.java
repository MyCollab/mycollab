/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.user;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectMemberStatusConstants;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.SearchTextField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectMembersWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private DefaultBeanPagedList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> memberList;
    private boolean sortAsc = true;
    private ProjectMemberSearchCriteria searchCriteria;

    ProjectMembersWidget() {
        super("", new CssLayout());
        MButton sortBtn = new MButton().withIcon(VaadinIcons.CARET_UP).withStyleName(WebThemes.BUTTON_ICON_ONLY);
        sortBtn.addClickListener(clickEvent -> {
            sortAsc = !sortAsc;
            if (sortAsc) {
                sortBtn.setIcon(VaadinIcons.CARET_UP);
                searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("memberFullName", SearchCriteria.ASC)));
            } else {
                sortBtn.setIcon(VaadinIcons.CARET_DOWN);
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

        memberList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectMemberService.class),
                new MemberRowDisplayHandler(), 7);
        bodyContent.addComponent(memberList);
    }

    void showInformation() {
        searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
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
            userAvatar.addStyleName(WebThemes.CIRCLE_BOX);
            layout.addComponent(userAvatar);

            MVerticalLayout content = new MVerticalLayout().withMargin(false);
            content.addComponent(ELabel.html(buildAssigneeValue(member)).withStyleName(WebThemes.TEXT_ELLIPSIS));
            layout.with(content).expand(content);

            CssLayout footer = new CssLayout();

            String roleVal = member.getRoleName() + "&nbsp;&nbsp;";
            ELabel memberRole = ELabel.html(roleVal).withDescription(UserUIContext.getMessage(ProjectRoleI18nEnum.SINGLE))
                    .withStyleName(WebThemes.META_INFO);
            footer.addComponent(memberRole);

            String memberWorksInfo = ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml() +
                    new Span().appendText("" + member.getNumOpenTasks()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_TASKS)) +
                    "&nbsp;&nbsp;" +
                    ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml() +
                    new Span().appendText("" + member.getNumOpenBugs()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_BUGS)) + "&nbsp;&nbsp;"
                    + VaadinIcons.MONEY.getHtml() + "&nbsp;" + new Span().appendText("" + NumberUtils.roundDouble(2,
                    member.getTotalBillableLogTime())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)) + "&nbsp;&nbsp;" +
                    VaadinIcons.GIFT.getHtml() + new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime()))
                    .setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS));

            ELabel memberWorkStatus = ELabel.html(memberWorksInfo).withStyleName(WebThemes.META_INFO);
            footer.addComponent(memberWorkStatus);

            content.addComponent(footer);
            return layout;
        }

        private String buildAssigneeValue(SimpleProjectMember member) {
            Div div = new DivLessFormatter();
            A userLink = new A(ProjectLinkGenerator.generateProjectMemberLink(member.getProjectid(), member.getUsername())).setId("tag" + TooltipHelper.TOOLTIP_ID);

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(member.getUsername()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            userLink.appendText(member.getMemberFullName());

            if (member.getUsername().equals(CurrentProjectVariables.getProject().getMemlead())) {
                userLink.appendText(" (Lead)");
            }

            return div.appendChild(userLink).write();
        }
    }
}
