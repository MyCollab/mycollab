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

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.AccountEvent;
import com.esofthead.mycollab.mobile.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */

@ViewComponent
public class AccountListViewImpl extends
		AbstractListViewComp<AccountSearchCriteria, SimpleAccount> implements
		AccountListView {

	private static final long serialVersionUID = -500810154594390148L;

	public AccountListViewImpl() {
		super();

		setCaption("Accounts");
		// setToggleButton(true);
	}

	@Override
	protected AbstractPagedBeanList<AccountSearchCriteria, SimpleAccount> createBeanTable() {
		AccountListDisplay accountListDisplay = new AccountListDisplay(
				"accountname");

		accountListDisplay
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(final TableClickEvent event) {
						final SimpleAccount account = (SimpleAccount) event
								.getData();
						if ("accountname".equals(event.getFieldName())) {
							EventBus.getInstance().fireEvent(
									new AccountEvent.GotoRead(
											AccountListViewImpl.this, account
													.getId()));
						}
					}
				});
		return accountListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		Button addAccount = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBus.getInstance().fireEvent(
						new AccountEvent.GotoAdd(this, null));
			}
		});
		addAccount.setStyleName("add-btn");
		return addAccount;
	}

}
