/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.community.module.crm.view.opportunity;

import java.util.List;

import org.jfree.data.general.DefaultPieDataset;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.community.ui.chart.PieChartDescriptionBox;
import com.esofthead.mycollab.community.ui.chart.PieChartWrapper;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.view.opportunity.IOpportunitySalesStageDashboard;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class OpportunitySalesStageDashboard extends
		PieChartWrapper<OpportunitySearchCriteria> implements
		IOpportunitySalesStageDashboard {
	private static final long serialVersionUID = 1L;

	public OpportunitySalesStageDashboard() {
		this(400, 265);
	}

	public OpportunitySalesStageDashboard(final int width, final int height) {
		super("Deals By Stages", width, height);
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@Override
	public void addViewListener(ViewListener listener) {

	}

	@Override
	protected DefaultPieDataset createDataset() {
		// create the dataset...
		final DefaultPieDataset dataset = new DefaultPieDataset();

		final OpportunityService opportunityService = ApplicationContextUtil
				.getSpringBean(OpportunityService.class);

		final List<GroupItem> groupItems = opportunityService
				.getSalesStageSummary(searchCriteria);

		final String[] salesStages = CrmDataTypeFactory
				.getOpportunitySalesStageList();
		for (final String status : salesStages) {
			boolean isFound = false;
			for (final GroupItem item : groupItems) {
				if (status.equals(item.getGroupid())) {
					dataset.setValue(status, item.getValue());
					isFound = true;
					break;
				}
			}

			if (!isFound) {
				dataset.setValue(status, 0);
			}
		}

		return dataset;
	}

	@Override
	protected void onClickedDescription(String key) {
		final OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		searchCriteria.setSalesStages(new SetSearchField<String>(
				SearchField.AND, new String[] { key }));
		EventBusFactory.getInstance().post(
				new OpportunityEvent.GotoList(this, searchCriteria));
	}

	@Override
	protected ComponentContainer createLegendBox() {
		return PieChartDescriptionBox.createLegendBox(this, pieDataSet);
	}

}
