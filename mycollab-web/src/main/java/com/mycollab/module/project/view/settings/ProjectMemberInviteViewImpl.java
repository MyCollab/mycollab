/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.mycollab.module.project.view.settings.component.InviteUserTokenField;
import com.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.ViewEvent;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.web.ui.AddViewLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.icons.VaadinIcons;
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

    private ProjectRoleComboBox roleComboBox;
    private TextArea messageArea;
    private InviteUserTokenField inviteUserTokenField;
    private GridFormLayoutHelper projectFormHelper;

    @Override
    public void display() {
        initContent();
    }

    private void initContent() {
        this.removeAllComponents();

        roleComboBox = new ProjectRoleComboBox();
        roleComboBox.addValueChangeListener(valueChangeEvent -> {
            SimpleProjectRole role = roleComboBox.getValue();
            displayRolePermission(role);
        });

        AddViewLayout userAddLayout = new AddViewLayout(UserUIContext.getMessage(ProjectMemberI18nEnum.FORM_INVITE_MEMBERS), VaadinIcons.USER);
        userAddLayout.addHeaderRight(createButtonControls());

        GridFormLayoutHelper informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);

        inviteUserTokenField = new InviteUserTokenField();
        informationLayout.addComponent(new MVerticalLayout(inviteUserTokenField, new ELabel(UserUIContext.getMessage
                        (ProjectMemberI18nEnum.USER_TOKEN_INVITE_HINT)).withFullWidth().withStyleName(WebThemes.META_INFO)).withMargin(false),
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
            SimpleProjectRole role = roleComboBox.getValue();
            ProjectMemberInviteViewImpl.this.fireEvent(new ViewEvent<>(ProjectMemberInviteViewImpl.this,
                    new InviteMembers(inviteUserTokenField.getInviteEmails(), role.getId(), role.getRolename(), messageArea.getValue())));
        }).withIcon(VaadinIcons.PAPERPLANE).withStyleName(WebThemes.BUTTON_ACTION);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                clickEvent -> EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, CurrentProjectVariables.getProjectId())))
                .withStyleName(WebThemes.BUTTON_OPTION);
        return new MHorizontalLayout(cancelBtn, inviteBtn);
    }

    private Layout createBottomPanel() {
        FormContainer permissionsPanel = new FormContainer();

        projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.TWO_COLUMN);
        permissionsPanel.addSection(UserUIContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS), projectFormHelper.getLayout());
        SimpleProjectRole role = roleComboBox.getValue();
        displayRolePermission(role);

        return permissionsPanel;
    }

    private void displayRolePermission(SimpleProjectRole role) {
        projectFormHelper.getLayout().removeAllComponents();
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
        } else {
            for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                projectFormHelper.addComponent(new Label(UserUIContext.getMessage(SecurityI18nEnum.ACCESS)),
                        permissionPath, i % 2, i / 2);
            }
        }
    }
}
