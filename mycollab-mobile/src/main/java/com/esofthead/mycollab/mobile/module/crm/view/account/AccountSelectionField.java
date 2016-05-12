/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.ui.AbstractSelectionCustomField;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.data.Property;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */

public class AccountSelectionField extends
		AbstractSelectionCustomField<Integer, Account> implements
		FieldSelection<Account> {
	private static final long serialVersionUID = 1L;

	public AccountSelectionField() {
		super(AccountSelectionView.class);
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		Object value = newDataSource.getValue();
		if (value instanceof Integer) {
			setAccountByVal((Integer) value);
		}
		super.setPropertyDataSource(newDataSource);
	}

	@Override
	public void setValue(Integer value) {
		this.setAccountByVal(value);
		super.setValue(value);
	}

	private void setAccountByVal(Integer accountId) {
		AccountService accountService = AppContextUtil
				.getSpringBean(AccountService.class);
		SimpleAccount account = accountService.findById(accountId,
				AppContext.getAccountId());
		if (account != null) {
			setInternalAccount(account);
		}
	}

	private void setInternalAccount(Account account) {
		this.beanItem = account;
		navButton.setCaption(account.getAccountname());
	}

	public Account getAccount() {
		return beanItem;
	}

	@Override
	public void fireValueChange(Account data) {
		setInternalAccount(data);
		setInternalValue(beanItem.getId());
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
}
