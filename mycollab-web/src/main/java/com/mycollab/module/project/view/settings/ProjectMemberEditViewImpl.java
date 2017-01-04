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
package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectMember;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.web.ui.DoubleField;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

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
        return (beanItem.getId() == null) ? UserUIContext.getMessage(ProjectMemberI18nEnum.NEW) :
                UserUIContext.getMessage(ProjectMemberI18nEnum.DETAIL);
    }

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getDisplayName();
    }

    @Override
    protected FontAwesome initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.MEMBER);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm, true, false, true);
    }

    @Override
    protected AdvancedEditBeanForm<SimpleProjectMember> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DecorFormLayoutFactory(new ProjectMemberFormLayoutFactory());
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

    private class DecorFormLayoutFactory extends WrappedFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        DecorFormLayoutFactory(AbstractFormLayoutFactory formLayoutFactory) {
            this.wrappedLayoutFactory = formLayoutFactory;
        }

        @Override
        public AbstractComponent getLayout() {
            VerticalLayout layout = new VerticalLayout();
            layout.addComponent(wrappedLayoutFactory.getLayout());

            FormContainer permissionsPanel = new FormContainer();
            projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, (ProjectRolePermissionCollections
                    .PROJECT_PERMISSIONS.length + 1) / 2, "180px");
            permissionsPanel.addSection(UserUIContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS),
                    projectFormHelper.getLayout());
            layout.addComponent(permissionsPanel);

            return layout;
        }
    }

    private void displayRolePermission(Integer roleId) {
        projectFormHelper.getLayout().removeAllComponents();
        if (roleId != null && roleId > 0) {
            ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
            SimpleProjectRole role = roleService.findById(roleId, MyCollabUI.getAccountId());
            if (role != null) {
                final PermissionMap permissionMap = role.getPermissionMap();
                for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                    final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                    Enum permissionKey = RolePermissionI18nEnum.valueOf(permissionPath);
                    Integer perVal = permissionMap.get(permissionKey.name());
                    SecurityI18nEnum permissionVal = PermissionFlag.toVal(perVal);
                    projectFormHelper.addComponent(new Label(UserUIContext.getPermissionCaptionValue(
                            permissionMap, permissionPath)), UserUIContext.getMessage(permissionKey),
                            UserUIContext.getMessage(permissionVal.desc()), i % 2, i / 2);
                }
            }
        } else {
            for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                Enum permissionKey = RolePermissionI18nEnum.valueOf(permissionPath);
                projectFormHelper.addComponent(new Label(UserUIContext.getMessage(SecurityI18nEnum.ACCESS)),
                        UserUIContext.getMessage(permissionKey), i % 2, i / 2);
            }
        }
    }

    private class AdminRoleSelectionField extends CustomField<Integer> {
        private static final long serialVersionUID = 1L;
        private ProjectRoleComboBox roleComboBox;

        AdminRoleSelectionField() {
            roleComboBox = new ProjectRoleComboBox();
            roleComboBox.addValueChangeListener(valueChangeEvent -> displayRolePermission((Integer) roleComboBox.getValue()));
        }

        @Override
        public void commit() throws SourceException, InvalidValueException {
            Integer roleId = (Integer) roleComboBox.getValue();
            if (roleId == -1) {
                if (CurrentProjectVariables.isAdmin()) {
                    beanItem.setIsadmin(Boolean.TRUE);
                    this.setInternalValue(null);
                } else {
                    throw new UserInvalidInputException(UserUIContext.getMessage(ProjectRoleI18nEnum.ERROR_ONLY_OWNER_ASSIGN_ROLE_OWNER));
                }
            } else {
                beanItem.setIsadmin(Boolean.FALSE);
                this.setInternalValue((Integer) this.roleComboBox.getValue());
            }

            super.commit();
        }

        @Override
        public void setPropertyDataSource(Property newDataSource) {
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
