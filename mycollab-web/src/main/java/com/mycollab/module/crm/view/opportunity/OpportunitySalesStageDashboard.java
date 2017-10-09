package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.event.OpportunityEvent;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunitySalesStage;
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
public class OpportunitySalesStageDashboard extends PieChartWrapper<OpportunitySearchCriteria> {
    private static final long serialVersionUID = 1L;

    public OpportunitySalesStageDashboard() {
        super(OpportunitySalesStage.class, 400, 265);
    }

    @Override
    protected List<GroupItem> loadGroupItems() {
        final OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
        return opportunityService.getSalesStageSummary(searchCriteria);
    }

    @Override
    protected DefaultPieDataset createDataset() {
        // create the dataset...
        final DefaultPieDataset dataset = new DefaultPieDataset();

        final OpportunitySalesStage[] salesStages = CrmDataTypeFactory.opportunitySalesStageList;
        for (final OpportunitySalesStage status : salesStages) {
            boolean isFound = false;
            for (final GroupItem item : groupItems) {
                if (status.name().equals(item.getGroupid())) {
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
    public void clickLegendItem(String key) {
        OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        searchCriteria.setSalesStages(new SetSearchField<>(key));
        EventBusFactory.getInstance().post(new OpportunityEvent.GotoList(this, searchCriteria));
    }
}
