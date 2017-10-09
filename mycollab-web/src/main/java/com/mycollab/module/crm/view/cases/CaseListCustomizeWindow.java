package com.mycollab.module.crm.view.cases;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.CaseTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public CaseListCustomizeWindow(AbstractPagedBeanTable table) {
        super(CrmTypeConstants.CASE, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(CaseTableFieldDef.account,
                CaseTableFieldDef.assignUser, CaseTableFieldDef.createdTime,
                CaseTableFieldDef.email, CaseTableFieldDef.origin,
                CaseTableFieldDef.lastUpdatedTime, CaseTableFieldDef.phone,
                CaseTableFieldDef.priority, CaseTableFieldDef.reason,
                CaseTableFieldDef.status, CaseTableFieldDef.subject,
                CaseTableFieldDef.type);
    }

}
