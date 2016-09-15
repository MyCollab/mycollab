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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.events.OpportunityEvent;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityLeadSource;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.ui.chart.PieChartWrapper;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class OpportunityLeadSourceDashboard extends PieChartWrapper<OpportunitySearchCriteria> implements IOpportunityLeadSourceDashboard {
    private static final long serialVersionUID = 1L;

    public OpportunityLeadSourceDashboard() {
        super(400, 265);
    }

    @Override
    protected List<GroupItem> loadGroupItems() {
        final OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
        return opportunityService.getLeadSourcesSummary(searchCriteria);
    }

    @Override
    protected DefaultPieDataset createDataset() {
        final DefaultPieDataset dataset = new DefaultPieDataset();

        final OpportunityLeadSource[] leadSources = CrmDataTypeFactory.getLeadSourceList();
        for (final OpportunityLeadSource source : leadSources) {
            boolean isFound = false;
            for (final GroupItem item : groupItems) {
                if (source.name().equals(item.getGroupid())) {
                    if (item.getValue() != 0)
                        dataset.setValue(UserUIContext.getMessage(source), item.getValue());
                    else
                        dataset.setValue(UserUIContext.getMessage(source), item.getCountNum());
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                dataset.setValue(UserUIContext.getMessage(source), 0);
            }
        }

        return dataset;
    }

    @Override
    public void clickLegendItem(String key) {
        OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        searchCriteria.setLeadSources(new SetSearchField<>(key));
        EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, searchCriteria));
    }
}
