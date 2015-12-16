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
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
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
        withMargin(new MarginInfo(true, false, false, false));

        CssLayout contentWrapper = new CssLayout();
        contentWrapper.setStyleName("content-wrapper");
        contentWrapper.setWidth("100%");
        this.addComponent(contentWrapper);

        MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%");
        contentWrapper.addComponent(layout);
        VerticalLayout leftPanel = new VerticalLayout();

        ProjectActivityStreamComponent activityPanel = new ProjectActivityStreamComponent();
        leftPanel.addComponent(activityPanel);

        MVerticalLayout rightPanel = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true));
        ProjectMembersWidget membersWidget = new ProjectMembersWidget();
        ProjectAssignmentsWidget taskOverdueWidget = new ProjectAssignmentsWidget();

        rightPanel.with(membersWidget, taskOverdueWidget);

        activityPanel.showProjectFeeds();
        membersWidget.showInformation();
        taskOverdueWidget.showOpenAssignments();

        layout.with(leftPanel, rightPanel);
    }
}