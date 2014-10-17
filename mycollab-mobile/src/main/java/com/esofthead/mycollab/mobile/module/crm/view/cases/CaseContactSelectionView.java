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
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class CaseContactSelectionView extends
		AbstractRelatedItemSelectionView<SimpleContact, ContactSearchCriteria> {

	private static final long serialVersionUID = 6507450165757528468L;

	public CaseContactSelectionView(
			AbstractRelatedListView<SimpleContact, ContactSearchCriteria> relatedListView) {
		super(AppContext.getMessage(ContactI18nEnum.M_TITLE_SELECT_CONTACTS),
				relatedListView);
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

							private static final long serialVersionUID = 5941188641800994460L;

							@Override
							public void buttonClick(Button.ClickEvent event) {
								if (b.isSelected())
									selections.add(obj);
								else
									selections.remove(obj);

							}
						});
						return b;
					}
				});
	}

}
