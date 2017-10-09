package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.fielddef.CaseTableFieldDef;
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
public class CaseListViewImpl extends AbstractListItemComp<CaseSearchCriteria, SimpleCase> implements CaseListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton().withListener(clickEvent -> UI.getCurrent().addWindow(
                new CaseListCustomizeWindow(tableItem)));
        this.addExtraButton(customizeViewBtn);
    }

    @Override
    protected DefaultGenericSearchPanel<CaseSearchCriteria> createSearchPanel() {
        return new CaseSearchPanel(true);
    }

    @Override
    protected AbstractPagedBeanTable<CaseSearchCriteria, SimpleCase> createBeanTable() {
        return new CaseTableDisplay(
                CrmTypeConstants.CASE, CaseTableFieldDef.selected,
                Arrays.asList(CaseTableFieldDef.subject, CaseTableFieldDef.account,
                        CaseTableFieldDef.priority, CaseTableFieldDef.status,
                        CaseTableFieldDef.assignUser, CaseTableFieldDef.createdTime));
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_CASE)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CASE)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new CaseCrmListNoItemView());
    }
}
