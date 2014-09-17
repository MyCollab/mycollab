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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class CampaignRelatedContactView extends
		AbstractRelatedListView<SimpleContact, ContactSearchCriteria> {
	private static final long serialVersionUID = 366429952188752174L;
	private SimpleCampaign campaign;

	public CampaignRelatedContactView() {
		super();
		setCaption(AppContext
				.getMessage(ContactI18nEnum.M_TITLE_RELATED_CONTACTS));
		this.itemList = new ContactListDisplay();
		this.setContent(this.itemList);
	}

	private void loadContacts() {
		final ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		searchCriteria.setCampaignId(new NumberSearchField(SearchField.AND,
				this.campaign.getId()));
		this.itemList.setSearchCriteria(searchCriteria);
	}

	public void displayContacts(SimpleCampaign campaign) {
		this.campaign = campaign;
		loadContacts();
	}

	@Override
	public void refresh() {
		loadContacts();
	}

	@Override
	protected Component createRightComponent() {
		final NavigationBarQuickMenu addContact = new NavigationBarQuickMenu();
		addContact.setStyleName("add-btn");

		VerticalLayout addButtons = new VerticalLayout();
		addButtons.setWidth("100%");
		addButtons.setSpacing(true);
		addButtons.setMargin(true);
		addButtons.setStyleName("edit-btn-layout");

		Button newContact = new Button(
				AppContext.getMessage(ContactI18nEnum.VIEW_NEW_TITLE));
		newContact.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent arg0) {
				fireNewRelatedItem("");
			}
		});
		addButtons.addComponent(newContact);

		Button selectContact = new Button(
				AppContext.getMessage(ContactI18nEnum.M_TITLE_SELECT_CONTACTS));
		selectContact.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -429296782998301810L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				CampaignContactSelectionView contactSelectionView = new CampaignContactSelectionView(
						CampaignRelatedContactView.this);
				ContactSearchCriteria criteria = new ContactSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(AppContext
						.getAccountId()));
				contactSelectionView.setSearchCriteria(criteria);
				EventBusFactory.getInstance().post(
						new ShellEvent.PushView(
								CampaignRelatedContactView.this,
								contactSelectionView));
			}
		});
		addButtons.addComponent(selectContact);

		addContact.setContent(addButtons);

		return addContact;
	}

}
