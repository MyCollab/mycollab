package com.esofthead.mycollab.module.crm.view.campaign;

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

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class CampaignContactSelectionWindow2 extends
RelatedItemSelectionWindow2<SimpleContact, ContactSearchCriteria> {

	public CampaignContactSelectionWindow2(
			CampaignContactListComp2 associateContactList) {
		super("Select Contacts", associateContactList);

		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		tableItem = new ContactTableDisplay(ContactTableFieldDef.selected,
				Arrays.asList(ContactTableFieldDef.name,
						ContactTableFieldDef.email,
						ContactTableFieldDef.phoneOffice,
						ContactTableFieldDef.account));

		Button selectBtn = new Button("Select", new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				close();
			}
		});
		selectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		ContactSimpleSearchPanel contactSimpleSearchPanel = new ContactSimpleSearchPanel();
		contactSimpleSearchPanel
		.addSearchHandler(new SearchHandler<ContactSearchCriteria>() {

			@Override
			public void onSearch(ContactSearchCriteria criteria) {
				tableItem.setSearchCriteria(criteria);
			}

		});

		this.bodyContent.addComponent(contactSimpleSearchPanel);
		this.bodyContent.addComponent(selectBtn);
		this.bodyContent.addComponent(tableItem);
	}

}
