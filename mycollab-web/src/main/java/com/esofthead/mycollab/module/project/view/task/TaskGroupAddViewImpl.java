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

package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.ui.components.ProjectMilestoneComboBox;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProgressPercentageIndicator;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope=ViewScope.PROTOTYPE)
public class TaskGroupAddViewImpl extends AbstractEditItemComp<TaskList>
		implements TaskGroupAddView {
	private static final long serialVersionUID = 1L;

	@Override
	public HasEditFormHandlers<TaskList> getEditFormHandlers() {
		return this.editForm;
	}

	@Override
	protected String initFormHeader() {
		return (beanItem.getId() != null) ? AppContext
				.getMessage(TaskGroupI18nEnum.FORM_EDIT_TASKGROUP_TITLE)
				: AppContext
						.getMessage(TaskGroupI18nEnum.FORM_NEW_TASKGROUP_TITLE);
	}

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() != null) ? beanItem.getName() : null;
	}

	@Override
	protected Resource initFormIconResource() {
		return MyCollabResource.newResource("icons/24/project/task.png");
	}

	@Override
	protected ComponentContainer createButtonControls() {
		final Layout controlButtons = (new EditFormControlsGenerator<TaskList>(
				editForm)).createButtonControls();
		return controlButtons;
	}

	@Override
	protected AdvancedEditBeanForm<TaskList> initPreviewForm() {
		return new AdvancedEditBeanForm<TaskList>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskGroupFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<TaskList> initBeanFormFieldFactory() {
		return new EditFormFieldFactory(editForm);
	}

	private class EditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<TaskList> {
		private static final long serialVersionUID = 1L;

		public EditFormFieldFactory(GenericBeanForm<TaskList> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(Object propertyId) {
			if ("owner".equals(propertyId)) {
				return new ProjectMemberSelectionField();
			} else if ("milestoneid".equals(propertyId)) {
				return new ProjectMilestoneComboBox();
			} else if ("description".equals(propertyId)) {
				return new RichTextArea();
			}

			if ("name".equals(propertyId)) {
				final TextField tf = new TextField();
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Please enter a Name");
				return tf;
			} else if (propertyId.equals("percentageComplete")) {
				final double percentage = (beanItem instanceof SimpleTaskList) ? ((SimpleTaskList) beanItem)
						.getPercentageComplete() : 0;
				final FormContainerHorizontalViewField fieldContainer = new FormContainerHorizontalViewField();
				final ProgressPercentageIndicator progressField = new ProgressPercentageIndicator(
						percentage);
				fieldContainer.addComponentField(progressField);
				return fieldContainer;
			} else if (propertyId.equals("numOpenTasks")) {
				final int openTask = (beanItem instanceof SimpleTaskList) ? ((SimpleTaskList) beanItem)
						.getNumOpenTasks() : 0;
				final int allTasks = (beanItem instanceof SimpleTaskList) ? ((SimpleTaskList) beanItem)
						.getNumAllTasks() : 0;
				final FormContainerHorizontalViewField fieldContainer = new FormContainerHorizontalViewField();
				final Label numTaskLbl = new Label("(" + openTask + "/"
						+ allTasks + ")");
				fieldContainer.addComponentField(numTaskLbl);
				return fieldContainer;
			}

			return null;
		}
	}
}
