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
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.view.component.RoleComboBox;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

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
        this.setMargin(new MarginInfo(false, true, false, true));
        this.editUserForm = new EditUserForm();
        this.addComponent(this.editUserForm);
    }

    @Override
    public void editItem(final SimpleUser item) {
        editItem(item, true);
    }

    @Override
    public void editItem(SimpleUser item, boolean isBasicForm) {
        this.user = item;
        this.removeAllComponents();
        this.addComponent(this.editUserForm);
        if (isBasicForm) {
            this.editUserForm.displayBasicForm(this.user);
        } else {
            this.editUserForm.displayAdvancedForm(this.user);
        }
    }


    @Override
    public HasEditFormHandlers<SimpleUser> getEditFormHandlers() {
        return this.editUserForm;
    }

    private class EditUserForm extends AdvancedEditBeanForm<SimpleUser> {
        private static final long serialVersionUID = 1L;

        public void displayBasicForm(final SimpleUser newDataSource) {
            this.setFormLayoutFactory(new BasicFormLayoutFactory());
            this.setBeanFormFieldFactory(new BasicEditFormFieldFactory(
                    editUserForm));
            super.setBean(newDataSource);
        }

        public void displayAdvancedForm(final SimpleUser newDataSource) {
            this.setFormLayoutFactory(new AdvancedFormLayoutFactory());
            this.setBeanFormFieldFactory(new AdvancedEditFormFieldFactory(
                    editUserForm));
            super.setBean(newDataSource);
        }
    }

    private class BasicFormLayoutFactory implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private GridFormLayoutHelper basicInformationLayout;

        @Override
        public ComponentContainer getLayout() {
            String title = (user.getUsername() == null) ? AppContext
                    .getMessage(UserI18nEnum.VIEW_NEW_USER) : user
                    .getDisplayName();
            final AddViewLayout formAddLayout = new AddViewLayout(title, FontAwesome.USER);

            final VerticalLayout layout = new VerticalLayout();
            final Label organizationHeader = new Label(
                    AppContext
                            .getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION));
            organizationHeader.setStyleName("h2");
            layout.addComponent(organizationHeader);

            this.basicInformationLayout = new GridFormLayoutHelper(2, 1,
                    "100%", "167px", Alignment.TOP_LEFT);
            this.basicInformationLayout.getLayout().setWidth("100%");
            this.basicInformationLayout.getLayout().setMargin(false);
            this.basicInformationLayout.getLayout().addStyleName(
                    UIConstants.COLORED_GRIDLAYOUT);

            layout.addComponent(this.basicInformationLayout.getLayout());

            formAddLayout.addHeaderRight(createButtonControls());
            formAddLayout.addBody(layout);
            formAddLayout.addBottomControls(createBottomPanel());
            return formAddLayout;
        }

        private Layout createButtonControls() {
            final HorizontalLayout controlPanel = new HorizontalLayout();
            final Layout controlButtons = (new EditFormControlsGenerator<>(
                    editUserForm)).createButtonControls();
            controlButtons.setSizeUndefined();
            controlPanel.addComponent(controlButtons);
            controlPanel.setComponentAlignment(controlButtons,
                    Alignment.MIDDLE_CENTER);
            return controlPanel;
        }

        private Layout createBottomPanel() {
            final HorizontalLayout controlPanel = new HorizontalLayout();
            controlPanel.setMargin(true);
            controlPanel.setStyleName("more-info");
            controlPanel.setHeight("40px");
            controlPanel.setWidth("100%");
            Button moreInfoBtn = new Button("More information...",
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(ClickEvent event) {
                            editUserForm.displayAdvancedForm(user);
                        }
                    });
            moreInfoBtn.addStyleName(UIConstants.THEME_LINK);
            controlPanel.addComponent(moreInfoBtn);
            controlPanel.setComponentAlignment(moreInfoBtn,
                    Alignment.MIDDLE_LEFT);
            return controlPanel;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {
            if (propertyId.equals("email")) {
                basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 0, 0);
            } else if (propertyId.equals("roleid")) {
                basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_ROLE), 1, 0);
            }

        }

    }

    private class BasicEditFormFieldFactory extends
            AbstractBeanFieldGroupEditFieldFactory<SimpleUser> {
        private static final long serialVersionUID = 1L;

        public BasicEditFormFieldFactory(GenericBeanForm<SimpleUser> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("roleid")) {
                return new AdminRoleSelectionField();
            } else if (propertyId.equals("email")) {
                final TextField tf = new TextField();
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
            String title = (user.getUsername() == null) ? AppContext
                    .getMessage(UserI18nEnum.VIEW_NEW_USER) : user
                    .getDisplayName();
            final AddViewLayout formAddLayout = new AddViewLayout(title, FontAwesome.USER);
            formAddLayout.addHeaderRight(createButtonControls());
            userInformationLayout = new UserInformationLayout();
            formAddLayout.addBody(userInformationLayout.getLayout());
            return formAddLayout;
        }

        private Layout createButtonControls() {
            final HorizontalLayout controlPanel = new HorizontalLayout();
            final Layout controlButtons = (new EditFormControlsGenerator<>(
                    editUserForm)).createButtonControls();
            controlButtons.setSizeUndefined();
            controlPanel.addComponent(controlButtons);
            controlPanel.setComponentAlignment(controlButtons,
                    Alignment.MIDDLE_CENTER);
            return controlPanel;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {
            userInformationLayout.attachField(propertyId, field);

        }
    }

    private class AdvancedEditFormFieldFactory extends
            AbstractBeanFieldGroupEditFieldFactory<SimpleUser> {
        private static final long serialVersionUID = 1L;

        public AdvancedEditFormFieldFactory(GenericBeanForm<SimpleUser> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("roleid")) {
                return new AdminRoleSelectionField();
            } else if (propertyId.equals("firstname")
                    || propertyId.equals("lastname")
                    || propertyId.equals("email")) {
                final TextField tf = new TextField();
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("This field must be not null");
                return tf;
            } else if (propertyId.equals("dateofbirth")) {
                return new DateComboboxSelectionField();
            } else if (propertyId.equals("timezone")) {
                TimeZoneSelectionField cboTimezone = new TimeZoneSelectionField(false);
                if (UserAddViewImpl.this.user.getTimezone() != null) {
                    cboTimezone.setTimeZone(TimezoneMapper
                            .getTimezoneExt(UserAddViewImpl.this.user
                                    .getTimezone()));
                } else {
                    if (AppContext.getSession().getTimezone() != null) {
                        cboTimezone
                                .setTimeZone(TimezoneMapper
                                        .getTimezoneExt(AppContext.getSession()
                                                .getTimezone()));
                    }
                }
                return cboTimezone;
            } else if (propertyId.equals("country")) {
                final CountryComboBox cboCountry = new CountryComboBox();
                cboCountry
                        .addValueChangeListener(new Property.ValueChangeListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void valueChange(
                                    final Property.ValueChangeEvent event) {
                                UserAddViewImpl.this.user
                                        .setCountry((String) cboCountry
                                                .getValue());
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
            this.roleBox.addValueChangeListener(new Property.ValueChangeListener() {
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
            } else {
                user.setIsAccountOwner(Boolean.FALSE);
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