package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.CampaignTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd,
 * @since 1.0
 */
public class CampaignListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public CampaignListCustomizeWindow(AbstractPagedBeanTable table) {
        super(CrmTypeConstants.CAMPAIGN, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(CampaignTableFieldDef.assignUser,
                CampaignTableFieldDef.actualcost, CampaignTableFieldDef.budget,
                CampaignTableFieldDef.campaignname, CampaignTableFieldDef.endDate,
                CampaignTableFieldDef.expectedCost, CampaignTableFieldDef.expectedRevenue,
                CampaignTableFieldDef.startDate, CampaignTableFieldDef.status,
                CampaignTableFieldDef.type);
    }

}
