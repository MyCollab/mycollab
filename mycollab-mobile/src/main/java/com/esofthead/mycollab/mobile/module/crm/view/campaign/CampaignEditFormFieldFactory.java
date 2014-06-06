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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.mobile.ui.CurrencyComboBoxField;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 * @param <B>
 */
class CampaignEditFormFieldFactory<B extends CampaignWithBLOBs> extends
		AbstractBeanFieldGroupEditFieldFactory<B> {
	private static final long serialVersionUID = 1L;

	CampaignEditFormFieldFactory(GenericBeanForm<B> form) {
		super(form);
	}

	CampaignEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
		super(form, isValidateForm);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {

		if ("type".equals(propertyId)) {
			CampaignTypeComboBox typeCombo = new CampaignTypeComboBox();
			typeCombo.setWidth("100%");
			return typeCombo;
		} else if ("status".equals(propertyId)) {
			CampaignStatusComboBox statusCombo = new CampaignStatusComboBox();
			statusCombo.setWidth("100%");
			return statusCombo;
		} else if ("campaignname".equals(propertyId)) {
			TextField tf = new TextField();
			if (isValidateForm) {
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Name must not be null");
			}

			return tf;
		} else if ("description".equals(propertyId)) {
			TextArea descArea = new TextArea();
			descArea.setNullRepresentation("");
			return descArea;
		} else if ("assignuser".equals(propertyId)) {
			ActiveUserComboBox userBox = new ActiveUserComboBox();
			userBox.select(attachForm.getBean().getAssignuser());
			return userBox;
		} else if (propertyId.equals("currencyid")) {
			return new CurrencyComboBoxField();
		}
		return null;
	}
}
