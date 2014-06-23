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
package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.mobile.ui.CountryComboBox;
import com.esofthead.mycollab.mobile.ui.IndustryComboBox;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class AccountEditFormFieldFactory<B extends Account> extends
		AbstractBeanFieldGroupEditFieldFactory<B> {

	private static final long serialVersionUID = 1L;

	public AccountEditFormFieldFactory(GenericBeanForm<B> form) {
		super(form);
	}

	AccountEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
		super(form, isValidateForm);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if ("type".equals(propertyId)) {
			AccountTypeComboBox accountTypeBox = new AccountTypeComboBox();
			return accountTypeBox;
		} else if ("industry".equals(propertyId)) {
			IndustryComboBox accountIndustryBox = new IndustryComboBox();
			return accountIndustryBox;
		} else if ("assignuser".equals(propertyId)) {
			ActiveUserComboBox userBox = new ActiveUserComboBox();
			userBox.select(attachForm.getBean().getAssignuser());
			return userBox;
		} else if ("description".equals(propertyId)) {
			TextArea textArea = new TextArea("", "");
			textArea.setNullRepresentation("");
			return textArea;
		} else if ("billingcountry".equals(propertyId)
				|| "shippingcountry".equals(propertyId)) {
			CountryComboBox billingCountryComboBox = new CountryComboBox();
			return billingCountryComboBox;
		} else if (propertyId.equals("accountname")) {
			TextField tf = new TextField();
			if (isValidateForm) {
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError(AppContext
						.getMessage(AccountI18nEnum.ERROR_ACCOUNT_NAME_IS_NULL));
			}

			return tf;
		}

		return null;
	}

}
