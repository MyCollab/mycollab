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

import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 */

@ViewComponent
public class TaskGroupReadViewImpl extends
		AbstractPreviewItemComp<SimpleTaskList> implements TaskGroupReadView {

	private static final long serialVersionUID = 8303226753169728418L;

	@Override
	protected void afterPreviewItem() {
	}

	@Override
	protected String initFormTitle() {
		return this.beanItem.getName();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleTaskList> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleTaskList>();
	}

	@Override
	protected void initRelatedComponents() {
		// TODO Add related comments
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new TaskGroupFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList> initBeanFormFieldFactory() {
		return new TaskGroupBeanFieldGroupFactory(this.previewForm);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new ProjectPreviewFormControlsGenerator<SimpleTaskList>(
				this.previewForm)
				.createButtonControls(ProjectRolePermissionCollections.TASKS);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return null;
	}

	private class TaskGroupBeanFieldGroupFactory extends
			AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList> {

		private static final long serialVersionUID = 4554258685587024348L;

		public TaskGroupBeanFieldGroupFactory(
				GenericBeanForm<SimpleTaskList> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(Object propertyId) {
			if (propertyId.equals("milestoneid")) {
				return new FormViewField(beanItem.getMilestoneName());
			} else if (propertyId.equals("owner")) {
				return new FormViewField(beanItem.getOwnerFullName());
			} else if (propertyId.equals("percentageComplete")) {
				final FormViewField progressField = new FormViewField(
						((int) (beanItem.getPercentageComplete() * 100)) / 100
								+ "%");
				return progressField;
			} else if (propertyId.equals("description")) {
				return new FormViewField(beanItem.getDescription(),
						ContentMode.HTML);
			} else if (propertyId.equals("numOpenTasks")) {
				final FormViewField fieldContainer = new FormViewField("("
						+ beanItem.getNumOpenTasks() + "/"
						+ beanItem.getNumAllTasks() + ")");
				return fieldContainer;
			}

			return null;
		}

	}

	@Override
	public HasPreviewFormHandlers<SimpleTaskList> getPreviewFormHandlers() {
		return this.previewForm;
	}

}
