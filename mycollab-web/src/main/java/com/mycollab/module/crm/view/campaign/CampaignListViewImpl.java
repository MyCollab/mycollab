package com.mycollab.module.crm.view.campaign;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.fielddef.CampaignTableFieldDef;
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
public class CampaignListViewImpl extends AbstractListItemComp<CampaignSearchCriteria, SimpleCampaign> implements CampaignListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton()
                .withListener(clickEvent -> UI.getCurrent().addWindow(new CampaignListCustomizeWindow(tableItem)));
        this.addExtraButton(customizeViewBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<CampaignSearchCriteria> createSearchPanel() {
        return new CampaignSearchPanel(true);
    }

    @Override
    protected AbstractPagedBeanTable<CampaignSearchCriteria, SimpleCampaign> createBeanTable() {
        return new CampaignTableDisplay(CrmTypeConstants.CAMPAIGN, CampaignTableFieldDef.selected,
                Arrays.asList(CampaignTableFieldDef.campaignname, CampaignTableFieldDef.status,
                        CampaignTableFieldDef.type, CampaignTableFieldDef.expectedRevenue,
                        CampaignTableFieldDef.endDate, CampaignTableFieldDef.assignUser));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_CAMPAIGN)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new CampaignCrmListNoItemView());
    }
}
