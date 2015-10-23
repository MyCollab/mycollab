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
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;

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
        this.setCaption(AppContext.getMessage(ProjectMemberI18nEnum.M_VIEW_MEMBER_LOOKUP));
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
        memberListDisplay.setSearchCriteria(searchCriteria);
        SimpleProjectMember blankMember = new SimpleProjectMember();
        memberListDisplay.getListContainer().addComponentAsFirst(rowDisplayHandler.generateRow(blankMember, 0));
    }

    private class MemberRowDisplayHandler implements RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(final SimpleProjectMember member,
                                     int rowIndex) {
            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.setWidth("100%");
            mainLayout.setStyleName("member-row");
            mainLayout.addStyleName("list-item");
            mainLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

                private static final long serialVersionUID = -6886497684142268213L;

                @Override
                public void layoutClick(
                        LayoutEvents.LayoutClickEvent event) {
                    selectionField.fireValueChange(member);
                    ProjectMemberSelectionView.this.getNavigationManager().navigateBack();
                }
            });
            if (member.getId() == null) {
                mainLayout.addStyleName("blank-item");
                return mainLayout;
            }
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(
                    member.getMemberAvatarId(), 64);
            mainLayout.addComponent(memberAvatar);

            VerticalLayout memberInfoLayout = new VerticalLayout();
            memberInfoLayout.setWidth("100%");
            memberInfoLayout.setStyleName("member-info");
            Label memberDisplayName = new Label(member.getDisplayName());
            memberDisplayName.setStyleName("display-name");
            memberInfoLayout.addComponent(memberDisplayName);

            Label memberUserName = new Label(member.getUsername());
            memberUserName.setStyleName("user-name");
            memberInfoLayout.addComponent(memberUserName);

            mainLayout.addComponent(memberInfoLayout);
            mainLayout.setExpandRatio(memberInfoLayout, 1.0f);

            return mainLayout;
        }

    }

}
