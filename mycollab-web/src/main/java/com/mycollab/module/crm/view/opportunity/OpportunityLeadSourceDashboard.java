package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.event.OpportunityEvent;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityLeadSource;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.chart.PieChartWrapper;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ViewComponent;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class OpportunityLeadSourceDashboard extends PieChartWrapper<OpportunitySearchCriteria> {
    private static final long serialVersionUID = 1L;

    public OpportunityLeadSourceDashboard() {
        super(OpportunityLeadSource.class, 400, 265);
    }

    @Override
    protected List<GroupItem> loadGroupItems() {
        final OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
        return opportunityService.getLeadSourcesSummary(searchCriteria);
    }

    @Override
    protected DefaultPieDataset createDataset() {
        final DefaultPieDataset dataset = new DefaultPieDataset();

        final OpportunityLeadSource[] leadSources = CrmDataTypeFactory.leadSourceList;
        for (final OpportunityLeadSource source : leadSources) {
            boolean isFound = false;
            for (final GroupItem item : groupItems) {
                if (source.name().equals(item.getGroupid())) {
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
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        searchCriteria.setLeadSources(new SetSearchField<>(key));
        EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, searchCriteria));
    }
}
