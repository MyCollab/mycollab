/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.event.ProjectMemberEvent.InviteProjectMembers;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.view.settings.component.InviteUserTokenField;
import com.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.event.ViewEvent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.AddViewLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberInviteViewImpl extends AbstractVerticalPageView implements ProjectMemberInviteView {
    private static final long serialVersionUID = 1L;

    private Integer roleId = 0;
    private ProjectRoleComboBox roleComboBox;
    private TextArea messageArea;
    private InviteUserTokenField inviteUserTokenField;
    private GridFormLayoutHelper projectFormHelper;

    @Override
    public void display() {
        roleId = 0;
        initContent();
    }

    private void initContent() {
        this.removeAllComponents();

        roleComboBox = new ProjectRoleComboBox();
        roleComboBox.addValueChangeListener(valueChangeEvent -> {
            Integer roleId = (Integer) roleComboBox.getValue();
            displayRolePermission(roleId);
        });

        AddViewLayout userAddLayout = new AddViewLayout(UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_INVITE_MEMBERS), FontAwesome.USER);
        userAddLayout.addHeaderRight(createButtonControls());

        GridFormLayoutHelper informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

        inviteUserTokenField = new InviteUserTokenField();
        informationLayout.addComponent(new MVerticalLayout(inviteUserTokenField, new ELabel(UserUIContext.getMessage
                        (ProjectMemberI18nEnum.USER_TOKEN_INVITE_HINT)).withStyleName(UIConstants.META_INFO)).withMargin(false),
                UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_INVITEES_EMAIL), 0, 0);
        informationLayout.addComponent(roleComboBox, UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 1);

        messageArea = new TextArea();
        messageArea.setValue(UserUIContext.getMessage(ProjectMemberI18nEnum.MSG_DEFAULT_INVITATION_COMMENT));
        informationLayout.addComponent(messageArea, UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_MESSAGE), 0, 2);

        userAddLayout.addBody(informationLayout.getLayout());
        userAddLayout.addBottom(createBottomPanel());
        this.addComponent(userAddLayout);
    }

    private Layout createButtonControls() {
        MButton inviteBtn = new MButton(UserUIContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEE), clickEvent -> {
            roleId = (Integer) roleComboBox.getValue();
            BeanItem<SimpleProjectRole> item = (BeanItem<SimpleProjectRole>) roleComboBox.getItem(roleId);
            String roleName = (item != null) ? item.getBean().getRolename() : "";
            ProjectMemberInviteViewImpl.this.fireEvent(new ViewEvent<>(this,
                    new InviteProjectMembers(inviteUserTokenField.getInviteEmails(), roleId, roleName, messageArea.getValue())));
        }).withIcon(FontAwesome.SEND).withStyleName(WebThemes.BUTTON_ACTION);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                clickEvent -> EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null)))
                .withStyleName(WebThemes.BUTTON_OPTION);
        return new MHorizontalLayout(cancelBtn, inviteBtn);
    }

    private Layout createBottomPanel() {
        FormContainer permissionsPanel = new FormContainer();

        projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length / 2 + 1, "180px");
        permissionsPanel.addSection(UserUIContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS), projectFormHelper.getLayout());
        roleId = (Integer) roleComboBox.getValue();
        displayRolePermission(roleId);

        return permissionsPanel;
    }

    private void displayRolePermission(Integer roleId) {
        projectFormHelper.getLayout().removeAllComponents();
        if (roleId != null && roleId > 0) {
            ProjectRoleService roleService = AppContextUtil.getSpringBean(ProjectRoleService.class);
            SimpleProjectRole role = roleService.findById(roleId, AppUI.getAccountId());
            if (role != null) {
                PermissionMap permissionMap = role.getPermissionMap();
                for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                    String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
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
                projectFormHelper.addComponent(new Label(UserUIContext.getMessage(SecurityI18nEnum.ACCESS)),
                        permissionPath, i % 2, i / 2);
            }
        }
    }
}
