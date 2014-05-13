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

import com.esofthead.mycollab.mobile.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadSourceComboBox;
import com.esofthead.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.CompoundCustomField;
import com.esofthead.mycollab.vaadin.ui.CountryComboBox;
import com.esofthead.mycollab.vaadin.ui.DateComboboxSelectionField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.PrefixNameComboBox;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
class ContactEditFormFieldFactory<B extends Contact> extends
		AbstractBeanFieldGroupEditFieldFactory<B> {
	private static final long serialVersionUID = 1L;

	private ContactFirstNamePrefixField firstNamePrefixField;

	ContactEditFormFieldFactory(GenericBeanForm<B> form) {
		this(form, true);
	}

	ContactEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
		super(form, isValidateForm);

		firstNamePrefixField = new ContactFirstNamePrefixField();
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("firstname") || propertyId.equals("prefix")) {
			return firstNamePrefixField;
		} else if (propertyId.equals("leadsource")) {
			LeadSourceComboBox leadSource = new LeadSourceComboBox();
			return leadSource;
		} else if (propertyId.equals("accountid")) {
			AccountSelectionField accountField = new AccountSelectionField();
			return accountField;
		} else if (propertyId.equals("lastname")) {
			TextField tf = new TextField();
			if (isValidateForm) {
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Last name must not be null");
			}

			return tf;
		} else if (propertyId.equals("description")) {
			TextArea descArea = new TextArea();
			descArea.setNullRepresentation("");
			return descArea;
		} else if (propertyId.equals("assignuser")) {
			ActiveUserComboBox userBox = new ActiveUserComboBox();
			userBox.select(attachForm.getBean().getAssignuser());
			return userBox;
		} else if (propertyId.equals("primcountry")
				|| propertyId.equals("othercountry")) {
			CountryComboBox otherCountryComboBox = new CountryComboBox();
			return otherCountryComboBox;
		} else if (propertyId.equals("birthday")) {
			return new DateComboboxSelectionField();
		}
		return null;
	}

	class ContactFirstNamePrefixField extends CompoundCustomField<Contact> {
		private static final long serialVersionUID = 1L;

		@Override
		protected Component initContent() {
			HorizontalLayout layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);

			final PrefixNameComboBox prefixSelect = new PrefixNameComboBox();
			prefixSelect.setValue(attachForm.getBean().getPrefix());
			layout.addComponent(prefixSelect);

			prefixSelect
					.addValueChangeListener(new Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(Property.ValueChangeEvent event) {
							attachForm.getBean().setPrefix(
									(String) prefixSelect.getValue());

						}
					});

			TextField firstnameTxtField = new TextField();
			firstnameTxtField.setWidth("100%");
			firstnameTxtField.setNullRepresentation("");
			layout.addComponent(firstnameTxtField);
			layout.setExpandRatio(firstnameTxtField, 1.0f);

			// binding field group
			fieldGroup.bind(prefixSelect, "prefix");
			fieldGroup.bind(firstnameTxtField, "firstname");

			return layout;
		}

		@Override
		public Class<? extends Contact> getType() {
			return Contact.class;
		}

	}
}
