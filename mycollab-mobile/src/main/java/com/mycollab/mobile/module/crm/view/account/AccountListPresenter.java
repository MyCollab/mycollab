package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class AccountListPresenter extends CrmListPresenter<AccountListView, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = -3014478937143932048L;

    public AccountListPresenter() {
        super(AccountListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        super.onGo(container, data);
    }
}
