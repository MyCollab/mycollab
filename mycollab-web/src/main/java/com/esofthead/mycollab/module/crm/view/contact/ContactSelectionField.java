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
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactSelectionField extends CustomField<Integer> implements FieldSelection<Contact> {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout layout;
    private TextField contactName;
    private Button browseBtn;
    private Button clearBtn;

    private SimpleContact contact;

    public ContactSelectionField() {
        contactName = new TextField();
        contactName.setNullRepresentation("");
        contactName.setWidth("100%");
        browseBtn = new Button(null, FontAwesome.ELLIPSIS_H);
        browseBtn.addStyleName(UIConstants.BUTTON_OPTION);
        browseBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING);
        browseBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ContactSelectionWindow contactWindow = new ContactSelectionWindow(ContactSelectionField.this);
                UI.getCurrent().addWindow(contactWindow);
                contactWindow.show();
            }
        });

        clearBtn = new Button(null, FontAwesome.TRASH_O);
        clearBtn.addStyleName(UIConstants.BUTTON_OPTION);
        clearBtn.addStyleName(UIConstants.BUTTON_SMALL_PADDING);

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
        ContactService contactService = ApplicationContextUtil.getSpringBean(ContactService.class);
        SimpleContact contactVal = contactService.findById(contactId, AppContext.getAccountId());
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
        layout = new MHorizontalLayout().withWidth("100%");
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        layout.with(contactName, browseBtn, clearBtn).expand(contactName);
        return layout;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
