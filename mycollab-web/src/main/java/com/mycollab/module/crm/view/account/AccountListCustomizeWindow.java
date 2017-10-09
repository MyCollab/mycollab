package com.mycollab.module.crm.view.account;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.fielddef.AccountTableFieldDef;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public AccountListCustomizeWindow(AccountTableDisplay table) {
        super(CrmTypeConstants.ACCOUNT, table);
    }

    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(AccountTableFieldDef.accountname,
                AccountTableFieldDef.assignUser, AccountTableFieldDef.city,
                AccountTableFieldDef.email, AccountTableFieldDef.phoneoffice,
                AccountTableFieldDef.website, AccountTableFieldDef.type,
                AccountTableFieldDef.ownership, AccountTableFieldDef.fax);
    }
}
