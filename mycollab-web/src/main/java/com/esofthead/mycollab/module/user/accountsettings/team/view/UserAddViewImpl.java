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
package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfileFormLayoutFactory.UserInformationLayout;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.esofthead.mycollab.module.user.view.component.RoleComboBox;
import com.esofthead.mycollab.security.*;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserAddViewImpl extends AbstractPageView implements UserAddView {
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
            this.editUserForm.displayBasicForm(user);
        } else {
            this.editUserForm.displayAdvancedForm(user);
        }
    }

    private void displayRolePermission(Integer roleId) {
        editUserForm.displayRolePermission(roleId);
    }


    @Override
    public HasEditFormHandlers<SimpleUser> getEditFormHandlers() {
        return this.editUserForm;
    }

    private class EditUserForm extends AdvancedEditBeanForm<SimpleUser> {
        private static final long serialVersionUID = 1L;


        public void displayBasicForm(SimpleUser newDataSource) {
            this.setFormLayoutFactory(new BasicFormLayoutFactory());
            this.setBeanFormFieldFactory(new BasicEditFormFieldFactory(editUserForm));
            super.setBean(newDataSource);
        }

        public void displayAdvancedForm(SimpleUser newDataSource) {
            this.setFormLayoutFactory(new AdvancedFormLayoutFactory());
            this.setBeanFormFieldFactory(new AdvancedEditFormFieldFactory(editUserForm));
            super.setBean(newDataSource);
        }

        private void displayRolePermission(Integer roleId) {
            if (this.getLayoutFactory() instanceof BasicFormLayoutFactory) {
                ((BasicFormLayoutFactory) getLayoutFactory()).displayRolePermission(roleId);
            }
        }

        private class BasicFormLayoutFactory implements IWrappedFormLayoutFactory {
            private static final long serialVersionUID = 1L;

            private IFormLayoutFactory formLayoutFactory;
            private VerticalLayout rolePermissionLayout;

            @Override
            public ComponentContainer getLayout() {
                String title = (user.getUsername() == null) ? AppContext.getMessage(UserI18nEnum.VIEW_NEW_USER) : user.getDisplayName();
                AddViewLayout formAddLayout = new AddViewLayout(title, FontAwesome.USER);

                formLayoutFactory = buildFormLayout();
                formAddLayout.addHeaderRight(createButtonControls());
                formAddLayout.addBody(formLayoutFactory.getLayout());
                formAddLayout.addBottomControls(createBottomPanel());
                return formAddLayout;
            }

            private Layout createButtonControls() {
                return new EditFormControlsGenerator<>(editUserForm).createButtonControls();
            }

            private Layout createBottomPanel() {
                MVerticalLayout bottomPanel = new MVerticalLayout().withSpacing(false).withMargin(false);
                Button moreInfoBtn = new Button("More information...", new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        EditUserForm.this.setFormBuffered(false);
                        editUserForm.displayAdvancedForm(user);
                        EditUserForm.this.setFormBuffered(true);
                    }
                });
                moreInfoBtn.addStyleName(UIConstants.BUTTON_LINK);
                MHorizontalLayout linkWrap = new MHorizontalLayout().withMargin(true).with(moreInfoBtn);
                bottomPanel.with(linkWrap).withAlign(linkWrap, Alignment.MIDDLE_LEFT);

                rolePermissionLayout = new VerticalLayout();
                bottomPanel.addComponent(rolePermissionLayout);
                return bottomPanel;
            }

            private void displayRolePermission(Integer roleId) {
                rolePermissionLayout.removeAllComponents();
                PermissionMap permissionMap = null;
                if (roleId != null && roleId > 0) {
                    RoleService roleService = ApplicationContextUtil.getSpringBean(RoleService.class);
                    SimpleRole role = roleService.findById(roleId, AppContext.getAccountId());
                    if (role != null) {
                        permissionMap = role.getPermissionMap();
                    }
                } else {
                    permissionMap = PermissionMap.buildAdminPermissionCollection();
                }

                if (permissionMap != null) {
                    rolePermissionLayout.addComponent(constructPermissionSectionView("Project", permissionMap,
                            RolePermissionCollections.PROJECT_PERMISSION_ARR));

                    rolePermissionLayout.addComponent(constructPermissionSectionView("Customer Management", permissionMap,
                            RolePermissionCollections.CRM_PERMISSIONS_ARR));

                    rolePermissionLayout.addComponent(constructPermissionSectionView("Document", permissionMap,
                            RolePermissionCollections.DOCUMENT_PERMISSION_ARR));

                    rolePermissionLayout.addComponent(constructPermissionSectionView("Account Management", permissionMap,
                            RolePermissionCollections.ACCOUNT_PERMISSION_ARR));
                }
            }

            protected ComponentContainer constructPermissionSectionView(String depotTitle, PermissionMap permissionMap,
                                                                        List<PermissionDefItem> defItems) {
                GridFormLayoutHelper formHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, defItems.size());
                FormContainer permissionsPanel = new FormContainer();

                for (int i = 0; i < defItems.size(); i++) {
                    PermissionDefItem permissionDefItem = defItems.get(i);
                    formHelper.addComponent(new Label(getValueFromPerPath(permissionMap,
                            permissionDefItem.getKey())), permissionDefItem.getCaption(), 0, i);
                }
                permissionsPanel.addSection(depotTitle, formHelper.getLayout());
                return permissionsPanel;
            }

            private DynaFormLayout buildFormLayout() {
                DynaForm defaultForm = new DynaForm();
                DynaSection mainSection = new DynaSectionBuilder().header(AppContext.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION))
                        .layoutType(DynaSection.LayoutType.TWO_COLUMN).build();
                mainSection.addField(new TextDynaFieldBuilder().fieldName(User.Field.firstname).displayName(AppContext
                        .getMessage(UserI18nEnum.FORM_FIRST_NAME)).fieldIndex(0).build());
                mainSection.addField(new TextDynaFieldBuilder().fieldName(User.Field.email).displayName(AppContext
                        .getMessage(UserI18nEnum.FORM_EMAIL)).fieldIndex(1).build());
                mainSection.addField(new TextDynaFieldBuilder().fieldName(User.Field.lastname).displayName(AppContext
                        .getMessage(UserI18nEnum.FORM_LAST_NAME)).fieldIndex(2).build());
                mainSection.addField(new TextDynaFieldBuilder().fieldName(SimpleUser.Field.roleid).displayName(AppContext
                        .getMessage(UserI18nEnum.FORM_ROLE)).fieldIndex(3).build());
                defaultForm.addSection(mainSection);
                return new DynaFormLayout(defaultForm);
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
                    TextField tf = new TextField();
                    tf.setNullRepresentation("");
                    tf.setRequired(true);
                    tf.setRequiredError("This field must be not null");
                    return tf;
                }

                return null;
            }
        }

        private class AdvancedFormLayoutFactory implements IFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private UserInformationLayout userInformationLayout;

            @Override
            public ComponentContainer getLayout() {
                String title = (user.getUsername() == null) ? AppContext.getMessage(UserI18nEnum.VIEW_NEW_USER) : user.getDisplayName();
                AddViewLayout formAddLayout = new AddViewLayout(title, FontAwesome.USER);
                formAddLayout.addHeaderRight(createButtonControls());
                userInformationLayout = new UserInformationLayout();
                formAddLayout.addBody(userInformationLayout.getLayout());
                return formAddLayout;
            }

            private Layout createButtonControls() {
                return new EditFormControlsGenerator<>(editUserForm).createButtonControls();
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                userInformationLayout.attachField(propertyId, field);
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
                } else if (User.Field.email.equalTo(propertyId) || User.Field.firstname.equalTo(propertyId) || User.Field.lastname.equalTo(propertyId)) {
                    TextField tf = new TextField();
                    tf.setNullRepresentation("");
                    tf.setRequired(true);
                    tf.setRequiredError("This field must be not null");
                    return tf;
                } else if (propertyId.equals("dateofbirth")) {
                    return new DateComboboxSelectionField();
                } else if (propertyId.equals("timezone")) {
                    TimeZoneSelectionField cboTimezone = new TimeZoneSelectionField(false);
                    if (user.getTimezone() != null) {
                        cboTimezone.setTimeZone(TimezoneMapper.getTimezoneExt(user.getTimezone()));
                    } else {
                        if (AppContext.getUser().getTimezone() != null) {
                            cboTimezone.setTimeZone(TimezoneMapper.getTimezoneExt(AppContext.getUser().getTimezone()));
                        }
                    }
                    return cboTimezone;
                } else if (propertyId.equals("country")) {
                    final CountryComboBox cboCountry = new CountryComboBox();
                    cboCountry.addValueChangeListener(new Property.ValueChangeListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void valueChange(final Property.ValueChangeEvent event) {
                            user.setCountry((String) cboCountry.getValue());
                        }
                    });
                    return cboCountry;
                }
                return null;
            }
        }
    }

    private static String getValueFromPerPath(PermissionMap permissionMap, String permissionItem) {
        final Integer perVal = permissionMap.get(permissionItem);
        if (perVal == null) {
            return "Undefined";
        } else {
            if (PermissionChecker.isAccessPermission(perVal)) {
                return AppContext.getMessage(AccessPermissionFlag.toKey(perVal));
            } else if (PermissionChecker.isBooleanPermission(perVal)) {
                return AppContext.getMessage(BooleanPermissionFlag.toKey(perVal));
            } else {
                throw new MyCollabException("Do not support permission value " + perVal);
            }
        }
    }

    private class AdminRoleSelectionField extends CustomField<Integer> {
        private static final long serialVersionUID = 1L;

        private RoleComboBox roleBox;

        public AdminRoleSelectionField() {
            roleBox = new RoleComboBox();
            roleBox.addValueChangeListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(final Property.ValueChangeEvent event) {
                    Integer roleId = (Integer) roleBox.getValue();
                    displayRolePermission(roleId);
                }
            });
            Integer val = (Integer) roleBox.getValue();
            displayRolePermission(val);
        }

        @Override
        public void setPropertyDataSource(Property newDataSource) {
            Object value = newDataSource.getValue();
            if (value == null) {
                Object itemId = roleBox.getItemIds().iterator().next();
                roleBox.setValue(itemId);
            } else if (value instanceof Integer) {
                roleBox.setValue(value);
            }
            super.setPropertyDataSource(newDataSource);
        }

        @Override
        public void commit() throws SourceException, InvalidValueException {
            Integer roleId = (Integer) roleBox.getValue();
            if (roleId == -1) {
                if (!AppContext.isAdmin()) {
                    throw new UserInvalidInputException("Only the Account Owner can assign the role Account " +
                            "Owner to the user");
                } else {
                    user.setIsAccountOwner(Boolean.TRUE);
                    user.setRoleName("Account Owner");
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
}