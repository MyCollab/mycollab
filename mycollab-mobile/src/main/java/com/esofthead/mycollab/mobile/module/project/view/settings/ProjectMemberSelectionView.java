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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.event.LayoutEvents;
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

    private ProjectMemberSearchCriteria searchCriteria;
    private ProjectMemberListDisplay memberListDisplay;

    private MemberRowDisplayHandler rowDisplayHandler = new MemberRowDisplayHandler();

    public ProjectMemberSelectionView() {
        super();
        createUI();
        this.setCaption("Select");
    }

    private void createUI() {
        memberListDisplay = new ProjectMemberListDisplay();
        memberListDisplay.setWidth("100%");
        memberListDisplay.setRowDisplayHandler(rowDisplayHandler);
        this.setContent(memberListDisplay);
    }

    @Override
    public void load() {
        searchCriteria = new ProjectMemberSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        memberListDisplay.search(searchCriteria);
        SimpleProjectMember blankMember = new SimpleProjectMember();
        memberListDisplay.getListContainer().addComponentAsFirst(rowDisplayHandler.generateRow(blankMember, 0));
    }

    private class MemberRowDisplayHandler implements RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(final SimpleProjectMember member, int rowIndex) {
            MHorizontalLayout mainLayout = new MHorizontalLayout().withWidth("100%");
            mainLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                private static final long serialVersionUID = -6886497684142268213L;

                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    selectionField.fireValueChange(member);
                    getNavigationManager().navigateBack();
                }
            });
            if (member.getId() == null) {
                mainLayout.addComponent(new Label("No assignee"));
                return mainLayout;
            }
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48);

            VerticalLayout memberInfoLayout = new VerticalLayout();
            mainLayout.addStyleName(UIConstants.TRUNCATE);
            mainLayout.with(memberAvatar, memberInfoLayout).expand(memberInfoLayout);

            A memberLink = new A(ProjectLinkBuilder.generateProjectMemberFullLink(CurrentProjectVariables
                    .getProjectId(), member.getUsername())).appendText(member.getDisplayName());
            Label memberLbl = new ELabel(member.getDisplayName(), ContentMode.HTML)
                    .withWidthUndefined();
            memberInfoLayout.addComponent(new MCssLayout(memberLbl).withFullWidth());

            Label memberEmailLabel = new Label(member.getUsername());
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
