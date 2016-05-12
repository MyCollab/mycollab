/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ProjectMemberListDisplay extends DefaultPagedBeanList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> {
    private static final long serialVersionUID = -8386107467240727141L;

    public ProjectMemberListDisplay() {
        super(AppContextUtil.getSpringBean(ProjectMemberService.class), new ProjectMemberRowDisplayHandler());
    }

    private static class ProjectMemberRowDisplayHandler implements RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(final SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout mainLayout = new MHorizontalLayout().withMargin(true).withWidth("100%");
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48);

            VerticalLayout memberInfoLayout = new VerticalLayout();
            mainLayout.addStyleName(UIConstants.TRUNCATE);
            mainLayout.with(memberAvatar, memberInfoLayout).expand(memberInfoLayout);

            A memberLink = new A(ProjectLinkBuilder.generateProjectMemberFullLink(CurrentProjectVariables
                    .getProjectId(), member.getUsername())).appendText(member.getDisplayName());
            Label memberLbl = new ELabel(memberLink.write(), ContentMode.HTML)
                    .withWidthUndefined();
            memberInfoLayout.addComponent(new MCssLayout(memberLbl).withFullWidth());

            Label memberEmailLabel = new Label(String.format("<a href='mailto:%s'>%s</a>", member.getUsername(),
                    member.getUsername()), ContentMode.HTML);
            memberEmailLabel.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(memberEmailLabel);

            ELabel memberSinceLabel = new ELabel(String.format("Member since: %s", AppContext.formatPrettyTime(member.getJoindate())))
                    .withDescription(AppContext.formatDateTime(member.getJoindate()));
            memberSinceLabel.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(memberSinceLabel);

            ELabel lastAccessTimeLbl = new ELabel(String.format("Logged in %s", AppContext.formatPrettyTime(member.getLastAccessTime())))
                    .withDescription(AppContext.formatDateTime(member.getLastAccessTime()));
            lastAccessTimeLbl.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(lastAccessTimeLbl);

            String memberWorksInfo = ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml() + " " + new Span
                    ().appendText("" + member.getNumOpenTasks()).setTitle("Open tasks") + "  " + ProjectAssetsManager.getAsset
                    (ProjectTypeConstants.BUG).getHtml() + " " + new Span().appendText("" + member.getNumOpenBugs())
                    .setTitle("Open bugs") + " " +
                    " " + FontAwesome.MONEY.getHtml() + " " + new Span().appendText("" + NumberUtils.roundDouble(2,
                    member.getTotalBillableLogTime())).setTitle("Billable hours") + "  " + FontAwesome.GIFT.getHtml() +
                    " " + new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime())).setTitle("Non billable hours");

            Label memberWorkStatus = new ELabel(memberWorksInfo, ContentMode.HTML).withWidth("100%");
            memberWorkStatus.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(new MCssLayout(memberWorkStatus).withFullWidth());

            return mainLayout;
        }
    }
}
