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

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */

@ViewComponent
public class AccountListViewImpl extends AbstractListViewComp<AccountSearchCriteria, SimpleAccount> implements AccountListView {

	private static final long serialVersionUID = -500810154594390148L;

	public AccountListViewImpl() {
		super();

		setCaption("Accounts");
		setToggleButton(true);
	}

	@Override
	protected AbstractPagedBeanList<AccountSearchCriteria, SimpleAccount> createBeanTable() {
		AccountListDisplay accountListDisplay = new AccountListDisplay(
				"accountname");

		accountListDisplay.addTableListener(new ApplicationEventListener<TableClickEvent>() {
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

}
