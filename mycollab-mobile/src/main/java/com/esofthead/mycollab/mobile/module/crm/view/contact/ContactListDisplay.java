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

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ContactListDisplay
		extends
		DefaultPagedBeanList<ContactService, ContactSearchCriteria, SimpleContact> {
	private static final long serialVersionUID = -2234454107835680053L;

	public ContactListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(ContactService.class),
				SimpleContact.class, displayColumnId);

		addGeneratedColumn("contactName", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					final Object columnId) {
				final SimpleContact contact = ContactListDisplay.this
						.getBeanByIndex(itemId);
				final NavigationButton b = new NavigationButton(contact
						.getContactName());
				b.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							final NavigationButton.NavigationButtonClickEvent event) {
						fireTableEvent(new TableClickEvent(
								ContactListDisplay.this, contact, "contactName"));
					}
				});
				b.setWidth("100%");
				return b;
			}
		});
	}
}
