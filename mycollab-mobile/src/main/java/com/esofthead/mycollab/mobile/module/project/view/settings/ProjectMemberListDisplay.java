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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ProjectMemberListDisplay extends DefaultPagedBeanList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> {
    private static final long serialVersionUID = -8386107467240727141L;

    public ProjectMemberListDisplay() {
        super(ApplicationContextUtil.getSpringBean(ProjectMemberService.class), new ProjectMemberRowDisplayHandler());
        this.addStyleName("member-list");
    }

    private static class ProjectMemberRowDisplayHandler implements RowDisplayHandler<SimpleProjectMember> {

        @Override
        public Component generateRow(final SimpleProjectMember member, int rowIndex) {
            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.setWidth("100%");
            mainLayout.setStyleName("member-row");
            Image memberAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(member.getMemberAvatarId(), 48);
            mainLayout.addComponent(memberAvatar);

            VerticalLayout memberInfoLayout = new VerticalLayout();
            memberInfoLayout.setWidth("100%");
            memberInfoLayout.setStyleName("member-info");
            Button memberDisplayName = new Button(member.getDisplayName());
            memberDisplayName.setStyleName("display-name");
            memberDisplayName.addClickListener(new Button.ClickListener() {

                private static final long serialVersionUID = -1689918040423397195L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoRead(this, member.getUsername()));
                }
            });
            memberInfoLayout.addComponent(memberDisplayName);

            Label memberUserName = new Label(member.getUsername());
            memberInfoLayout.addComponent(memberUserName);

            String bugStatus = member.getNumOpenBugs() + " open bug";
            if (member.getNumOpenBugs() > 1) {
                bugStatus += "s";
            }

            String taskStatus = member.getNumOpenTasks() + " open task";
            if (member.getNumOpenTasks() > 1) {
                taskStatus += "s";
            }

            Label memberWorkStatus = new Label(bugStatus + " - " + taskStatus);
            memberInfoLayout.addComponent(memberWorkStatus);

            mainLayout.addComponent(memberInfoLayout);
            mainLayout.setExpandRatio(memberInfoLayout, 1.0f);

            return mainLayout;
        }

    }

}
