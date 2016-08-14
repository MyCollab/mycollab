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

import com.mycollab.module.project.view.milestone.MilestoneTimelineWidget;
import com.mycollab.vaadin.mvp.view.AbstractLazyPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
@ViewComponent
public class ProjectSummaryViewImpl extends AbstractLazyPageView implements ProjectSummaryView {

    @Override
    protected void displayView() {
        withMargin(new MarginInfo(true, true, false, true));

        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();
        this.addComponent(layout);
        MVerticalLayout leftPanel = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, false));

        MilestoneTimelineWidget milestoneTimelineWidget = new MilestoneTimelineWidget();
        ProjectOverdueAssignmentsWidget taskOverdueWidget = new ProjectOverdueAssignmentsWidget();
        ProjectUnresolvedAssignmentWidget unresolvedAssignmentThisWeekWidget = new ProjectUnresolvedAssignmentWidget();

        ProjectUnresolvedAssignmentWidget unresolvedAssignmentNextWeekWidget = new ProjectUnresolvedAssignmentWidget();

        leftPanel.addComponent(milestoneTimelineWidget);
        leftPanel.with(milestoneTimelineWidget, unresolvedAssignmentThisWeekWidget, unresolvedAssignmentNextWeekWidget,
                taskOverdueWidget);

        MVerticalLayout rightPanel = new MVerticalLayout().withMargin(false).withWidth("500px");
        ProjectMembersWidget membersWidget = new ProjectMembersWidget();
        ProjectActivityStreamComponent activityPanel = new ProjectActivityStreamComponent();
        rightPanel.with(membersWidget, activityPanel);

        milestoneTimelineWidget.display();
        unresolvedAssignmentThisWeekWidget.displayUnresolvedAssignmentsThisWeek();
        unresolvedAssignmentNextWeekWidget.displayUnresolvedAssignmentsNextWeek();
        activityPanel.showProjectFeeds();
        membersWidget.showInformation();
        taskOverdueWidget.showOpenAssignments();

        layout.with(leftPanel, rightPanel).expand(leftPanel);
    }

    @Override
    public void displaySearchResult(String value) {
        removeAllComponents();
    }
}