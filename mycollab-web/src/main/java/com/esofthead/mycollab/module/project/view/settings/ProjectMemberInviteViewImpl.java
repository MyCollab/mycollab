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
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent.InviteProjectMembers;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberInviteViewImpl extends AbstractPageView implements
        ProjectMemberInviteView {
    private static final long serialVersionUID = 1L;

    private List<String> inviteEmails;
    private Integer roleId = 0;
    private ProjectRoleComboBox roleComboBox;
    private TextArea messageArea;
    private GridFormLayoutHelper projectFormHelper;

    @Override
    public void display() {
        inviteEmails = new ArrayList<>();
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

        AddViewLayout userAddLayout = new AddViewLayout(
                AppContext
                        .getMessage(ProjectMemberI18nEnum.FORM_INVITE_MEMBERS),
                FontAwesome.USER);

        userAddLayout.addHeaderRight(createButtonControls());

        GridFormLayoutHelper informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

        InviteUserTokenField inviteUserTokenField = new InviteUserTokenField();
        informationLayout.addComponent(inviteUserTokenField, AppContext
                .getMessage(ProjectMemberI18nEnum.FORM_INVITEES_EMAIL), 0, 0);
        informationLayout.addComponent(roleComboBox,
                AppContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 1);

        messageArea = new TextArea();
        messageArea
                .setValue(AppContext
                        .getMessage(ProjectMemberI18nEnum.MSG_DEFAULT_INVITATION_COMMENT));
        informationLayout
                .addComponent(messageArea, AppContext
                        .getMessage(ProjectMemberI18nEnum.FORM_MESSAGE), 0, 2);

        userAddLayout.addBody(informationLayout.getLayout());
        userAddLayout.addBottomControls(createBottomPanel());
        this.addComponent(userAddLayout);
    }

    private Layout createButtonControls() {
        MHorizontalLayout controlButtons = new MHorizontalLayout();

        Button inviteBtn = new Button(
                AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        roleId = (Integer) roleComboBox.getValue();
                        ProjectMemberInviteViewImpl.this.fireEvent(new ViewEvent<>(
                                ProjectMemberInviteViewImpl.this,
                                new InviteProjectMembers(inviteEmails, roleId,
                                        messageArea.getValue())));

                    }
                });
        inviteBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        inviteBtn.setIcon(FontAwesome.SEND);
        controlButtons.addComponent(inviteBtn);

        Button cancelBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        ViewState viewState = HistoryViewManager.back();
                        if (viewState instanceof NullViewState) {
                            EventBusFactory.getInstance()
                                    .post(new ProjectMemberEvent.GotoList(this,
                                            null));
                        }

                    }
                });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
        controlButtons.addComponent(cancelBtn);

        controlButtons.setSizeUndefined();
        return controlButtons;
    }

    private Layout createBottomPanel() {
        VerticalLayout permissionsPanel = new VerticalLayout();
        final Label organizationHeader = new Label(
                AppContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS));
        organizationHeader.setStyleName("h2");
        permissionsPanel.addComponent(organizationHeader);

        projectFormHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2,
                ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length);
        permissionsPanel.addComponent(projectFormHelper.getLayout());

        roleId = (Integer) roleComboBox.getValue();
        displayRolePermission(roleId);

        return permissionsPanel;
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

    private class InviteUserTokenField extends CssLayout implements SuggestField.NewItemsHandler,
            SuggestField.SuggestionHandler, SuggestField.TokenHandler {
        private static final long serialVersionUID = 1L;

        private SuggestField suggestField;
        private List<SimpleUser> candidateUsers;

        public InviteUserTokenField() {
            super();
            this.setWidth("100%");
            this.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
            this.addStyleName("member-token");
            suggestField = new SuggestField();
            suggestField.setWidth("350px");
            suggestField.setHeight("32px");
            suggestField.setInputPrompt(AppContext
                    .getMessage(ProjectMemberI18nEnum.USER_TOKEN_INVITE_HINT));
            suggestField.setNewItemsAllowed(true);
            suggestField.setNewItemsHandler(this);
            suggestField.setImmediate(true);
            suggestField.setTokenMode(true);
            suggestField.setSuggestionHandler(this);
            suggestField.setSuggestionConverter(new UserSuggestionConverter());
            suggestField.setTokenHandler(this);
            suggestField.setMinimumQueryCharacters(1);
            suggestField.setPopupWidth(400);

            addComponent(suggestField);
            ProjectMemberService prjMemberService = ApplicationContextUtil
                    .getSpringBean(ProjectMemberService.class);
            candidateUsers = prjMemberService
                    .getUsersNotInProject(
                            CurrentProjectVariables.getProjectId(),
                            AppContext.getAccountId());
        }

        @Override
        public Object addNewItem(String value) {
            if (StringUtils.isValidEmail(value) && !inviteEmails.contains(value)) {
                inviteEmails.add(value);
                return value;
            }
            return null;
        }

        @Override
        public List<Object> searchItems(String query) {
            if ("".equals(query) || query == null) {
                return Collections.emptyList();
            }
            List<SimpleUser> result = new ArrayList<>();
            for (SimpleUser user : candidateUsers) {
                if (user.getEmail().contains(query) || user.getDisplayName().contains(query)) {
                    result.add(user);
                }
            }
            return new ArrayList<Object>(result);
        }

        @Override
        public void handleToken(Object token) {
            if (token != null) {
                if (token instanceof String) {
                    String address = (String) token;
                    addToken(generateToken(address));
                } else if (token instanceof SimpleUser) {
                    SimpleUser user = (SimpleUser)token;
                    if (!inviteEmails.contains(user.getEmail())) {
                        addToken(generateToken(user));
                        inviteEmails.add(user.getEmail());
                    }
                } else {
                    throw new MyCollabException("Do not support token type " + token);
                }
            }
        }

        private void addToken(Component button) {
            int index = getComponentIndex(suggestField);
            addComponent(button, index);
        }

        private Component generateToken(final String email) {
            final Button btn =new Button(email, FontAwesome.TIMES);
            btn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    InviteUserTokenField.this.removeComponent(btn);
                    inviteEmails.remove(email);
                }
            });
            btn.addStyleName("token-field");
            return btn;
        }

        private Component generateToken(final SimpleUser user) {
            final Button btn =new Button("", FontAwesome.TIMES);
            btn.setCaptionAsHtml(true);
            btn.setCaption((new Img("", StorageManager.getAvatarLink(user.getAvatarid(), 16))).write() + " " + user
                    .getDisplayName());
            btn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    InviteUserTokenField.this.removeComponent(btn);
                    inviteEmails.remove(user.getEmail());
                }
            });
            btn.setStyleName("token-field");
            return btn;
        }

        private class UserSuggestionConverter extends BeanSuggestionConverter {
            public UserSuggestionConverter() {
                super(SimpleUser.class, "email", "displayName", "displayName");
            }

            @Override
            public Object toItem(SuggestFieldSuggestion suggestion) {
                SimpleUser result = null;
                for (SimpleUser bean : candidateUsers) {
                    if (bean.getEmail().equals(suggestion.getId())) {
                        result = bean;
                        break;
                    }
                }
                assert result != null : "This should not be happening";
                return result;
            }
        }
    }
}
