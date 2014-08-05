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
