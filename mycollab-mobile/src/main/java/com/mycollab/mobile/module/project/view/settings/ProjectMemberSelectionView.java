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
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
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

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ProjectMemberSelectionView extends AbstractSelectionView<SimpleProjectMember> {
    private static final long serialVersionUID = 4392390405558243836L;

    private ProjectMemberListDisplay memberListDisplay;

    private MemberRowDisplayHandler rowDisplayHandler;

    public ProjectMemberSelectionView() {
        super();
        createUI();
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT));
    }

    private void createUI() {
        rowDisplayHandler = new MemberRowDisplayHandler();
        memberListDisplay = new ProjectMemberListDisplay(rowDisplayHandler);
        memberListDisplay.setWidth("100%");
        this.setContent(memberListDisplay);
    }

    @Override
    public void load() {
        ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        memberListDisplay.search(searchCriteria);
        SimpleProjectMember blankMember = new SimpleProjectMember();
        memberListDisplay.getListContainer().addComponentAsFirst(rowDisplayHandler.generateRow(blankMember, 0));
    }

    private class MemberRowDisplayHandler implements RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(final SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout mainLayout = new MHorizontalLayout().withFullWidth();
            mainLayout.addLayoutClickListener(layoutClickEvent -> {
                selectionField.fireValueChange(member);
                getNavigationManager().navigateBack();
            });
            if (member.getId() == null) {
                mainLayout.addComponent(new Label(UserUIContext.getMessage(GenericI18Enum.EXT_NO_ITEM)));
                return mainLayout;
            }
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48);
            memberAvatar.addStyleName(UIConstants.CIRCLE_BOX);

            VerticalLayout memberInfoLayout = new VerticalLayout();
            mainLayout.with(memberAvatar, memberInfoLayout).expand(memberInfoLayout);

            A memberLink = new A(ProjectLinkBuilder.generateProjectMemberFullLink(CurrentProjectVariables
                    .getProjectId(), member.getUsername())).appendText(member.getDisplayName());
            Label memberLbl = ELabel.html(memberLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
            memberInfoLayout.addComponent(memberLbl);

            Label memberEmailLabel = new Label(member.getUsername());
            memberEmailLabel.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(memberEmailLabel);

            ELabel memberSinceLabel = new ELabel(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_SINCE, UserUIContext.formatPrettyTime(member.getJoindate())))
                    .withDescription(UserUIContext.formatDateTime(member.getJoindate()));
            memberSinceLabel.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(memberSinceLabel);

            ELabel lastAccessTimeLbl = new ELabel(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_LOGGED_IN, UserUIContext.formatPrettyTime(member.getLastAccessTime())))
                    .withDescription(UserUIContext.formatDateTime(member.getLastAccessTime()));
            lastAccessTimeLbl.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(lastAccessTimeLbl);

            String memberWorksInfo = String.format("%s %s  %s %s  %s %s  %s %s", ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml(), new Span
                    ().appendText("" + member.getNumOpenTasks()).setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_TASKS)), ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml(), new Span().appendText("" + member.getNumOpenBugs())
                    .setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OPEN_BUGS)), FontAwesome.MONEY.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2,
                    member.getTotalBillableLogTime())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)), FontAwesome.GIFT.getHtml(), new Span().appendText("" + NumberUtils.roundDouble(2, member.getTotalNonBillableLogTime()))
                    .setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)));

            Label memberWorkStatus = new ELabel(memberWorksInfo, ContentMode.HTML).withFullWidth();
            memberWorkStatus.addStyleName(UIConstants.META_INFO);
            memberInfoLayout.addComponent(new MCssLayout(memberWorkStatus).withFullWidth());

            return mainLayout;
        }

    }

}
