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
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.mobile.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.module.crm.domain.CaseWithBLOBs;
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
class CaseEditFormFieldFactory<B extends CaseWithBLOBs> extends
		AbstractBeanFieldGroupEditFieldFactory<B> {
	private static final long serialVersionUID = 1L;

	CaseEditFormFieldFactory(GenericBeanForm<B> form) {
		super(form);
	}

	CaseEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
		super(form, isValidateForm);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("priority")) {
			return new CasePriorityComboBox();
		} else if (propertyId.equals("status")) {
			return new CaseStatusComboBox();
		} else if (propertyId.equals("reason")) {
			return new CaseReasonComboBox();
		} else if (propertyId.equals("origin")) {
			return new CasesOriginComboBox();
		} else if (propertyId.equals("type")) {
			return new CaseTypeComboBox();
		} else if (propertyId.equals("description")) {
			TextArea descArea = new TextArea("", "");
			descArea.setNullRepresentation("");
			return descArea;
		} else if (propertyId.equals("resolution")) {
			TextArea reArea = new TextArea("", "");
			reArea.setNullRepresentation("");
			return reArea;
		} else if (propertyId.equals("accountid")) {
			AccountSelectionField accountField = new AccountSelectionField();
			accountField.setRequired(true);
			return accountField;
		} else if (propertyId.equals("subject")) {
			TextField tf = new TextField();
			if (isValidateForm) {
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Subject must not be null");
			}

			return tf;
		} else if (propertyId.equals("assignuser")) {
			ActiveUserComboBox userBox = new ActiveUserComboBox();
			userBox.select(attachForm.getBean().getAssignuser());
			return userBox;
		}

		return null;
	}
}
