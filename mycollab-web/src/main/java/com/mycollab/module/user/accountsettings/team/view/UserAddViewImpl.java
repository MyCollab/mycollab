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
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.SecurityI18nEnum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.form.view.builder.DynaSectionBuilder;
import com.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.RoleService;
import com.mycollab.module.user.view.component.RoleComboBox;
import com.mycollab.security.PermissionDefItem;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserAddViewImpl extends AbstractVerticalPageView implements UserAddView {
    private static final long serialVersionUID = 1L;

    private EditUserForm editUserForm;
    private SimpleUser user;

    public UserAddViewImpl() {
        super();
        withMargin(new MarginInfo(false, true, true, true));
        editUserForm = new EditUserForm();
    }

    @Override
    public void editItem(SimpleUser item) {
        editItem(item, true);
    }

    @Override
    public void editItem(SimpleUser item, boolean isBasicForm) {
        this.user = item;
        this.removeAllComponents();
        this.addComponent(editUserForm);
        if (isBasicForm) {
            editUserForm.displayBasicForm(user);
        } else {
            editUserForm.displayAdvancedForm(user);
        }
    }

    private void displayRolePermission(Integer roleId) {
        editUserForm.displayRolePermission(roleId);
    }


    @Override
    public HasEditFormHandlers<SimpleUser> getEditFormHandlers() {
        return editUserForm;
    }

    private class EditUserForm extends AdvancedEditBeanForm<SimpleUser> {
        private static final long serialVersionUID = 1L;


        private void displayBasicForm(SimpleUser newDataSource) {
            this.setFormLayoutFactory(new BasicFormLayoutFactory());
            this.setBeanFormFieldFactory(new BasicEditFormFieldFactory(editUserForm));
            super.setBean(newDataSource);
        }

        private void displayAdvancedForm(SimpleUser newDataSource) {
            this.setFormLayoutFactory(new AdvancedFormLayoutFactory());
            this.setBeanFormFieldFactory(new AdvancedEditFormFieldFactory(editUserForm));
            super.setBean(newDataSource);
        }

        private void displayRolePermission(Integer roleId) {
            if (this.getLayoutFactory() instanceof BasicFormLayoutFactory) {
                ((BasicFormLayoutFactory) getLayoutFactory()).displayRolePermission(roleId);
            } else if (this.getLayoutFactory() instanceof AdvancedFormLayoutFactory) {
                ((AdvancedFormLayoutFactory) getLayoutFactory()).displayRolePermission(roleId);
            }
        }

        private class BasicFormLayoutFactory extends WrappedFormLayoutFactory {
            private static final long serialVersionUID = 1L;

            private RolePermissionContainer rolePermissionLayout;

            @Override
            public AbstractComponent getLayout() {
                String title = (user.getUsername() == null) ? UserUIContext.getMessage(UserI18nEnum.NEW) : user.getDisplayName();
                AddViewLayout formAddLayout = new AddViewLayout(title, FontAwesome.USER);

                wrappedLayoutFactory = buildFormLayout();
                formAddLayout.addHeaderRight(generateEditFormControls(editUserForm, true, false, true));
                formAddLayout.addBody(wrappedLayoutFactory.getLayout());
                formAddLayout.addBottom(createBottomPanel());
                return formAddLayout;
            }

            private Layout createBottomPanel() {
                MVerticalLayout bottomPanel = new MVerticalLayout().withMargin(false);
                Button moreInfoBtn = new MButton(UserUIContext.getMessage(UserI18nEnum.ACTION_MORE_INFORMATION), event -> {
                    editUserForm.displayAdvancedForm(user);
                    setFormBuffered(true);
                }).withStyleName(WebThemes.BUTTON_LINK);
                MHorizontalLayout linkWrap = new MHorizontalLayout().withMargin(true).with(moreInfoBtn);
                bottomPanel.with(linkWrap).withAlign(linkWrap, Alignment.MIDDLE_LEFT);
                rolePermissionLayout = new RolePermissionContainer();
                bottomPanel.addComponent(rolePermissionLayout);
                return bottomPanel;
            }

            void displayRolePermission(Integer roleId) {
                rolePermissionLayout.displayRolePermission(roleId);
            }

            private DefaultDynaFormLayout buildFormLayout() {
                DynaForm defaultForm = new DynaForm();
                DynaSection mainSection = new DynaSectionBuilder().header(UserI18nEnum.SECTION_BASIC_INFORMATION)
                        .layoutType(DynaSection.LayoutType.TWO_COLUMN).build();
                mainSection.fields(new TextDynaFieldBuilder().fieldName(User.Field.firstname).displayName(UserI18nEnum.FORM_FIRST_NAME)
                        .fieldIndex(0).build());
                mainSection.fields(new TextDynaFieldBuilder().fieldName(User.Field.email).displayName(GenericI18Enum.FORM_EMAIL)
                        .fieldIndex(1).build());
                mainSection.fields(new TextDynaFieldBuilder().fieldName(User.Field.lastname).displayName(UserI18nEnum.FORM_LAST_NAME)
                        .fieldIndex(2).build());
                mainSection.fields(new TextDynaFieldBuilder().fieldName(SimpleUser.Field.roleid).displayName(UserI18nEnum.FORM_ROLE)
                        .fieldIndex(3).build());
                if (user.getUsername() == null) {
                    mainSection.fields(new TextDynaFieldBuilder().fieldName(User.Field.password).displayName
                            (ShellI18nEnum.FORM_PASSWORD).contextHelp(ShellI18nEnum.FORM_PASSWORD_HELP).fieldIndex(4).build());
                }
                defaultForm.sections(mainSection);
                return new DefaultDynaFormLayout(defaultForm);
            }
        }

        private class BasicEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleUser> {
            private static final long serialVersionUID = 1L;

            BasicEditFormFieldFactory(GenericBeanForm<SimpleUser> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if (SimpleUser.Field.roleid.equalTo(propertyId)) {
                    return new AdminRoleSelectionField();
                } else if (User.Field.email.equalTo(propertyId) || User.Field.firstname.equalTo(propertyId) ||
                        User.Field.lastname.equalTo(propertyId)) {
                    return new MTextField().withNullRepresentation("").withRequired(true)
                            .withRequiredError("This field must be not null");
                } else if (User.Field.password.equalTo(propertyId)) {
                    return new MPasswordField();
                }

                return null;
            }
        }

        private class AdvancedFormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper basicInformationLayout;
            private GridFormLayoutHelper advancedInformationLayout;
            private GridFormLayoutHelper contactInformationLayout;
            private RolePermissionContainer rolePermissionLayout;

            @Override
            public AbstractComponent getLayout() {
                String title = (user.getUsername() == null) ? UserUIContext.getMessage(UserI18nEnum.NEW) : user.getDisplayName();
                AddViewLayout formAddLayout = new AddViewLayout(title, FontAwesome.USER);
                formAddLayout.addHeaderRight(generateEditFormControls(editUserForm, true, false, true));
                FormContainer formContainer = new FormContainer();
                basicInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 7);
                formContainer.addSection(UserUIContext.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION),
                        basicInformationLayout.getLayout());

                contactInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
                formContainer.addSection(UserUIContext.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION),
                        contactInformationLayout.getLayout());

                advancedInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 2);
                formContainer.addSection(UserUIContext.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION),
                        advancedInformationLayout.getLayout());
                formAddLayout.addBody(formContainer);
                rolePermissionLayout = new RolePermissionContainer();
                formAddLayout.addBottom(rolePermissionLayout);
                return formAddLayout;
            }

            void displayRolePermission(Integer roleId) {
                rolePermissionLayout.displayRolePermission(roleId);
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("firstname")) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0, 0);
                } else if (propertyId.equals("lastname")) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0, 1);
                } else if (propertyId.equals("nickname")) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_NICK_NAME), 1, 0);
                } else if (propertyId.equals("dateofbirth")) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 1, 1);
                } else if (propertyId.equals("email")) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL), 0, 2);
                } else if (propertyId.equals("roleid")) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_ROLE), 1, 2);
                } else if (propertyId.equals("timezone")) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 3);
                } else if (User.Field.password.equalTo(propertyId) && (user.getUsername() == null)) {
                    return basicInformationLayout.addComponent(field, UserUIContext.getMessage(ShellI18nEnum.FORM_PASSWORD),
                            UserUIContext.getMessage(ShellI18nEnum.FORM_PASSWORD_HELP), 1, 3);
                } else if (propertyId.equals("company")) {
                    return advancedInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_COMPANY), 0, 0);
                } else if (propertyId.equals("country")) {
                    return advancedInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_COUNTRY),
                            0, 1, 2, "100%");
                } else if (propertyId.equals("website")) {
                    return advancedInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_WEBSITE), 1, 0);
                } else if (propertyId.equals("workphone")) {
                    return contactInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0, 0);
                } else if (propertyId.equals("homephone")) {
                    return contactInformationLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0, 1);
                } else if (propertyId.equals("facebookaccount")) {
                    return contactInformationLayout.addComponent(field, "Facebook", 1, 0);
                } else if (propertyId.equals("twitteraccount")) {
                    return contactInformationLayout.addComponent(field, "Twitter", 1, 1);
                } else if (propertyId.equals("skypecontact")) {
                    return contactInformationLayout.addComponent(field, "Skype", 0, 2, 2, "262px");
                }
                return null;
            }
        }

        private class AdvancedEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleUser> {
            private static final long serialVersionUID = 1L;

            AdvancedEditFormFieldFactory(GenericBeanForm<SimpleUser> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if (SimpleUser.Field.roleid.equalTo(propertyId)) {
                    return new AdminRoleSelectionField();
                } else if (User.Field.email.equalTo(propertyId) || User.Field.firstname.equalTo(propertyId) ||
                        User.Field.lastname.equalTo(propertyId)) {
                    return new MTextField().withNullRepresentation("").withRequired(true)
                            .withRequiredError("This field must be not null");
                } else if (propertyId.equals("dateofbirth")) {
                    return new DateSelectionField();
                } else if (propertyId.equals("timezone")) {
                    return new TimeZoneSelectionField(false);
                } else if (propertyId.equals("country")) {
                    final CountryComboBox cboCountry = new CountryComboBox();
                    cboCountry.addValueChangeListener(valueChangeEvent -> user.setCountry((String) cboCountry.getValue()));
                    return cboCountry;
                } else if (User.Field.password.equalTo(propertyId)) {
                    return new MPasswordField();
                }
                return null;
            }
        }
    }

    private class AdminRoleSelectionField extends CustomField<Integer> {
        private static final long serialVersionUID = 1L;

        private RoleComboBox roleBox;

        AdminRoleSelectionField() {
            roleBox = new RoleComboBox();
            roleBox.addValueChangeListener(valueChangeEvent -> {
                Integer roleId = (Integer) roleBox.getValue();
                displayRolePermission(roleId);
            });
            Integer val = (Integer) roleBox.getValue();
            displayRolePermission(val);
        }

        @Override
        public void setPropertyDataSource(Property newDataSource) {
            Object value = newDataSource.getValue();
            if (value instanceof Integer) {
                roleBox.setValue(value);
            } else if (value == null && Boolean.TRUE.equals(user.getIsAccountOwner())) {
                roleBox.setValue(-1);
            }
            super.setPropertyDataSource(newDataSource);
        }

        @Override
        public void commit() throws SourceException, InvalidValueException {
            Integer roleId = (Integer) roleBox.getValue();
            if (roleId == -1) {
                if (!UserUIContext.isAdmin()) {
                    throw new UserInvalidInputException(UserUIContext.getMessage(RoleI18nEnum.ERROR_ONLY_OWNER_CAN_ASSIGN_OWNER_ROLE));
                } else {
                    user.setIsAccountOwner(Boolean.TRUE);
                    user.setRoleName(UserUIContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER));
                }
            } else {
                user.setIsAccountOwner(Boolean.FALSE);
                BeanItem<SimpleRole> role = (BeanItem<SimpleRole>) roleBox.getItem(roleId);
                if (role != null) {
                    user.setRoleName(role.getBean().getRolename());
                }
            }
            setInternalValue(roleId);
            super.commit();
        }

        @Override
        public Class<Integer> getType() {
            return Integer.class;
        }

        @Override
        protected Component initContent() {
            return roleBox;
        }
    }

    private static class RolePermissionContainer extends VerticalLayout {
        private VerticalLayout permissionLayout;

        RolePermissionContainer() {
            this.setMargin(new MarginInfo(true, false, false, false));
            this.addComponent(ELabel.h2(UserUIContext.getMessage(RolePermissionI18nEnum.LIST)));
            permissionLayout = new VerticalLayout();
            this.addComponent(permissionLayout);
        }

        private void displayRolePermission(Integer roleId) {
            permissionLayout.removeAllComponents();
            PermissionMap permissionMap = null;
            if (roleId != null && roleId > 0) {
                RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
                SimpleRole role = roleService.findById(roleId, MyCollabUI.getAccountId());
                if (role != null) {
                    permissionMap = role.getPermissionMap();
                }
            } else {
                permissionMap = PermissionMap.buildAdminPermissionCollection();
            }

            if (permissionMap != null) {
                permissionLayout.addComponent(constructPermissionSectionView(UserUIContext.getMessage(RoleI18nEnum.SECTION_PROJECT_MANAGEMENT_TITLE),
                        permissionMap, RolePermissionCollections.PROJECT_PERMISSION_ARR));

                permissionLayout.addComponent(constructPermissionSectionView(UserUIContext.getMessage(RoleI18nEnum.SECTION_CRM_TITLE),
                        permissionMap, RolePermissionCollections.CRM_PERMISSIONS_ARR));

                permissionLayout.addComponent(constructPermissionSectionView(UserUIContext.getMessage(RoleI18nEnum.SECTION_DOCUMENT_TITLE),
                        permissionMap, RolePermissionCollections.DOCUMENT_PERMISSION_ARR));

                permissionLayout.addComponent(constructPermissionSectionView(UserUIContext.getMessage(RoleI18nEnum.SECTION_ACCOUNT_MANAGEMENT_TITLE),
                        permissionMap, RolePermissionCollections.ACCOUNT_PERMISSION_ARR));
            }
        }

        private ComponentContainer constructPermissionSectionView(String depotTitle, PermissionMap permissionMap,
                                                                  List<PermissionDefItem> defItems) {
            GridFormLayoutHelper formHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, defItems.size() / 2 + 1);
            FormContainer permissionsPanel = new FormContainer();

            for (int i = 0; i < defItems.size(); i++) {
                PermissionDefItem permissionDefItem = defItems.get(i);
                Integer flag = permissionMap.getPermissionFlag(permissionDefItem.getKey());
                SecurityI18nEnum permissionVal = PermissionFlag.toVal(flag);
                formHelper.addComponent(new Label(UserUIContext.getMessage(permissionVal)), UserUIContext.getMessage(permissionDefItem.getCaption()),
                        UserUIContext.getMessage(permissionVal.desc()), i % 2, i / 2);
            }
            permissionsPanel.addSection(depotTitle, formHelper.getLayout());
            return permissionsPanel;
        }
    }
}