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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AccountRelatedContactView extends
		AbstractRelatedListView<SimpleContact, ContactSearchCriteria> {
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
		this.setCaption(AppContext
				.getMessage(ContactI18nEnum.M_TITLE_RELATED_CONTACTS));
		this.itemList = new ContactListDisplay();
		this.setContent(itemList);
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

	@Override
	protected Component createRightComponent() {
		final Popover controlBtns = new Popover();
		controlBtns.setClosable(true);
		controlBtns.setStyleName("controls-popover");

		VerticalLayout addButtons = new VerticalLayout();
		addButtons.setSpacing(true);
		addButtons.setWidth("100%");
		addButtons.setMargin(true);
		addButtons.addStyleName("edit-btn-layout");

		NavigationButton newContact = new NavigationButton();
		newContact.setTargetViewCaption(AppContext
				.getMessage(ContactI18nEnum.VIEW_NEW_TITLE));
		newContact
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent arg0) {
						controlBtns.close();
						fireNewRelatedItem("");
					}
				});
		addButtons.addComponent(newContact);

		NavigationButton selectContact = new NavigationButton();
		selectContact.setTargetViewCaption("Select Contacts");
		selectContact
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 243969948418203441L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent event) {
						controlBtns.close();
						AccountContactSelectionView contactSelectionView = new AccountContactSelectionView(
								AccountRelatedContactView.this);
						final ContactSearchCriteria criteria = new ContactSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						contactSelectionView.setSearchCriteria(criteria);
						EventBusFactory.getInstance().post(
								new CrmEvent.PushView(
										AccountRelatedContactView.this,
										contactSelectionView));

					}
				});
		addButtons.addComponent(selectContact);

		controlBtns.setContent(addButtons);

		final Button addContact = new Button();
		addContact.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1920289198458066344L;

			@Override
			public void buttonClick(ClickEvent evt) {
				if (!controlBtns.isAttached())
					controlBtns.showRelativeTo(addContact);
				else
					controlBtns.close();
			}
		});
		addContact.setStyleName("add-btn");
		return addContact;
	}

}
