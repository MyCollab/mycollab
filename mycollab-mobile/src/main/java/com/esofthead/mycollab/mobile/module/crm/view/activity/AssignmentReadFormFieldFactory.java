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
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.mobile.module.crm.ui.RelatedReadItemField;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.DateFieldWithUserTimeZone;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
class AssignmentReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
	private static final long serialVersionUID = 1L;

	public AssignmentReadFormFieldFactory(GenericBeanForm<SimpleTask> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("assignuser")) {
			return new FormViewField(attachForm.getBean()
					.getAssignUserFullName());
		} else if (propertyId.equals("startdate")) {
			if (attachForm.getBean().getStartdate() == null)
				return null;
			return new DateFieldWithUserTimeZone(attachForm.getBean()
					.getStartdate(), "DATETIME_FIELD");
		} else if (propertyId.equals("duedate")) {
			if (attachForm.getBean().getDuedate() == null)
				return null;
			return new DateFieldWithUserTimeZone(attachForm.getBean()
					.getDuedate(), "DATETIME_FIELD");
		} else if (propertyId.equals("contactid")) {
			return new FormViewField(attachForm.getBean().getContactName());
		} else if (propertyId.equals("type")) {
			return new RelatedReadItemField(attachForm.getBean());

		}

		return null;
	}

}
