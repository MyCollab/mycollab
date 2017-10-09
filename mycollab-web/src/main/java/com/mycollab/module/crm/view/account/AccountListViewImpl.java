package com.mycollab.module.crm.view.account;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.fielddef.AccountTableFieldDef;
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
public class AccountListViewImpl extends AbstractListItemComp<AccountSearchCriteria, SimpleAccount> implements AccountListView {
    private static final long serialVersionUID = 1L;

    @Override
    protected AbstractPagedBeanTable<AccountSearchCriteria, SimpleAccount> createBeanTable() {
        return new AccountTableDisplay(CrmTypeConstants.ACCOUNT, AccountTableFieldDef.selected,
                Arrays.asList(AccountTableFieldDef.accountname, AccountTableFieldDef.city,
                        AccountTableFieldDef.phoneoffice, AccountTableFieldDef.email,
                        AccountTableFieldDef.assignUser));
    }

    @Override
    protected DefaultGenericSearchPanel<AccountSearchCriteria> createSearchPanel() {
        return new AccountSearchPanel(true);
    }

    @Override
    protected DefaultMassItemActionHandlerContainer createActionControls() {
        DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

        if (UserUIContext.canAccess(RolePermissionCollections.CRM_ACCOUNT)) {
            container.addDeleteActionItem();
        }

        container.addMailActionItem();
        container.addDownloadPdfActionItem();
        container.addDownloadExcelActionItem();
        container.addDownloadCsvActionItem();

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) {
            container.addMassUpdateActionItem();
        }

        return container;
    }

    @Override
    protected void buildExtraControls() {
        MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton()
                .withListener(clickEvent -> UI.getCurrent().addWindow(new AccountListCustomizeWindow((AccountTableDisplay) tableItem)));
        this.addExtraButton(customizeViewBtn);
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        addComponent(new AccountCrmListNoItemView());
    }
}
