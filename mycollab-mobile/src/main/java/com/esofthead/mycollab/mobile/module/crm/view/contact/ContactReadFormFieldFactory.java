/**
 * This file is part of mycollab-mobile.
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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormEmailLinkViewField;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ContactReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleContact> {
	private static final long serialVersionUID = 1L;

	public ContactReadFormFieldFactory(GenericBeanForm<SimpleContact> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("accountid")) {
			return new FormViewField(attachForm.getBean().getAccountName());
		} else if (propertyId.equals("email")) {
			return new FormEmailLinkViewField(attachForm.getBean().getEmail());
		} else if (propertyId.equals("assignuser")) {
			return new FormViewField(attachForm.getBean()
					.getAssignUserFullName());
		} else if (propertyId.equals("iscallable")) {
			if (attachForm.getBean().getIscallable() == null
					|| Boolean.FALSE == attachForm.getBean().getIscallable()) {
				return new FormViewField(
						AppContext.getMessage(GenericI18Enum.BUTTON_NO_LABEL));
			} else {
				return new FormViewField(
						AppContext.getMessage(GenericI18Enum.BUTTON_YES_LABEL));
			}
		} else if (propertyId.equals("birthday")) {
			return new FormViewField(AppContext.formatDate(attachForm.getBean()
					.getBirthday()));
		} else if (propertyId.equals("firstname")) {
			return new FormViewField(attachForm.getBean().getFirstname());
		}

		return null;
	}
}
