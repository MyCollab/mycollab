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

import com.esofthead.mycollab.mobile.ui.AbstractSelectionCustomField;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.data.Property;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ContactSelectionField extends AbstractSelectionCustomField<Integer, SimpleContact> {
    private static final long serialVersionUID = 1L;

    public ContactSelectionField() {
        super(ContactSelectionView.class);
    }

    @Override
    public void fireValueChange(SimpleContact data) {
        setInternalContact(data);
        setInternalValue(beanItem.getId());
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        final Object value = newDataSource.getValue();
        if (value instanceof Integer) {
            setContactByVal((Integer) value);
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void setValue(Integer value) {
        this.setContactByVal(value);
        super.setValue(value);
    }

    private void setContactByVal(Integer contactId) {
        ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
        SimpleContact contactVal = contactService.findById(contactId, AppContext.getAccountId());
        if (contactVal != null) {
            setInternalContact(contactVal);
        }
    }

    private void setInternalContact(SimpleContact contact) {
        this.beanItem = contact;
        navButton.setCaption(contact.getContactName());
    }

    public SimpleContact getContact() {
        return this.beanItem;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
