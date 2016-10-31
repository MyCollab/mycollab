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

import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.view.milestone.MilestoneTimelineWidget;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.view.AbstractLazyPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.HorizontalLocationIs;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectSummaryViewImpl extends AbstractLazyPageView implements ProjectSummaryView {

    @Override
    protected void displayView() {
        withMargin(true);

        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();
        this.with(layout);

        DDVerticalLayout leftPanel = new DDVerticalLayout();
        leftPanel.setSpacing(true);
        leftPanel.setMargin(new MarginInfo(false, true, false, false));
        leftPanel.setComponentVerticalDropRatio(0.3f);
        leftPanel.setDragMode(LayoutDragMode.CLONE_OTHER);
        leftPanel.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();

                DDVerticalLayout.VerticalLayoutTargetDetails details = (DDVerticalLayout.VerticalLayoutTargetDetails) event
                        .getTargetDetails();
                Component dragComponent = transferable.getComponent();
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new Not(HorizontalLocationIs.CENTER);
            }
        });

        MilestoneTimelineWidget milestoneTimelineWidget = new MilestoneTimelineWidget();
        ProjectOverdueTicketsWidget taskOverdueWidget = new ProjectOverdueTicketsWidget();
        ProjectUnresolvedTicketsWidget unresolvedAssignmentThisWeekWidget = new ProjectUnresolvedTicketsWidget();

        ProjectUnresolvedTicketsWidget unresolvedAssignmentNextWeekWidget = new ProjectUnresolvedTicketsWidget();

        leftPanel.addComponent(milestoneTimelineWidget);
        leftPanel.addComponent(unresolvedAssignmentThisWeekWidget);
        leftPanel.addComponent(unresolvedAssignmentNextWeekWidget);
        leftPanel.addComponent(taskOverdueWidget);


        DDVerticalLayout rightPanel = new DDVerticalLayout();
        rightPanel.setWidth("500px");
        rightPanel.setSpacing(true);
        rightPanel.setMargin(new MarginInfo(false, false, true, false));
        rightPanel.setComponentVerticalDropRatio(0.3f);
        rightPanel.setDragMode(LayoutDragMode.CLONE_OTHER);
        rightPanel.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();

                DDVerticalLayout.VerticalLayoutTargetDetails details = (DDVerticalLayout.VerticalLayoutTargetDetails) event
                        .getTargetDetails();
                Component dragComponent = transferable.getComponent();
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new Not(HorizontalLocationIs.CENTER);
            }
        });
        ProjectMembersWidget membersWidget = new ProjectMembersWidget();
        ProjectActivityStreamComponent activityPanel = new ProjectActivityStreamComponent();
        rightPanel.addComponent(membersWidget);
        rightPanel.addComponent(activityPanel);

        milestoneTimelineWidget.display();
        unresolvedAssignmentThisWeekWidget.displayUnresolvedAssignmentsThisWeek();
        unresolvedAssignmentNextWeekWidget.displayUnresolvedAssignmentsNextWeek();
        activityPanel.showProjectFeeds();
        membersWidget.showInformation();
        taskOverdueWidget.showOpenTickets();

        layout.with(leftPanel, rightPanel).expand(leftPanel);
    }

    @Override
    public void displaySearchResult(String value) {
        removeAllComponents();
    }
}