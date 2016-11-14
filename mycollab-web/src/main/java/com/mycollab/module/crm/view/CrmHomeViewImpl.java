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
package com.mycollab.module.crm.view;

import com.mycollab.module.crm.view.account.AccountListDashlet;
import com.mycollab.module.crm.view.activity.CallListDashlet;
import com.mycollab.module.crm.view.activity.MeetingListDashlet;
import com.mycollab.module.crm.view.lead.LeadListDashlet;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.view.AbstractLazyPageView;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class CrmHomeViewImpl extends AbstractLazyPageView implements CrmHomeView {

    private AccountListDashlet accountDashlet;
    private MeetingListDashlet meetingDashlet;
    private CallListDashlet callDashlet;
    private LeadListDashlet leadDashlet;

    private ActivityStreamPanel activityStreamPanel;
    private SalesDashboardView salesDashboard;

    @Override
    protected void displayView() {
        MHorizontalLayout contentLayout = new MHorizontalLayout().withMargin(true).withFullWidth();

        MVerticalLayout myAssignmentsLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false));

        if (UserUIContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
            accountDashlet = new AccountListDashlet();
            myAssignmentsLayout.addComponent(accountDashlet);
        }

        if (UserUIContext.canRead(RolePermissionCollections.CRM_MEETING)) {
            meetingDashlet = new MeetingListDashlet();
            myAssignmentsLayout.addComponent(meetingDashlet);
        }

        if (UserUIContext.canRead(RolePermissionCollections.CRM_CALL)) {
            callDashlet = new CallListDashlet();
            myAssignmentsLayout.addComponent(callDashlet);
        }

        if (UserUIContext.canRead(RolePermissionCollections.CRM_LEAD)) {
            leadDashlet = new LeadListDashlet();
            myAssignmentsLayout.addComponent(leadDashlet);
        }

        contentLayout.with(myAssignmentsLayout).expand(myAssignmentsLayout);

        MVerticalLayout streamsLayout = new MVerticalLayout().withMargin(new MarginInfo(true, false, false, true));
        streamsLayout.setSizeUndefined();

        salesDashboard = new SalesDashboardView();
        salesDashboard.setWidth("550px");
        streamsLayout.with(salesDashboard);

        activityStreamPanel = new ActivityStreamPanel();
        activityStreamPanel.setWidth("550px");
        streamsLayout.with(activityStreamPanel);

        contentLayout.with(streamsLayout).withAlign(streamsLayout, Alignment.TOP_RIGHT);
        this.addComponent(contentLayout);
        displayDashboard();
    }

    private void displayDashboard() {
        if (accountDashlet != null) {
            accountDashlet.display();
        }

        if (meetingDashlet != null) {
            meetingDashlet.display();
        }

        if (callDashlet != null) {
            callDashlet.display();
        }

        if (leadDashlet != null) {
            leadDashlet.display();
        }

        if (activityStreamPanel != null) {
            activityStreamPanel.display();
        }

        if (salesDashboard != null) {
            salesDashboard.displayReport();
        }
    }
}
