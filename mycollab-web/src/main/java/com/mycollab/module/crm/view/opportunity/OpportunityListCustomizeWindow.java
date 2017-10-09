package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.OpportunityTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunityListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public OpportunityListCustomizeWindow(AbstractPagedBeanTable table) {
        super(CrmTypeConstants.OPPORTUNITY, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(OpportunityTableFieldDef.accountName,
                OpportunityTableFieldDef.amount,
                OpportunityTableFieldDef.assignUser,
                OpportunityTableFieldDef.campaignName,
                OpportunityTableFieldDef.currency,
                OpportunityTableFieldDef.expectedCloseDate,
                OpportunityTableFieldDef.leadSource,
                OpportunityTableFieldDef.opportunityName,
                OpportunityTableFieldDef.probability,
                OpportunityTableFieldDef.type);
    }

}
