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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractMobilePageView;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class ContactSelectionView extends AbstractMobilePageView {
	private static final long serialVersionUID = 7742786524816492321L;
	private ContactSearchCriteria searchCriteria;
	private ContactListDisplay tableItem;
	private FieldSelection<SimpleContact> fieldSelection;

	public ContactSelectionView(FieldSelection<SimpleContact> fieldSelection) {
		super();
		createUI();
		this.setCaption("Contact Name Lookup");
		this.fieldSelection = fieldSelection;
	}

	public void createUI() {
		searchCriteria = new ContactSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		createContactList();

		layout.addComponent(tableItem);
		this.setContent(layout);

		tableItem.setSearchCriteria(searchCriteria);

		SimpleContact clearContact = new SimpleContact();
		tableItem.getBeanContainer().addItemAt(0, clearContact);
	}

	@SuppressWarnings("serial")
	private void createContactList() {
		tableItem = new ContactListDisplay("contactName");
		tableItem.setWidth("100%");

		tableItem.addGeneratedColumn("contactName",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleContact contact = tableItem
								.getBeanByIndex(itemId);

						Button b = new Button(contact.getContactName(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										fieldSelection.fireValueChange(contact);
										ContactSelectionView.this
												.getNavigationManager()
												.navigateBack();
									}
								});
						return b;
					}
				});

	}
}
