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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormEmailLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.UserLinkViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class LeadReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleLead> {
	private static final long serialVersionUID = 1L;

	public LeadReadFormFieldFactory(GenericBeanForm<SimpleLead> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		SimpleLead lead = attachForm.getBean();
		if (propertyId.equals("firstname")) {
			String prefix = "", firstName = "";
			if (lead.getPrefixname() != null) {
				prefix = lead.getPrefixname();
			}

			if (lead.getFirstname() != null) {
				firstName = lead.getFirstname();
			}

			return new FormViewField(prefix + firstName);
		} else if (propertyId.equals("website")) {
			return new DefaultFormViewFieldFactory.FormUrlLinkViewField(
					lead.getWebsite());
		} else if (propertyId.equals("email")) {
			return new FormEmailLinkViewField(lead.getEmail());
		} else if (propertyId.equals("accountid")) {
			FormLinkViewField field = new FormLinkViewField(
					lead.getAccountname(), null, null);

			return field;
		} else if (propertyId.equals("assignuser")) {
			return new UserLinkViewField(lead.getAssignuser(),
					lead.getAssignUserAvatarId(), lead.getAssignUserFullName());
		}

		return null;
	}

}
