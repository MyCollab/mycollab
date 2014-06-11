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
package com.esofthead.mycollab.module.crm.view;

import com.esofthead.mycollab.module.crm.view.account.AccountListDashlet;
import com.esofthead.mycollab.module.crm.view.activity.CallListDashlet;
import com.esofthead.mycollab.module.crm.view.activity.MeetingListDashlet;
import com.esofthead.mycollab.module.crm.view.lead.LeadListDashlet;
import com.esofthead.mycollab.module.crm.view.opportunity.IOpportunityPipelineFunnelChartDashlet;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
@ViewComponent
public class CrmHomeViewImpl extends AbstractLazyPageView implements
		CrmHomeView {
	private IOpportunityPipelineFunnelChartDashlet opportunityChartDashlet;
	private AccountListDashlet accountDashlet;
	private MeetingListDashlet meetingDashlet;
	private CallListDashlet callDashlet;
	private LeadListDashlet leadDashlet;

	private ActivityStreamPanel activityStreamPanel;
	private SalesDashboardView salesDashboard;

	public CrmHomeViewImpl() {
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false, true, true, true));
	}

	@Override
	protected void displayView() {
		this.removeAllComponents();

		HorizontalLayout contentLayout = new HorizontalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setWidth("100%");

		VerticalLayout myAssignmentsLayout = new VerticalLayout();

		if (AppContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
			opportunityChartDashlet = ViewManager
					.getView(IOpportunityPipelineFunnelChartDashlet.class);
			myAssignmentsLayout.addComponent(opportunityChartDashlet);
		}

		if (AppContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
			accountDashlet = new AccountListDashlet();
			myAssignmentsLayout.addComponent(accountDashlet);
		}

		if (AppContext.canRead(RolePermissionCollections.CRM_MEETING)) {
			meetingDashlet = new MeetingListDashlet();
			myAssignmentsLayout.addComponent(meetingDashlet);
		}

		if (AppContext.canRead(RolePermissionCollections.CRM_CALL)) {
			callDashlet = new CallListDashlet();
			myAssignmentsLayout.addComponent(callDashlet);
		}

		if (AppContext.canRead(RolePermissionCollections.CRM_LEAD)) {
			leadDashlet = new LeadListDashlet();
			myAssignmentsLayout.addComponent(leadDashlet);
		}

		contentLayout.addComponent(myAssignmentsLayout);
		contentLayout.setExpandRatio(myAssignmentsLayout, 1.0f);

		VerticalLayout streamsLayout = new VerticalLayout();
		streamsLayout.setSizeUndefined();
		streamsLayout.setMargin(new MarginInfo(false, false, false, true));

		salesDashboard = new SalesDashboardView();
		salesDashboard.setWidth("550px");
		streamsLayout.addComponent(salesDashboard);
		streamsLayout.setComponentAlignment(salesDashboard,
				Alignment.MIDDLE_RIGHT);

		activityStreamPanel = new ActivityStreamPanel();
		activityStreamPanel.setWidth("550px");
		streamsLayout.addComponent(activityStreamPanel);
		streamsLayout.setComponentAlignment(activityStreamPanel,
				Alignment.MIDDLE_RIGHT);

		contentLayout.addComponent(streamsLayout);
		contentLayout.setComponentAlignment(streamsLayout, Alignment.TOP_RIGHT);

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

		if (opportunityChartDashlet != null) {
			opportunityChartDashlet.display();
		}

		if (activityStreamPanel != null) {
			activityStreamPanel.display();
		}

		if (salesDashboard != null) {
			salesDashboard.displayReport();
		}
	}
}
