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
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.FormSectionBuilder;
import com.esofthead.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
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
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.EmailViewField;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class ProjectMemberReadViewImpl extends AbstractPreviewItemComp<SimpleProjectMember> implements ProjectMemberReadView {
    private static final long serialVersionUID = 364308373821870384L;

    private VerticalComponentGroup permissionGroup;

    public ProjectMemberReadViewImpl() {
        permissionGroup = new VerticalComponentGroup();
        permissionGroup.setWidth("100%");
    }

    @Override
    protected void afterPreviewItem() {
        displayRolePermission(beanItem.getProjectroleid());
    }

    @Override
    protected String initFormTitle() {
        return this.beanItem.getDisplayName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleProjectMember> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected void initRelatedComponents() {
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new ProjectMemberFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> initBeanFormFieldFactory() {
        return new ProjectMemberBeanFormFieldFactory(this.previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new ProjectPreviewFormControlsGenerator<>(this.previewForm).createButtonControls(
                ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED,
                ProjectRolePermissionCollections.USERS);
    }

    private void displayRolePermission(Integer roleId) {
        permissionGroup.removeAllComponents();
        if (roleId != null && roleId > 0) {
            ProjectRoleService roleService = ApplicationContextUtil.getSpringBean(ProjectRoleService.class);
            SimpleProjectRole role = roleService.findById(roleId, AppContext.getAccountId());
            if (role != null) {
                final PermissionMap permissionMap = role.getPermissionMap();
                for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                    final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                    Label permissionLbl = new Label(AppContext.getPermissionCaptionValue(permissionMap, permissionPath));
                    permissionLbl.setCaption(AppContext.getMessage(RolePermissionI18nEnum.valueOf(permissionPath)));
                    permissionGroup.addComponent(permissionLbl);
                }
            }
        } else {
            for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                Label permissionLbl = new Label(AppContext.getMessage(SecurityI18nEnum.ACCESS));
                permissionLbl.setCaption(permissionPath);
                permissionGroup.addComponent(permissionLbl);
            }
        }
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return null;
    }

    class ProjectMemberFormLayoutFactory implements IFormLayoutFactory {
        private static final long serialVersionUID = 8920529536882351151L;

        private GridFormLayoutHelper informationLayout;

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(false);
            layout.addComponent(FormSectionBuilder.build(AppContext.getMessage(ProjectMemberI18nEnum.FORM_INFORMATION_SECTION)));

            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);
            layout.addComponent(informationLayout.getLayout());
            layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);

            layout.addComponent(FormSectionBuilder.build(AppContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS)));
            layout.addComponent(permissionGroup);
            return layout;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {
            if (propertyId.equals("memberFullName")) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectMemberI18nEnum.FORM_USER), 0, 0);
            } else if (propertyId.equals("email")) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectMemberI18nEnum.M_FORM_EMAIL), 0, 1);
            } else if (propertyId.equals("roleName")) {
                informationLayout.addComponent(field, AppContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 2);
            }
        }
    }

    private class ProjectMemberBeanFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> {
        private static final long serialVersionUID = 5269043189285551214L;

        public ProjectMemberBeanFormFieldFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("memberFullName")) {
                return new DefaultViewField(ProjectLinkBuilder.generateProjectMemberHtmlLink(CurrentProjectVariables
                        .getProjectId(), beanItem.getUsername(), beanItem.getDisplayName(), beanItem
                        .getMemberAvatarId(), false), ContentMode.HTML);
            } else if (propertyId.equals("roleName")) {
                String memberRole;
                if (Boolean.TRUE.equals(beanItem.getIsadmin())) {
                    memberRole = AppContext.getMessage(ProjectMemberI18nEnum.M_FORM_PROJECT_ADMIN);
                } else {
                    memberRole = beanItem.getRoleName();
                }
                return new DefaultViewField(memberRole);
            } else if (propertyId.equals("email")) {
                return new EmailViewField(beanItem.getEmail());
            }
            return null;
        }
    }

    @Override
    public HasPreviewFormHandlers<SimpleProjectMember> getPreviewFormHandlers() {
        return this.previewForm;
    }

}
