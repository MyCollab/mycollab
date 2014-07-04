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
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class OpportunityRelatedContactView extends
		AbstractRelatedListView<SimpleContact, ContactSearchCriteria> {
	private static final long serialVersionUID = 1599508020335748835L;
	private SimpleOpportunity opportunity;

	public OpportunityRelatedContactView() {
		super();

		setCaption("Related Contacts");
		this.itemList = new ContactListDisplay();
		this.setContent(itemList);
	}

	private void loadContacts() {
		final ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		searchCriteria.setOpportunityId(new NumberSearchField(SearchField.AND,
				this.opportunity.getId()));
		this.itemList.setSearchCriteria(searchCriteria);
	}

	public void displayContacts(SimpleOpportunity opportunity) {
		this.opportunity = opportunity;
		loadContacts();
	}

	@Override
	public void refresh() {
		loadContacts();
	}

}
