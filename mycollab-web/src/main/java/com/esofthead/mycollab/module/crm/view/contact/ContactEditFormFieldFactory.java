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
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.module.crm.view.lead.LeadSourceComboBox;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.CompoundCustomField;
import com.esofthead.mycollab.vaadin.ui.DateSelectionField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.CountryComboBox;
import com.esofthead.mycollab.vaadin.web.ui.PrefixNameComboBox;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ContactEditFormFieldFactory<B extends Contact> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    private ContactFirstNamePrefixField firstNamePrefixField;

    ContactEditFormFieldFactory(GenericBeanForm<B> form) {
        this(form, true);
    }

    ContactEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
        super(form, isValidateForm);
        firstNamePrefixField = new ContactFirstNamePrefixField();
        firstNamePrefixField.setWidth("100%");
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if (propertyId.equals("firstname") || propertyId.equals("prefix")) {
            return firstNamePrefixField;
        } else if (propertyId.equals("leadsource")) {
            return new LeadSourceComboBox();
        } else if (propertyId.equals("accountid")) {
            return new AccountSelectionField();
        } else if (propertyId.equals("lastname")) {
            TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("Last name must not be null");
            }

            return tf;
        } else if (propertyId.equals("description")) {
            return new RichTextArea();
        } else if (propertyId.equals("assignuser")) {
            ActiveUserComboBox userBox = new ActiveUserComboBox();
            userBox.select(attachForm.getBean().getAssignuser());
            return userBox;
        } else if (propertyId.equals("primcountry") || propertyId.equals("othercountry")) {
            return new CountryComboBox();
        } else if (propertyId.equals("birthday")) {
            return new DateSelectionField();
        }
        return null;
    }

    class ContactFirstNamePrefixField extends CompoundCustomField<Contact> {
        private static final long serialVersionUID = 1L;

        @Override
        protected Component initContent() {
            MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();

            final PrefixNameComboBox prefixSelect = new PrefixNameComboBox();
            prefixSelect.setValue(attachForm.getBean().getPrefix());
            layout.addComponent(prefixSelect);

            prefixSelect.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    attachForm.getBean().setPrefix((String) prefixSelect.getValue());
                }
            });

            TextField firstNameField = new TextField();
            firstNameField.setWidth("100%");
            firstNameField.setNullRepresentation("");
            layout.with(firstNameField).expand(firstNameField);

            // binding field group
            fieldGroup.bind(prefixSelect, "prefix");
            fieldGroup.bind(firstNameField, "firstname");

            return layout;
        }

        @Override
        public Class<? extends Contact> getType() {
            return Contact.class;
        }
    }
}