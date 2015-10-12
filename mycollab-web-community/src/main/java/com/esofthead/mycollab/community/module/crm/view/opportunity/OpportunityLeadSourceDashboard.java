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

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.community.ui.chart.PieChartWrapper;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.view.opportunity.IOpportunityLeadSourceDashboard;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.ComponentContainer;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class OpportunityLeadSourceDashboard extends PieChartWrapper<OpportunitySearchCriteria> implements IOpportunityLeadSourceDashboard {
    private static final long serialVersionUID = 1L;

    public OpportunityLeadSourceDashboard(final int width, final int height) {
        super(width, height);
    }

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    @Override
    public void addViewListener(ViewListener listener) {

    }

    @Override
    protected List<GroupItem> loadGroupItems() {
        final OpportunityService opportunityService = ApplicationContextUtil.getSpringBean(OpportunityService.class);
        return opportunityService.getLeadSourcesSummary(searchCriteria);
    }

    @Override
    protected DefaultPieDataset createDataset() {
        final DefaultPieDataset dataset = new DefaultPieDataset();

        final String[] leadSources = CrmDataTypeFactory.getLeadSourceList();
        for (final String source : leadSources) {
            boolean isFound = false;
            for (final GroupItem item : groupItems) {
                if (source.equals(item.getGroupid())) {
                    if (item.getValue() != 0)
                        dataset.setValue(source, item.getValue());
                    else
                        dataset.setValue(source, item.getCountNum());
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                dataset.setValue(source, 0);
            }
        }

        return dataset;
    }

    @Override
    public void clickLegendItem(String key) {
        OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        searchCriteria.setLeadSources(new SetSearchField<>(key));
        EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, searchCriteria));
    }
}
