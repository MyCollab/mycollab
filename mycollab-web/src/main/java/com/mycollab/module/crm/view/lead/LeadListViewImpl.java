package com.mycollab.module.crm.view.lead;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.fielddef.LeadTableFieldDef;
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
 * @since 2.0
 */
@ViewComponent
public class LeadListViewImpl extends AbstractListItemComp<LeadSearchCriteria, SimpleLead> implements LeadListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton()
                .withListener(clickEvent -> UI.getCurrent().addWindow(new LeadListCustomizeWindow(tableItem)));
        this.addExtraButton(customizeViewBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<LeadSearchCriteria> createSearchPanel() {
        return new LeadSearchPanel(true);
    }

    @Override
    protected AbstractPagedBeanTable<LeadSearchCriteria, SimpleLead> createBeanTable() {
        return new LeadTableDisplay(CrmTypeConstants.LEAD, LeadTableFieldDef.selected,
                Arrays.asList(LeadTableFieldDef.name, LeadTableFieldDef.status,
                        LeadTableFieldDef.accountName, LeadTableFieldDef.phoneoffice,
                        LeadTableFieldDef.email, LeadTableFieldDef.assignedUser));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_LEAD)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new LeadCrmListNoItemView());
    }
}