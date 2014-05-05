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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormEmailLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.UserLinkViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class CaseReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleCase> {
	private static final long serialVersionUID = 1L;

	public CaseReadFormFieldFactory(GenericBeanForm<SimpleCase> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		final SimpleCase cases = attachForm.getBean();
		if (propertyId.equals("accountid")) {
			return new FormLinkViewField(cases.getAccountName(),
					CrmLinkBuilder.generateAccountPreviewLinkFull(cases.getAccountid()),
					MyCollabResource
							.newResourceLink("icons/16/crm/account.png"));
		} else if (propertyId.equals("email")) {
			return new FormEmailLinkViewField(cases.getEmail());
		} else if (propertyId.equals("assignuser")) {
			return new UserLinkViewField(cases.getAssignuser(),
					cases.getAssignUserAvatarId(),
					cases.getAssignUserFullName());
		}

		return null;
	}

}
