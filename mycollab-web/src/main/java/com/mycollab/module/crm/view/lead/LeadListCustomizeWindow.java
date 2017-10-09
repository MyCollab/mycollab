package com.mycollab.module.crm.view.lead;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.LeadTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public LeadListCustomizeWindow(AbstractPagedBeanTable table) {
        super(CrmTypeConstants.LEAD, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(LeadTableFieldDef.accountName,
                LeadTableFieldDef.assignedUser, LeadTableFieldDef.department,
                LeadTableFieldDef.email, LeadTableFieldDef.phoneoffice,
                LeadTableFieldDef.fax, LeadTableFieldDef.industry,
                LeadTableFieldDef.leadSource, LeadTableFieldDef.mobile,
                LeadTableFieldDef.name, LeadTableFieldDef.status,
                LeadTableFieldDef.title, LeadTableFieldDef.website);
    }

}
