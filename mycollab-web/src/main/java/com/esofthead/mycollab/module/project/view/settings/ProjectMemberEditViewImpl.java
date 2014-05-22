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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.localization.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DummyCustomField;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class ProjectMemberEditViewImpl extends
		AbstractEditItemComp<ProjectMember> implements ProjectMemberEditView {

	private static final long serialVersionUID = 1L;

	public ProjectMemberEditViewImpl() {
		super();
	}

	@Override
	protected String initFormHeader() {
		return (beanItem.getId() == null) ? AppContext
				.getMessage(ProjectMemberI18nEnum.FORM_NEW_TITLE) : AppContext
				.getMessage(ProjectMemberI18nEnum.FORM_EDIT_TITLE);
	}

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() == null) ? null
				: ((SimpleProjectMember) beanItem).getDisplayName();
	}

	@Override
	protected Resource initFormIconResource() {
		return MyCollabResource.newResource("icons/24/project/user.png");
	}

	@Override
	protected ComponentContainer createButtonControls() {
		final HorizontalLayout controlButtons = (new EditFormControlsGenerator<ProjectMember>(
				editForm)).createButtonControls(true, false, true);
		return controlButtons;
	}

	@Override
	protected AdvancedEditBeanForm<ProjectMember> initPreviewForm() {
		return new AdvancedEditBeanForm<ProjectMember>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new ProjectMemberFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<ProjectMember> initBeanFormFieldFactory() {
		return new EditFormFieldFactory(editForm);
	}

	private class EditFormFieldFactory extends
			AbstractBeanFieldGroupEditFieldFactory<ProjectMember> {
		private static final long serialVersionUID = 1L;

		public EditFormFieldFactory(GenericBeanForm<ProjectMember> form) {
			super(form);
		}

		@Override
		protected Field<?> onCreateField(final Object propertyId) {

			if (propertyId.equals("username")) {
				return new DefaultFormViewFieldFactory.FormViewField(
						((SimpleProjectMember) beanItem).getMemberFullName());

			} else if (propertyId.equals("projectroleid")) {
				AdminRoleSelectionField roleBox = new AdminRoleSelectionField();
				return roleBox;
			} else if (propertyId.equals("isadmin")) {
				return new DummyCustomField<Boolean>();
			}
			return null;
		}
	}

	private class AdminRoleSelectionField extends CustomField<Integer> {
		private static final long serialVersionUID = 1L;
		private ProjectRoleComboBox roleComboBox;

		public AdminRoleSelectionField() {
			this.roleComboBox = new ProjectRoleComboBox();
			this.roleComboBox
					.addValueChangeListener(new Property.ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(
								final Property.ValueChangeEvent event) {
							getValue();

						}
					});
		}

		@Override
		public void commit() throws SourceException, InvalidValueException {
			Integer roleId = (Integer) roleComboBox.getValue();
			if (roleId == -1) {
				beanItem.setIsadmin(Boolean.TRUE);
				this.setInternalValue(null);
			} else {
				this.setInternalValue((Integer) this.roleComboBox.getValue());
				beanItem.setIsadmin(Boolean.FALSE);
			}

			super.commit();
		}

		@Override
		public void setPropertyDataSource(Property newDataSource) {
			Object value = newDataSource.getValue();
			if (value instanceof Integer) {
				roleComboBox.setValue(value);
			} else if (value == null) {
				if (beanItem.getIsadmin() != null
						&& beanItem.getIsadmin() == Boolean.TRUE) {
					roleComboBox.setValue(-1);
				}
			}
			super.setPropertyDataSource(newDataSource);
		}

		@Override
		public Class<Integer> getType() {
			return Integer.class;
		}

		@Override
		protected Component initContent() {
			return roleComboBox;
		}
	}

}
