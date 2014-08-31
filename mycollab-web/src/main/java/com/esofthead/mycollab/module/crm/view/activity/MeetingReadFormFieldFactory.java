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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.ui.components.RelatedReadItemField;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormDateTimeViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class MeetingReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleMeeting> {
	private static final long serialVersionUID = 1L;

	public MeetingReadFormFieldFactory(GenericBeanForm<SimpleMeeting> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("type")) {
			return new RelatedReadItemField(attachForm.getBean());
		} else if (propertyId.equals("startdate")) {
			if (attachForm.getBean().getStartdate() == null)
				return null;
			return new FormDateTimeViewField(attachForm.getBean()
					.getStartdate());
		} else if (propertyId.equals("enddate")) {
			if (attachForm.getBean().getEnddate() == null)
				return null;
			return new FormDateTimeViewField(attachForm.getBean()
					.getEnddate());
		} else if (propertyId.equals("isrecurrence")) {
			return null;
		}
		return null;
	}

}
