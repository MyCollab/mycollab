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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.view.contact.ContactSelectionField;
import com.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.mobile.ui.ValueComboBox;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.domain.Task;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.DummyCustomField;
import com.mycollab.vaadin.ui.GenericBeanForm;
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
public class AssignmentEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Task> {
	private static final long serialVersionUID = 1L;

	public AssignmentEditFormFieldFactory(GenericBeanForm<Task> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("startdate")) {
			return new DatePicker();
		} else if (propertyId.equals("duedate")) {
			return new DatePicker();
		} else if (propertyId.equals("status")) {
			return new TaskStatusComboBox();
		} else if (propertyId.equals("priority")) {
			return new TaskPriorityComboBox();
		} else if (propertyId.equals("description")) {
			TextArea descArea = new TextArea();
			descArea.setNullRepresentation("");
			return descArea;
		} else if (propertyId.equals("contactid")) {
			return new ContactSelectionField();
		} else if (propertyId.equals("subject")) {
			TextField tf = new TextField();

			if (isValidateForm) {
				tf.setRequired(true);
				tf.setRequiredError("Subject must not be null");
				tf.setNullRepresentation("");
			}

			return tf;
		} else if (propertyId.equals("typeid")) {
			return new RelatedItemSelectionField(attachForm.getBean());
		} else if (propertyId.equals("type")) {
			return new DummyCustomField<String>();
		} else if (propertyId.equals("assignuser")) {
			ActiveUserComboBox userBox = new ActiveUserComboBox();
			userBox.select(attachForm.getBean().getAssignuser());
			return userBox;
		}
		return null;
	}

	static class TaskPriorityComboBox extends ValueComboBox {
		private static final long serialVersionUID = 1L;

		public TaskPriorityComboBox() {
			super();
			setCaption(null);
			this.loadData(CrmDataTypeFactory.getTaskPriorities());
		}
	}

	static class TaskStatusComboBox extends ValueComboBox {
		private static final long serialVersionUID = 1L;

		public TaskStatusComboBox() {
			super();
			setCaption(null);
			this.loadData(CrmDataTypeFactory.getTaskStatuses());
		}
	}
}
