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
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectMemberSelectionField;
import com.esofthead.mycollab.mobile.ui.AbstractEditItemComp;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 */

@ViewComponent
public class TaskAddViewImpl extends AbstractEditItemComp<SimpleTask> implements
		TaskAddView {

	private static final long serialVersionUID = 6835605062072536907L;

	private ProjectFormAttachmentUploadField attachmentUploadField;

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() == null) ? AppContext
				.getMessage(TaskI18nEnum.FORM_NEW_TASK_TITLE) : beanItem
				.getTaskname();
	}

	@Override
	public void editItem(SimpleTask item) {
		attachmentUploadField = new ProjectFormAttachmentUploadField();
		if (item.getId() != null) {
			attachmentUploadField.getAttachments(item.getProjectid(),
					AttachmentType.PROJECT_TASK_TYPE, item.getId());
		}
		super.editItem(item);
		this.editForm.addComponent(attachmentUploadField);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<SimpleTask> initBeanFormFieldFactory() {
		return new TaskEditFormFieldFactory(this.editForm);
	}

	public class TaskEditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<SimpleTask> {

		private static final long serialVersionUID = -1508613237858970400L;

		public TaskEditFormFieldFactory(GenericBeanForm<SimpleTask> form) {
			super(form);
		}

		public TaskEditFormFieldFactory(GenericBeanForm<SimpleTask> form,
				boolean isValidateForm) {
			super(form, isValidateForm);
		}

		@Override
		protected Field<?> onCreateField(Object propertyId) {
			if (propertyId.equals("assignuser")) {
				return new ProjectMemberSelectionField();
			} else if (propertyId.equals("tasklistid")) {
				return new TaskListSelectionField();
			} else if (propertyId.equals("notes")) {
				final TextArea textArea = new TextArea();
				textArea.setNullRepresentation("");
				return textArea;
			} else if ("name".equals(propertyId)) {
				final TextField tf = new TextField();
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Please enter a Name");
				return tf;
			} else if ("percentagecomplete".equals(propertyId)) {
				return new TaskPercentageCompleteComboBox();
			} else if ("priority".equals(propertyId)) {
				return new TaskPriorityComboBox();
			} else if (propertyId.equals("startdate")
					|| propertyId.equals("actualstartdate")
					|| propertyId.equals("enddate")
					|| propertyId.equals("actualenddate")
					|| propertyId.equals("deadline")) {
				return new DatePicker();
			}
			return null;
		}

	}

	@Override
	public ProjectFormAttachmentUploadField getAttachUploadField() {
		return attachmentUploadField;
	}

}
