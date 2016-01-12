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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent.InviteProjectMembers;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.view.settings.component.InviteUserTokenField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.web.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.FormContainer;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberInviteViewImpl extends AbstractPageView implements ProjectMemberInviteView {
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

        this.roleComboBox = new ProjectRoleComboBox();
        this.roleComboBox.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Integer roleId = (Integer) roleComboBox.getValue();
                displayRolePermission(roleId);
            }
        });

        AddViewLayout userAddLayout = new AddViewLayout(AppContext.getMessage(ProjectMemberI18nEnum.FORM_INVITE_MEMBERS),
                FontAwesome.USER);

        userAddLayout.addHeaderRight(createButtonControls());

        GridFormLayoutHelper informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

        inviteUserTokenField = new InviteUserTokenField();
        informationLayout.addComponent(new MVerticalLayout(inviteUserTokenField, new ELabel(AppContext.getMessage
                (ProjectMemberI18nEnum.USER_TOKEN_INVITE_HINT)).withStyleName(UIConstants.LABEL_META_INFO)).withMargin(false),
                AppContext.getMessage(ProjectMemberI18nEnum.FORM_INVITEES_EMAIL), 0, 0);
        informationLayout.addComponent(roleComboBox, AppContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 1);

        messageArea = new TextArea();
        messageArea.setValue(AppContext.getMessage(ProjectMemberI18nEnum.MSG_DEFAULT_INVITATION_COMMENT));
        informationLayout.addComponent(messageArea, AppContext.getMessage(ProjectMemberI18nEnum.FORM_MESSAGE), 0, 2);

        userAddLayout.addBody(informationLayout.getLayout());
        userAddLayout.addBottomControls(createBottomPanel());
        this.addComponent(userAddLayout);
    }

    private Layout createButtonControls() {
        MHorizontalLayout controlButtons = new MHorizontalLayout();

        Button inviteBtn = new Button(AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                roleId = (Integer) roleComboBox.getValue();
                BeanItem<SimpleProjectRole> item = (BeanItem<SimpleProjectRole>) roleComboBox.getItem(roleId);
                String roleName = (item != null) ? item.getBean().getRolename() : "";
                ProjectMemberInviteViewImpl.this.fireEvent(new ViewEvent<>(ProjectMemberInviteViewImpl.this,
                        new InviteProjectMembers(inviteUserTokenField.getInviteEmails(), roleId, roleName, messageArea.getValue())));

            }
        });
        inviteBtn.setStyleName(UIConstants.BUTTON_ACTION);
        inviteBtn.setIcon(FontAwesome.SEND);

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                ViewState viewState = HistoryViewManager.back();
                if (viewState instanceof NullViewState) {
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoList(this, null));
                }
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
        controlButtons.with(inviteBtn, cancelBtn);

        controlButtons.setSizeUndefined();
        return controlButtons;
    }

    private Layout createBottomPanel() {
        FormContainer permissionsPanel = new FormContainer();

        projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length);
        permissionsPanel.addSection(AppContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS), projectFormHelper.getLayout());
        roleId = (Integer) roleComboBox.getValue();
        displayRolePermission(roleId);

        return permissionsPanel;
    }

    private void displayRolePermission(Integer roleId) {
        projectFormHelper.getLayout().removeAllComponents();
        if (roleId != null && roleId > 0) {
            ProjectRoleService roleService = ApplicationContextUtil.getSpringBean(ProjectRoleService.class);
            SimpleProjectRole role = roleService.findById(roleId, AppContext.getAccountId());
            if (role != null) {
                PermissionMap permissionMap = role.getPermissionMap();
                for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                    String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                    projectFormHelper.addComponent(new Label(AppContext.getPermissionCaptionValue(
                            permissionMap, permissionPath)), AppContext
                            .getMessage(RolePermissionI18nEnum.valueOf(permissionPath)), 0, i);
                }
            }
        } else {
            for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
                final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
                projectFormHelper.addComponent(new Label(AppContext
                        .getMessage(SecurityI18nEnum.ACCESS)), permissionPath, 0, i);
            }
        }
    }
}
