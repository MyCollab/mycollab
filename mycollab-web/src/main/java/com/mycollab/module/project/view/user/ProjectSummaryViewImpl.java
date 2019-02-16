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

import com.jarektoro.responsivelayout.ResponsiveColumn;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.mycollab.module.project.view.milestone.MilestoneTimelineWidget;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.AbstractLazyPageView;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectSummaryViewImpl extends AbstractLazyPageView implements ProjectSummaryView {

    @Override
    protected void displayView() {
        ResponsiveLayout layout = new ResponsiveLayout(ResponsiveLayout.ContainerType.FIXED);
        layout.setSizeFull();
        this.with(layout);

        ResponsiveRow row = layout.addRow();

        ResponsiveColumn column1 = new ResponsiveColumn(12, 12, 12, 6);

        MilestoneTimelineWidget milestoneTimelineWidget = new MilestoneTimelineWidget();
        ProjectOverdueTicketsWidget taskOverdueWidget = new ProjectOverdueTicketsWidget();
        ProjectUnresolvedTicketsWidget unresolvedAssignmentThisWeekWidget = new ProjectUnresolvedTicketsWidget();

        ProjectUnresolvedTicketsWidget unresolvedAssignmentNextWeekWidget = new ProjectUnresolvedTicketsWidget();

        column1.setContent(new MVerticalLayout(milestoneTimelineWidget, unresolvedAssignmentThisWeekWidget, unresolvedAssignmentNextWeekWidget, taskOverdueWidget));

        ResponsiveColumn column2 = new ResponsiveColumn(12, 12, 12, 6);

        ProjectMembersWidget membersWidget = new ProjectMembersWidget();
        ProjectActivityStreamComponent activityPanel = new ProjectActivityStreamComponent();
        column2.setContent(new MVerticalLayout(membersWidget, activityPanel));

        milestoneTimelineWidget.display();
        unresolvedAssignmentThisWeekWidget.displayUnresolvedAssignmentsThisWeek();
        unresolvedAssignmentNextWeekWidget.displayUnresolvedAssignmentsNextWeek();
        activityPanel.showProjectFeeds();
        membersWidget.showInformation();
        taskOverdueWidget.showOpenTickets();

        row.addColumn(column1);
        row.addColumn(column2);
    }

    @Override
    public void displaySearchResult(String value) {
        removeAllComponents();
    }
}