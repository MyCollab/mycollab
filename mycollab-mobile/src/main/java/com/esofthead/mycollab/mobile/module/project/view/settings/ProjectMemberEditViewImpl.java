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
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;
import com.esofthead.mycollab.mobile.ui.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
@ViewComponent
public class ProjectMemberEditViewImpl extends AbstractEditItemComp<SimpleProjectMember> implements
        ProjectMemberEditView {
    private static final long serialVersionUID = 1483479851089277052L;

    private VerticalComponentGroup permissionGroup;

    public ProjectMemberEditViewImpl() {
        this.addStyleName("member-edit-view");
        this.permissionGroup = new VerticalComponentGroup();
        this.permissionGroup.setWidth("100%");
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getDisplayName();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new ProjectMemberEditFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<SimpleProjectMember> initBeanFormFieldFactory() {
        return new ProjectMemberEditFieldGroupFactory(this.editForm);
    }

    private void displayRolePermission(Integer roleId) {
        permissionGroup.removeAllComponents();
        if (roleId != null && roleId > 0) {
            ProjectRoleService roleService = ApplicationContextUtil
                    .getSpringBean(ProjectRoleService.class);
            SimpleProjectRole role = roleService.findById(roleId, AppContext.getAccountId());
            if (role != null) {
                final PermissionMap permissionMap = role.getPermissionMap();
                for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                    final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                    Label permissionLbl = new Label(
                            AppContext.getPermissionCaptionValue(permissionMap,
                                    permissionPath));
                    permissionLbl.setCaption(AppContext
                            .getMessage(RolePermissionI18nEnum
                                    .valueOf(permissionPath)));
                    permissionGroup.addComponent(permissionLbl);
                }
            }
        } else {
            for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                Label permissionLbl = new Label(
                        AppContext.getMessage(SecurityI18nEnum.ACCESS));
                permissionLbl.setCaption(permissionPath);
                permissionGroup.addComponent(permissionLbl);
            }
        }

    }

    private class ProjectMemberEditFormLayoutFactory implements
            IFormLayoutFactory {

        private static final long serialVersionUID = -6204799792781581979L;
        VerticalComponentGroup fieldGroup;

        @Override
        public ComponentContainer getLayout() {
            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(false);
            Label header = new Label(AppContext
                    .getMessage(ProjectMemberI18nEnum.FORM_INFORMATION_SECTION));
            header.setStyleName("h2");
            layout.addComponent(header);

            fieldGroup = new VerticalComponentGroup();
            fieldGroup.setWidth("100%");

            layout.addComponent(fieldGroup);

            Label permissionSectionHdr = new Label(
                    AppContext
                            .getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS));
            permissionSectionHdr.setStyleName("h2");
            layout.addComponent(permissionSectionHdr);
            layout.addComponent(permissionGroup);

            return layout;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {
            if (propertyId.equals("projectroleid")) {
                field.setCaption(AppContext
                        .getMessage(ProjectMemberI18nEnum.FORM_ROLE));
                fieldGroup.addComponent(field);
            }
        }

    }

    private class ProjectMemberEditFieldGroupFactory extends
            AbstractBeanFieldGroupEditFieldFactory<SimpleProjectMember> {

        private static final long serialVersionUID = 1490026787891513129L;

        public ProjectMemberEditFieldGroupFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("projectroleid")) {
                return new ProjectRoleSelectionField();
            }
            return null;
        }

    }

    private class ProjectRoleSelectionField extends CustomField<Integer> {
        private static final long serialVersionUID = 1L;
        private ProjectRoleComboBox roleComboBox;

        public ProjectRoleSelectionField() {
            this.roleComboBox = new ProjectRoleComboBox();
            this.roleComboBox.addValueChangeListener(new Property.ValueChangeListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void valueChange(
                                final Property.ValueChangeEvent event) {
                            displayRolePermission((Integer) roleComboBox.getValue());

                        }
                    });
            this.roleComboBox.setWidth("100%");
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
