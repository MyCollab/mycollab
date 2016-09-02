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

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.module.crm.view.campaign.CampaignImportWindow;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppContext;
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

        MButton importBtn = ComponentUtils.createImportEntitiesButton()
                .withListener(clickEvent -> UI.getCurrent().addWindow(new CampaignImportWindow()))
                .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
        this.addExtraButton(importBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<OpportunitySearchCriteria> createSearchPanel() {
        return new OpportunitySearchPanel();
    }

    @Override
    protected AbstractPagedBeanTable<OpportunitySearchCriteria, SimpleOpportunity> createBeanTable() {
        return new OpportunityTableDisplay(CrmTypeConstants.OPPORTUNITY,
                OpportunityTableFieldDef.selected(), Arrays.asList(
                OpportunityTableFieldDef.opportunityName(), OpportunityTableFieldDef.accountName(),
                OpportunityTableFieldDef.saleStage(), OpportunityTableFieldDef.amount(),
                OpportunityTableFieldDef.expectedCloseDate(), OpportunityTableFieldDef.assignUser()));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (AppContext.canAccess(RolePermissionCollections.CRM_OPPORTUNITY)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
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
