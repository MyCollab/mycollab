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
package com.mycollab.mobile.module.project.view.settings;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ProjectMemberListDisplay extends DefaultPagedBeanList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> {
    private static final long serialVersionUID = -8386107467240727141L;

    public ProjectMemberListDisplay() {
        super(AppContextUtil.getSpringBean(ProjectMemberService.class), new ProjectMemberRowDisplayHandler());
    }

    public ProjectMemberListDisplay(RowDisplayHandler<SimpleProjectMember> rowDisplayHandler) {
        super(AppContextUtil.getSpringBean(ProjectMemberService.class), rowDisplayHandler);
    }

    private static class ProjectMemberRowDisplayHandler implements RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(final SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout mainLayout = new MHorizontalLayout().withMargin(true).withFullWidth();
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48);
            memberAvatar.addStyleName(UIConstants.CIRCLE_BOX);

            MVerticalLayout memberInfoLayout = new MVerticalLayout().withMargin(false);
            mainLayout.with(memberAvatar, memberInfoLayout).expand(memberInfoLayout);

            A memberLink = new A(ProjectLinkBuilder.generateProjectMemberFullLink(CurrentProjectVariables
                    .getProjectId(), member.getUsername())).appendText(member.getDisplayName());
            Label memberLbl = ELabel.html(memberLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
            memberInfoLayout.addComponent(memberLbl);

            if (Boolean.TRUE.equals(MyCollabUI.getBillingAccount().getDisplayemailpublicly())) {
                Label memberEmailLabel = ELabel.html(String.format("<a href='mailto:%s'>%s</a>", member.getUsername(), member.getUsername()))
                        .withStyleName(UIConstants.META_INFO);
                memberInfoLayout.addComponent(memberEmailLabel);
            }

            ELabel memberSinceLabel = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_SINCE, UserUIContext.formatPrettyTime(member.getJoindate())))
                    .withDescription(UserUIContext.formatDateTime(member.getJoindate())).withStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(memberSinceLabel);

            ELabel lastAccessTimeLbl = ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_LOGGED_IN,
                    UserUIContext.formatPrettyTime(member.getLastAccessTime())))
                    .withDescription(UserUIContext.formatDateTime(member.getLastAccessTime())).withStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(lastAccessTimeLbl);

            String memberWorksInfo = String.format("%s %s  %s %s  %s %s  %s %s", ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml(), new Span().appendText("" + member.getNumOpenTasks()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_TASKS)), ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml(), new Span().appendText("" + member.getNumOpenBugs())
                    .setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_BUGS)), FontAwesome.MONEY.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2,
                    member.getTotalBillableLogTime())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)), FontAwesome.GIFT.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime()))
                    .setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)));

            Label memberWorkStatus = ELabel.html(memberWorksInfo).withStyleName(UIConstants.META_INFO).withFullWidth();
            memberInfoLayout.addComponent(new MCssLayout(memberWorkStatus).withFullWidth());

            return mainLayout;
        }
    }
}
