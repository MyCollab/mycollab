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

import com.esofthead.mycollab.form.view.DynaFormLayout;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Task;
import com.esofthead.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.ui.components.RelatedEditItemField;
import com.esofthead.mycollab.module.crm.view.contact.ContactSelectionField;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.DummyCustomField;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ValueComboBox;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class AssignmentAddViewImpl extends AbstractEditItemComp<Task> implements
		AssignmentAddView {
	private static final long serialVersionUID = 1L;

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() == null) ? "New Task" : beanItem.getSubject();
	}

	@Override
	protected Resource initFormIconResource() {
		return MyCollabResource.newResource("icons/22/crm/task.png");
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new EditFormControlsGenerator<Task>(editForm)
				.createButtonControls();
	}

	@Override
	protected AdvancedEditBeanForm<Task> initPreviewForm() {
		return new AdvancedEditBeanForm<Task>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.TASK,
				AssignmentDefaultFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<Task> initBeanFormFieldFactory() {
		return new AssignmentEditFormFieldFactory(editForm);
	}

	private class AssignmentEditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<Task> {
		private static final long serialVersionUID = 1L;

		public AssignmentEditFormFieldFactory(GenericBeanForm<Task> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(Object propertyId) {
			if (propertyId.equals("startdate")) {
				return new DateTimePickerField();
			} else if (propertyId.equals("duedate")) {
				return new DateTimePickerField();
			} else if (propertyId.equals("status")) {
				return new TaskStatusComboBox();
			} else if (propertyId.equals("priority")) {
				return new TaskPriorityComboBox();
			} else if (propertyId.equals("description")) {
				TextArea descArea = new TextArea();
				descArea.setNullRepresentation("");
				return descArea;
			} else if (propertyId.equals("contactid")) {
				ContactSelectionField field = new ContactSelectionField();
				return field;
			} else if (propertyId.equals("subject")) {
				TextField tf = new TextField();

				if (isValidateForm) {
					tf.setRequired(true);
					tf.setRequiredError("Subject must not be null");
					tf.setNullRepresentation("");
				}

				return tf;
			} else if (propertyId.equals("type")) {
				RelatedEditItemField relatedField = new RelatedEditItemField(
						new String[] { CrmTypeConstants.ACCOUNT,
								CrmTypeConstants.CAMPAIGN,
								CrmTypeConstants.CONTACT,
								CrmTypeConstants.LEAD,
								CrmTypeConstants.OPPORTUNITY,
								CrmTypeConstants.CASE }, attachForm.getBean());
				return relatedField;
			} else if (propertyId.equals("typeid")) {
				return new DummyCustomField<Integer>();
			} else if (propertyId.equals("assignuser")) {
				ActiveUserComboBox userBox = new ActiveUserComboBox();
				userBox.select(attachForm.getBean().getAssignuser());
				return userBox;
			}
			return null;
		}
	}

	class TaskPriorityComboBox extends ValueComboBox {

		private static final long serialVersionUID = 1L;

		public TaskPriorityComboBox() {
			super();
			setCaption(null);
			this.loadData(CrmDataTypeFactory.getTaskPriorities());
		}
	}

	class TaskStatusComboBox extends ValueComboBox {

		private static final long serialVersionUID = 1L;

		public TaskStatusComboBox() {
			super();
			setCaption(null);
			this.loadData(CrmDataTypeFactory.getTaskStatuses());
		}
	}
}
