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

import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfileFormLayoutFactory.UserInformationLayout;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.view.component.RoleComboBox;
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
        withMargin(new MarginInfo(false, true, false, true));
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
    }

    private class BasicFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private GridFormLayoutHelper basicInformationLayout;

        @Override
        public ComponentContainer getLayout() {
            String title = (user.getUsername() == null) ? AppContext.getMessage(UserI18nEnum.VIEW_NEW_USER) :
                    user.getDisplayName();
            AddViewLayout formAddLayout = new AddViewLayout(title, FontAwesome.USER);

            Label organizationHeader = new Label(AppContext.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION));
            organizationHeader.setStyleName("h2");
            VerticalLayout layout = new VerticalLayout();
            layout.addComponent(organizationHeader);
            basicInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 2);
            layout.addComponent(basicInformationLayout.getLayout());

            formAddLayout.addHeaderRight(createButtonControls());
            formAddLayout.addBody(layout);
            formAddLayout.addBottomControls(createBottomPanel());
            return formAddLayout;
        }

        private Layout createButtonControls() {
            return new EditFormControlsGenerator<>(editUserForm).createButtonControls();
        }

        private Layout createBottomPanel() {
            MHorizontalLayout controlPanel = new MHorizontalLayout().withMargin(true).withStyleName("more-info").withHeight
                    ("40px").withWidth("100%");
            Button moreInfoBtn = new Button("More information...",
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(ClickEvent event) {
                            editUserForm.displayAdvancedForm(user);
                        }
                    });
            moreInfoBtn.addStyleName(UIConstants.THEME_LINK);
            controlPanel.with(moreInfoBtn).withAlign(moreInfoBtn, Alignment.MIDDLE_LEFT);
            return controlPanel;
        }

        @Override
        protected void onAttachField(Object propertyId, Field<?> field) {
            if (User.Field.email.equalTo(propertyId)) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 1, 0);
            } else if (SimpleUser.Field.roleid.equalTo(propertyId)) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_ROLE), 1, 1);
            } else if (User.Field.firstname.equalTo(propertyId)) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0, 0);
            } else if (User.Field.lastname.equalTo(propertyId)) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0, 1);
            }
        }

    }

    private class BasicEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleUser> {
        private static final long serialVersionUID = 1L;

        public BasicEditFormFieldFactory(GenericBeanForm<SimpleUser> form) {
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

    private class AdvancedFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private UserInformationLayout userInformationLayout;

        @Override
        public ComponentContainer getLayout() {
            String title = (user.getUsername() == null) ?
                    AppContext.getMessage(UserI18nEnum.VIEW_NEW_USER) : user.getDisplayName();
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
        protected void onAttachField(Object propertyId, Field<?> field) {
            userInformationLayout.attachField(propertyId, field);
        }
    }

    private class AdvancedEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleUser> {
        private static final long serialVersionUID = 1L;

        public AdvancedEditFormFieldFactory(GenericBeanForm<SimpleUser> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (SimpleUser.Field.roleid.equalTo(propertyId)) {
                return new AdminRoleSelectionField();
            } else if (User.Field.email.equalTo(propertyId) || User.Field.firstname.equalTo(propertyId)
                    || User.Field.lastname.equalTo(propertyId)) {
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
                    public void valueChange(
                            final Property.ValueChangeEvent event) {
                        user.setCountry((String) cboCountry.getValue());
                    }
                });
                return cboCountry;
            }
            return null;
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
                public void valueChange(
                        final Property.ValueChangeEvent event) {
                    getValue();
                }
            });
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
                user.setIsAccountOwner(Boolean.TRUE);
                user.setRoleName("Account Owner");
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