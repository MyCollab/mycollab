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
package com.esofthead.mycollab.module.crm.view.account;

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class AccountSelectionWindow extends Window {

	private static final long serialVersionUID = 1L;
	private AccountSearchCriteria searchCriteria;
	private AccountTableDisplay tableItem;
	private FieldSelection<Account> fieldSelection;

	public AccountSelectionWindow(FieldSelection<Account> fieldSelection) {
		super("Account Name Lookup");
		this.setWidth("900px");
		this.setResizable(false);
		this.fieldSelection = fieldSelection;
		this.setModal(true);
	}

	public void show() {
		searchCriteria = new AccountSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);

		createAccountList();

		AccountSimpleSearchPanel accountSimpleSearchPanel = new AccountSimpleSearchPanel();
		accountSimpleSearchPanel
				.addSearchHandler(new SearchHandler<AccountSearchCriteria>() {

					@Override
					public void onSearch(AccountSearchCriteria criteria) {
						tableItem.setSearchCriteria(criteria);
					}

				});
		layout.addComponent(accountSimpleSearchPanel);
		layout.addComponent(tableItem);
		this.setContent(layout);

		tableItem.setSearchCriteria(searchCriteria);
		center();
	}

	@SuppressWarnings("serial")
	private void createAccountList() {
		tableItem = new AccountTableDisplay(Arrays.asList(
				AccountTableFieldDef.accountname, AccountTableFieldDef.city,
				AccountTableFieldDef.assignUser));

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

						ButtonLink b = new ButtonLink(account.getAccountname(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										// TODO Auto-generated method stub
										fieldSelection.fireValueChange(account);
										AccountSelectionWindow.this.close();
									}
								});
						return b;
					}
				});

	}
}
