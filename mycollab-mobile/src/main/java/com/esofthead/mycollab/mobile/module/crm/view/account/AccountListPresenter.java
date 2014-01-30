package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.ui.ListPresenter;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class AccountListPresenter extends ListPresenter<AccountListView, AccountSearchCriteria, SimpleAccount> {
	private static final long serialVersionUID = -3014478937143932048L;

	public AccountListPresenter() {
		super(AccountListView.class);
	}

	@Override
	protected void onGo(MobileNavigationManager navigationManager, ScreenData<?> data) {
		navigationManager.navigateTo(view.getWidget());
		doSearch((AccountSearchCriteria) data.getParams());
	}

}
