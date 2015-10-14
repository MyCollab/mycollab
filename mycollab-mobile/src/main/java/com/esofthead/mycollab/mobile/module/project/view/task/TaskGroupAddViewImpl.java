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

import com.esofthead.mycollab.mobile.module.project.view.milestone.MilestoneComboBox;
import com.esofthead.mycollab.mobile.module.project.view.settings.ProjectMemberSelectionField;
import com.esofthead.mycollab.mobile.ui.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.0
 */

@ViewComponent
public class TaskGroupAddViewImpl extends AbstractEditItemComp<SimpleTaskList>
		implements TaskGroupAddView {
	private static final long serialVersionUID = 1L;

	@Override
	public HasEditFormHandlers<SimpleTaskList> getEditFormHandlers() {
		return this.editForm;
	}

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() != null) ? beanItem.getName() : AppContext
				.getMessage(TaskGroupI18nEnum.M_VIEW_NEW_TITLE);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskGroupFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<SimpleTaskList> initBeanFormFieldFactory() {
		return new EditFormFieldFactory(editForm);
	}

	private static class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleTaskList> {
		private static final long serialVersionUID = 1L;

		EditFormFieldFactory(GenericBeanForm<SimpleTaskList> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(Object propertyId) {
			if ("owner".equals(propertyId)) {
				return new ProjectMemberSelectionField();
			} else if ("milestoneid".equals(propertyId)) {
				return new MilestoneComboBox();
			} else if ("description".equals(propertyId)) {
				return new TextArea();
			}

			if ("name".equals(propertyId)) {
				final TextField tf = new TextField();
				tf.setNullRepresentation("");
				tf.setRequired(true);
				tf.setRequiredError("Please enter a Name");
				return tf;
			}

			return null;
		}
	}
}
