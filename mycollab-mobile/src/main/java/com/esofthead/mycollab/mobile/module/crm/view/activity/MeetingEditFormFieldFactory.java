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

import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DummyCustomField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class MeetingEditFormFieldFactory extends
		AbstractBeanFieldGroupEditFieldFactory<MeetingWithBLOBs> {

	public MeetingEditFormFieldFactory(GenericBeanForm<MeetingWithBLOBs> form) {
		super(form);
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("subject")) {
			TextField tf = new TextField();
			if (isValidateForm) {
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Subject must not be null");
			}

			return tf;
		} else if (propertyId.equals("status")) {
			return new MeetingStatusComboBox();
		} else if (propertyId.equals("startdate")) {
			return new DatePicker();
		} else if (propertyId.equals("enddate")) {
			return new DatePicker();
		} else if (propertyId.equals("description")) {
			TextArea descArea = new TextArea();
			descArea.setNullRepresentation("");
			return descArea;
		} else if (propertyId.equals("typeid")) {
			return new RelatedItemSelectionField(attachForm.getBean());
		} else if (propertyId.equals("type")) {
			return new DummyCustomField<String>();
		} else if (propertyId.equals("isrecurrence")) {
			return null;
		}
		return null;
	}

}
