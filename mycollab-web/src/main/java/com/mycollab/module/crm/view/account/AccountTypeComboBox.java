package com.mycollab.module.crm.view.account;

import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountTypeComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public AccountTypeComboBox() {
        setCaption(null);
        this.loadData(CrmDataTypeFactory.accountTypeList);
    }

}
