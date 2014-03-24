package com.esofthead.mycollab.module.crm.view.account;

import java.util.Arrays;

import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow2;
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
public class AccountContactSelectionWindow extends
RelatedItemSelectionWindow2<SimpleContact, ContactSearchCriteria> {

	public AccountContactSelectionWindow(
			final AccountContactListComp associateContactList) {
		super("Select Contacts", associateContactList);

		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		this.tableItem = new ContactTableDisplay(ContactTableFieldDef.selected,
				Arrays.asList(ContactTableFieldDef.name,
						ContactTableFieldDef.title,
						ContactTableFieldDef.account,
						ContactTableFieldDef.phoneOffice));

		this.tableItem.addStyleName(UIConstants.LIMITED_HEIGHT_TABLE);

		final Button selectBtn = new Button("Select",
				new Button.ClickListener() {

			@Override
			public void buttonClick(final ClickEvent event) {
				AccountContactSelectionWindow.this.close();
			}
		});
		selectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		final ContactSimpleSearchPanel contactSimpleSearchPanel = new ContactSimpleSearchPanel();
		contactSimpleSearchPanel
		.addSearchHandler(new SearchHandler<ContactSearchCriteria>() {

			@Override
			public void onSearch(final ContactSearchCriteria criteria) {
				AccountContactSelectionWindow.this.tableItem
				.setSearchCriteria(criteria);
			}

		});

		this.bodyContent.addComponent(contactSimpleSearchPanel);
		this.bodyContent.addComponent(selectBtn);
		this.bodyContent.addComponent(this.tableItem);
	}
}