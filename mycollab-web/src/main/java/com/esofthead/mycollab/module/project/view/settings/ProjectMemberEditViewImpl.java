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

import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.ui.AssetsManager;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;

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

	private VerticalLayout permissionsPanel;
	private GridFormLayoutHelper projectFormHelper;

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
		return AssetsManager.getAsset(ProjectTypeConstants.MEMBER);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return (new EditFormControlsGenerator<>(editForm)).createButtonControls(true, false, true);
	}

	@Override
	protected AdvancedEditBeanForm<ProjectMember> initPreviewForm() {
		return new AdvancedEditBeanForm<>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DecorFormLayourFactory(new ProjectMemberFormLayoutFactory());
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
				return new DefaultViewField(
						((SimpleProjectMember) beanItem).getMemberFullName());

			} else if (propertyId.equals("projectroleid")) {
				return new AdminRoleSelectionField();
			} else if (propertyId.equals("isadmin")) {
				return new DummyCustomField<Boolean>();
			}
			return null;
		}
	}

	private class DecorFormLayourFactory implements IFormLayoutFactory {
		private static final long serialVersionUID = 1L;
		private IFormLayoutFactory formLayoutFactory;

		DecorFormLayourFactory(IFormLayoutFactory formLayoutFactory) {
			this.formLayoutFactory = formLayoutFactory;
		}

		@Override
		public ComponentContainer getLayout() {
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(formLayoutFactory.getLayout());

			permissionsPanel = new VerticalLayout();
			final Label organizationHeader = new Label(
					AppContext
							.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS));
			organizationHeader.setStyleName("h2");
			permissionsPanel.addComponent(organizationHeader);

			projectFormHelper = new GridFormLayoutHelper(
					2,
					ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length,
					"100%", "167px", Alignment.TOP_LEFT);
			projectFormHelper.getLayout().setWidth("100%");
			projectFormHelper.getLayout().setMargin(false);
			projectFormHelper.getLayout().addStyleName("colored-gridlayout");

			permissionsPanel.addComponent(projectFormHelper.getLayout());
			layout.addComponent(permissionsPanel);

			return layout;
		}

		@Override
		public void attachField(Object propertyId, Field<?> field) {
			formLayoutFactory.attachField(propertyId, field);
		}
	}

	private void displayRolePermission(Integer roleId) {
		projectFormHelper.getLayout().removeAllComponents();
		if (roleId != null && roleId > 0) {
			ProjectRoleService roleService = ApplicationContextUtil
					.getSpringBean(ProjectRoleService.class);
			SimpleProjectRole role = roleService.findById(roleId,
					AppContext.getAccountId());
			if (role != null) {
				final PermissionMap permissionMap = role.getPermissionMap();
				for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
					final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
					projectFormHelper.addComponent(
							new Label(AppContext.getPermissionCaptionValue(
									permissionMap, permissionPath)), AppContext
									.getMessage(RolePermissionI18nEnum
											.valueOf(permissionPath)), 0, i);
				}
			}
		} else {
			for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
				final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
				projectFormHelper.addComponent(
						new Label(AppContext
								.getMessage(SecurityI18nEnum.ACCESS)),
						permissionPath, 0, i);
			}
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
							displayRolePermission((Integer) roleComboBox
									.getValue());

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
		public void setPropertyDataSource(
				@SuppressWarnings("rawtypes") Property newDataSource) {
			Object value = newDataSource.getValue();
			if (value instanceof Integer) {
				roleComboBox.setValue(value);
				displayRolePermission((Integer) roleComboBox.getValue());
			} else if (value == null) {
				if (beanItem.getIsadmin() != null
						&& beanItem.getIsadmin() == Boolean.TRUE) {
					roleComboBox.setValue(-1);
					displayRolePermission(null);
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
