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
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ContactSelectionField extends CustomField<Integer> implements
		FieldSelection<Contact> {
	private static final long serialVersionUID = 1L;

	private HorizontalLayout layout;

	private TextField contactName;

	private SimpleContact contact;

	private Button browseBtn;
	private Button clearBtn;

	public ContactSelectionField() {
		contactName = new TextField();
		contactName.setNullRepresentation("");
		contactName.setWidth("100%");
		browseBtn = new Button(null, FontAwesome.ELLIPSIS_H);
        browseBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
		browseBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ContactSelectionWindow contactWindow = new ContactSelectionWindow(
                        ContactSelectionField.this);
                UI.getCurrent().addWindow(contactWindow);
                contactWindow.show();
            }
        });

		clearBtn = new Button(null, FontAwesome.TRASH_O);
        clearBtn.addStyleName(UIConstants.THEME_GRAY_LINK);

		clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                contactName.setValue("");
                contact = null;
            }
        });
	}

	@Override
	public void fireValueChange(Contact data) {
		contact = (SimpleContact) data;
		if (contact != null) {
			contactName.setValue(contact.getContactName());
			setInternalValue(contact.getId());
		}

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
		contactName.setValue(contact.getContactName());
	}

	public SimpleContact getContact() {
		return this.contact;
	}

	@Override
	protected Component initContent() {
		layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setWidth("100%");

		layout.addComponent(contactName);
		layout.setComponentAlignment(contactName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(contactName, 1.0f);

		layout.addComponent(browseBtn);
		layout.setComponentAlignment(browseBtn, Alignment.MIDDLE_LEFT);

		layout.addComponent(clearBtn);
		layout.setComponentAlignment(clearBtn, Alignment.MIDDLE_LEFT);

		return layout;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
}
