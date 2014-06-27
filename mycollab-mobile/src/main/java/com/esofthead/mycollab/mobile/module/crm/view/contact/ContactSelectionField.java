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

import com.esofthead.mycollab.mobile.ui.MobileNavigationButton;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class ContactSelectionField extends CustomField<Integer> implements
		FieldSelection<SimpleContact> {
	private static final long serialVersionUID = 1L;

	private MobileNavigationButton contactName = new MobileNavigationButton();

	private SimpleContact contact;

	@Override
	public void fireValueChange(SimpleContact data) {
		contact = data;
		contactName.setCaption(contact.getContactName());
		setInternalValue(contact.getId());
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		final Object value = newDataSource.getValue();
		if (value instanceof Integer) {
			setContactByVal((Integer) value);
			super.setPropertyDataSource(newDataSource);
		} else {
			super.setPropertyDataSource(newDataSource);
		}
	}

	@Override
	public void setValue(Integer value) {
		this.setContactByVal(value);
		super.setValue(value);
	}

	private void setContactByVal(Integer contactId) {
		ContactService contactService = ApplicationContextUtil
				.getSpringBean(ContactService.class);
		SimpleContact contactVal = contactService.findById(contactId,
				AppContext.getAccountId());
		if (contactVal != null) {
			setInternalContact(contactVal);
		}
	}

	private void setInternalContact(SimpleContact contact) {
		this.contact = contact;
		contactName.setCaption(contact.getContactName());
	}

	public SimpleContact getContact() {
		return this.contact;
	}

	@Override
	protected Component initContent() {
		contactName.setStyleName("combo-box");
		contactName.setWidth("100%");

		ContactSelectionView contactView = new ContactSelectionView(
				ContactSelectionField.this);
		contactName.setTargetView(contactView);

		return contactName;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
}
