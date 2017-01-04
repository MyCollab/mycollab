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
package com.mycollab.mobile.module.project.view.settings;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.ProjectMemberEvent;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectMember;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.EmailViewField;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

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
    protected String initFormHeader() {
        return beanItem.getDisplayName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleProjectMember> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected void initRelatedComponents() {
    }

    @Override
    protected String getType() {
        return null;
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
        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoEdit(this, beanItem)))
                .withIcon(FontAwesome.EDIT).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));

        MButton deleteBtn = new MButton("", clickEvent -> ConfirmDialog.show(UI.getCurrent(),
                UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                dialog -> {
                    if (dialog.isConfirmed()) {
                        ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                        projectMemberService.removeWithSession(beanItem, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                        EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                    }
                })).withIcon(FontAwesome.TRASH).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.USERS));

        return new MHorizontalLayout(editBtn, deleteBtn);
    }

    private void displayRolePermission(Integer roleId) {
        permissionGroup.removeAllComponents();
        if (roleId != null && roleId > 0) {
            ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
            SimpleProjectRole role = roleService.findById(roleId, MyCollabUI.getAccountId());
            if (role != null) {
                final PermissionMap permissionMap = role.getPermissionMap();
                for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                    final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                    Label permissionLbl = new Label(UserUIContext.getPermissionCaptionValue(permissionMap, permissionPath));
                    permissionLbl.setCaption(UserUIContext.getMessage(RolePermissionI18nEnum.valueOf(permissionPath)));
                    permissionGroup.addComponent(permissionLbl);
                }
            }
        } else {
            for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                Label permissionLbl = new Label(UserUIContext.getMessage(SecurityI18nEnum.ACCESS));
                permissionLbl.setCaption(permissionPath);
                permissionGroup.addComponent(permissionLbl);
            }
        }
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return null;
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        MyCollabUI.addFragment("project/user/preview/" + UrlEncodeDecoder.encode(CurrentProjectVariables
                .getProjectId() + "/" + beanItem.getUsername()), beanItem.getDisplayName());
    }

    private class ProjectMemberFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 8920529536882351151L;

        private GridFormLayoutHelper informationLayout;

        @Override
        public AbstractComponent getLayout() {
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(false);

            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 2);
            layout.addComponent(informationLayout.getLayout());
            layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);

            layout.addComponent(FormSectionBuilder.build(UserUIContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS)));
            layout.addComponent(permissionGroup);
            return layout;
        }

        @Override
        public Component onAttachField(Object propertyId, Field<?> field) {
            if (propertyId.equals("email")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL), 0, 0);
            } else if (propertyId.equals("roleName")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 1);
            }
            return null;
        }
    }

    private class ProjectMemberBeanFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleProjectMember> {
        private static final long serialVersionUID = 5269043189285551214L;

        ProjectMemberBeanFormFieldFactory(GenericBeanForm<SimpleProjectMember> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("roleName")) {
                String memberRole;
                if (Boolean.TRUE.equals(beanItem.getIsadmin())) {
                    memberRole = UserUIContext.getMessage(ProjectMemberI18nEnum.M_FORM_PROJECT_ADMIN);
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
