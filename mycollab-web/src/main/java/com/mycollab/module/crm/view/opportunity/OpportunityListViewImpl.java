package com.mycollab.module.crm.view.opportunity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.fielddef.OpportunityTableFieldDef;
import com.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class OpportunityListViewImpl extends AbstractListItemComp<OpportunitySearchCriteria, SimpleOpportunity> implements OpportunityListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton().withListener(
                clickEvent -> UI.getCurrent().addWindow(new OpportunityListCustomizeWindow(tableItem))
        );
        this.addExtraButton(customizeViewBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<OpportunitySearchCriteria> createSearchPanel() {
        return new OpportunitySearchPanel(true);
    }

    @Override
    protected AbstractPagedBeanTable<OpportunitySearchCriteria, SimpleOpportunity> createBeanTable() {
        return new OpportunityTableDisplay(CrmTypeConstants.OPPORTUNITY,
                OpportunityTableFieldDef.selected, Arrays.asList(
                OpportunityTableFieldDef.opportunityName, OpportunityTableFieldDef.accountName,
                OpportunityTableFieldDef.saleStage, OpportunityTableFieldDef.amount,
                OpportunityTableFieldDef.expectedCloseDate, OpportunityTableFieldDef.assignUser));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_OPPORTUNITY)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new OpportunityCrmListNoItemView());
    }
}
