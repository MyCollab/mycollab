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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class AccountSelectionView extends MobileNavigationView {

	private static final long serialVersionUID = 1L;
	private AccountSearchCriteria searchCriteria;
	private AccountListDisplay tableItem;
	private FieldSelection<Account> fieldSelection;

	public AccountSelectionView(FieldSelection<Account> fieldSelection) {
		super();
		createUI();
		this.setCaption("Account Name Lookup");
		this.fieldSelection = fieldSelection;
	}

	public void createUI() {
		searchCriteria = new AccountSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		VerticalLayout layout = new VerticalLayout();

		createAccountList();

		layout.addComponent(tableItem);
		this.setContent(layout);

		tableItem.setSearchCriteria(searchCriteria);
	}

	@SuppressWarnings("serial")
	private void createAccountList() {
		tableItem = new AccountListDisplay("accountname");

		tableItem.setWidth("100%");
		tableItem.addGeneratedColumn("accountname",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleAccount account = tableItem
								.getBeanByIndex(itemId);

						Button b = new Button(account.getAccountname(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										fieldSelection.fireValueChange(account);
										AccountSelectionView.this
												.getNavigationManager()
												.navigateBack();
									}
								});
						return b;
					}
				});

	}
}
