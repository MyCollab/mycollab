package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;

public class AccountRelatedContactView extends AbstractRelatedListView<SimpleContact, ContactSearchCriteria> {
	private static final long serialVersionUID = 6290597056477524107L;
	private Account account;

	public AccountRelatedContactView() {
		initUI();
	}

	public void displayContacts(final Account account) {
		this.account = account;
		loadContacts();
	}

	private void initUI() {
		this.setCaption("Related Contacts");
		this.tableItem = new ContactListDisplay("contactName");
		this.setContent(tableItem);
	}

	private void loadContacts() {
		final ContactSearchCriteria criteria = new ContactSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(SearchField.AND, account
				.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadContacts();
	}

}
