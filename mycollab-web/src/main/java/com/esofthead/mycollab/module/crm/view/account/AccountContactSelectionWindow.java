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

import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.esofthead.mycollab.module.crm.view.contact.ContactSimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.contact.ContactTableDisplay;
import com.esofthead.mycollab.module.crm.view.contact.ContactTableFieldDef;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AccountContactSelectionWindow extends RelatedItemSelectionWindow<SimpleContact, ContactSearchCriteria> {

	public AccountContactSelectionWindow(AccountContactListComp associateContactList) {
		super("Select Contacts", associateContactList);
		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		this.tableItem = new ContactTableDisplay(ContactTableFieldDef.selected(),
				Arrays.asList(ContactTableFieldDef.name(),
						ContactTableFieldDef.title(),
						ContactTableFieldDef.account(),
						ContactTableFieldDef.phoneOffice()));

		Button selectBtn = new Button("Select", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				AccountContactSelectionWindow.this.close();
			}
		});
		selectBtn.setStyleName(UIConstants.BUTTON_ACTION);

		ContactSimpleSearchPanel contactSimpleSearchPanel = new ContactSimpleSearchPanel();
		contactSimpleSearchPanel.addSearchHandler(new SearchHandler<ContactSearchCriteria>() {

			@Override
			public void onSearch(ContactSearchCriteria criteria) {
				tableItem.setSearchCriteria(criteria);
			}
		});

		this.bodyContent.with(contactSimpleSearchPanel, selectBtn, tableItem);
	}
}