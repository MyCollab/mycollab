package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class AccountListDisplay
extends
DefaultPagedBeanList<AccountService, AccountSearchCriteria, SimpleAccount> {
	private static final long serialVersionUID = 1491890029721763281L;

	public AccountListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(AccountService.class),
				SimpleAccount.class, displayColumnId);

		addGeneratedColumn("accountname", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleAccount account = AccountListDisplay.this
						.getBeanByIndex(itemId);

				final Button b = new Button(account.getAccountname(),
						new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							final Button.ClickEvent event) {
						fireTableEvent(new TableClickEvent(
								AccountListDisplay.this, account,
								"accountname"));
					}
				});
				b.setWidth("100%");
				return b;
			}
		});
	}

}
