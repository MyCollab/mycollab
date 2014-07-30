/**
 * 
 */
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class CampaignContactSelectionView extends
		AbstractRelatedItemSelectionView<SimpleContact, ContactSearchCriteria> {

	private static final long serialVersionUID = -4814745879533635783L;

	public CampaignContactSelectionView(
			AbstractRelatedListView<SimpleContact, ContactSearchCriteria> relatedListView) {
		super("Select Contacts", relatedListView);
	}

	@Override
	protected void initUI() {
		this.itemList = new ContactListDisplay();
		this.itemList
				.setRowDisplayHandler(new AbstractPagedBeanList.RowDisplayHandler<SimpleContact>() {

					@Override
					public Component generateRow(final SimpleContact obj,
							int rowIndex) {
						final SelectableButton b = new SelectableButton(obj
								.getContactName());
						if (selections.contains(obj))
							b.select();
						b.addClickListener(new Button.ClickListener() {

							private static final long serialVersionUID = 7411309155870615939L;

							@Override
							public void buttonClick(Button.ClickEvent event) {
								if (b.isSelected())
									selections.add(obj);
								else
									selections.remove(obj);
							}
						});
						return null;
					}
				});
	}

}
