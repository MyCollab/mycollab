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
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.DoubleField;
import com.esofthead.mycollab.vaadin.web.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberEditViewImpl extends AbstractEditItemComp<SimpleProjectMember> implements ProjectMemberEditView {
    private static final long serialVersionUID = 1L;

    private GridFormLayoutHelper projectFormHelper;

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? AppContext.getMessage(ProjectMemberI18nEnum.FORM_NEW_TITLE) :
                AppContext.getMessage(ProjectMemberI18nEnum.FORM_EDIT_TITLE);
    }

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getDisplayName();
    }

    @Override
    protected Resource initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return (new EditFormControlsGenerator<>(editForm)).createButtonControls(true, false, true);
    }

    @Override
    protected AdvancedEditBeanForm<SimpleProjectMember> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DecorFormLayourFactory(new ProjectMemberFormLayoutFactory());
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<SimpleProjectMember> initBeanFormFieldFactory() {
        return new EditFormFieldFactory(editForm);
    }

    private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleProjectMember> {
        private static final long serialVersionUID = 1L;

        public EditFormFieldFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("memberFullName")) {
                return new DefaultViewField(beanItem.getMemberFullName());
            } else if (propertyId.equals("projectroleid")) {
                return new AdminRoleSelectionField();
            } else if (propertyId.equals("isadmin")) {
                return new DummyCustomField<Boolean>();
            } else if (ProjectMember.Field.billingrate.equalTo(propertyId)) {
                return new DoubleField();
            } else if (ProjectMember.Field.overtimebillingrate.equalTo(propertyId)) {
                return new DoubleField();
            }
            return null;
        }
    }

    private class DecorFormLayourFactory implements IWrappedFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private IFormLayoutFactory formLayoutFactory;

        DecorFormLayourFactory(IFormLayoutFactory formLayoutFactory) {
            this.formLayoutFactory = formLayoutFactory;
        }

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout layout = new VerticalLayout();
            layout.addComponent(formLayoutFactory.getLayout());

            FormContainer permissionsPanel = new FormContainer();
            projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, (ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length + 1) / 2);
            permissionsPanel.addSection(AppContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS),
                    projectFormHelper.getLayout());
            layout.addComponent(permissionsPanel);

            return layout;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {
            formLayoutFactory.attachField(propertyId, field);
        }

        @Override
        public IFormLayoutFactory getWrappedFactory() {
            return formLayoutFactory;
        }
    }

    private void displayRolePermission(Integer roleId) {
        projectFormHelper.getLayout().removeAllComponents();
        if (roleId != null && roleId > 0) {
            ProjectRoleService roleService = ApplicationContextUtil.getSpringBean(ProjectRoleService.class);
            SimpleProjectRole role = roleService.findById(roleId, AppContext.getAccountId());
            if (role != null) {
                final PermissionMap permissionMap = role.getPermissionMap();
                for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                    final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                    projectFormHelper.addComponent(new Label(AppContext.getPermissionCaptionValue(
                            permissionMap, permissionPath)), AppContext.getMessage(RolePermissionI18nEnum
                            .valueOf(permissionPath)), i % 2, i / 2);
                }
            }
        } else {
            for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                projectFormHelper.addComponent(new Label(AppContext.getMessage(SecurityI18nEnum.ACCESS)),
                        permissionPath, i % 2, i / 2);
            }
        }
    }

    private class AdminRoleSelectionField extends CustomField<Integer> {
        private static final long serialVersionUID = 1L;
        private ProjectRoleComboBox roleComboBox;

        public AdminRoleSelectionField() {
            this.roleComboBox = new ProjectRoleComboBox();
            this.roleComboBox.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    displayRolePermission((Integer) roleComboBox.getValue());
                }
            });
        }

        @Override
        public void commit() throws SourceException, InvalidValueException {
            Integer roleId = (Integer) roleComboBox.getValue();
            if (roleId == -1) {
                if (CurrentProjectVariables.isAdmin()) {
                    beanItem.setIsadmin(Boolean.TRUE);
                    this.setInternalValue(null);
                } else {
                    throw new UserInvalidInputException("Only the Project Owner can assign the role Project " +
                            "Owner to the user");
                }
            } else {
                beanItem.setIsadmin(Boolean.FALSE);
                this.setInternalValue((Integer) this.roleComboBox.getValue());
            }

            super.commit();
        }

        @Override
        public void setPropertyDataSource(@SuppressWarnings("rawtypes") Property newDataSource) {
            Object value = newDataSource.getValue();
            if (value instanceof Integer) {
                roleComboBox.setValue(value);
                displayRolePermission((Integer) roleComboBox.getValue());
            } else if (value == null) {
                if (Boolean.TRUE.equals(beanItem.getIsadmin())) {
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
