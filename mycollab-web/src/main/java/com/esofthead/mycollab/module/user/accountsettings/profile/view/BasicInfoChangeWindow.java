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
package com.esofthead.mycollab.module.user.accountsettings.profile.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.module.user.ui.components.LanguageComboBox;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DateSelectionField;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.TimeZoneSelectionField;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.google.common.base.MoreObjects;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BasicInfoChangeWindow extends Window {
    private TextField txtFirstName = new TextField();
    private TextField txtLastName = new TextField();
    private TextField txtEmail = new TextField();
    private DateSelectionField birthdayField = new DateSelectionField();
    private TimeZoneSelectionField timeZoneField = new TimeZoneSelectionField(false);
    private LanguageComboBox languageBox = new LanguageComboBox();

    private final User user;

    public BasicInfoChangeWindow(final User user) {
        this.user = user;
        this.setWidth("600px");
        this.setResizable(false);
        this.setModal(true);
        this.initUI();
        this.center();
        this.setCaption(AppContext.getMessage(UserI18nEnum.WINDOW_CHANGE_BASIC_INFO_TITLE));
    }

    private void initUI() {
        final MVerticalLayout mainLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false)).withFullWidth();

        final GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(1, 6);

        passInfo.addComponent(txtFirstName, AppContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0, 0);
        passInfo.addComponent(txtLastName, AppContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0, 1);
        txtLastName.setRequired(true);
        passInfo.addComponent(txtEmail, AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 0, 2);
        txtEmail.setRequired(true);
        passInfo.addComponent(birthdayField, AppContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 0, 3);
        birthdayField.setDate(user.getDateofbirth());

        passInfo.addComponent(timeZoneField, AppContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 4);
        timeZoneField.setValue(user.getTimezone());

        passInfo.addComponent(languageBox, AppContext.getMessage(UserI18nEnum.FORM_LANGUAGE), 0, 5);
        languageBox.setValue(user.getLanguage());

        txtFirstName.setValue(MoreObjects.firstNonNull(user.getFirstname(), ""));
        txtLastName.setValue(MoreObjects.firstNonNull(user.getLastname(), ""));
        txtEmail.setValue(MoreObjects.firstNonNull(user.getEmail(), ""));
        birthdayField.setValue(user.getDateofbirth());
        mainLayout.addComponent(passInfo.getLayout());
        mainLayout.setComponentAlignment(passInfo.getLayout(), Alignment.TOP_LEFT);

        MHorizontalLayout hlayoutControls = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true));
        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                close();
            }
        });
        cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);

        Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                changeUserInfo();
            }
        });
        saveBtn.setStyleName(UIConstants.BUTTON_ACTION);
        saveBtn.setIcon(FontAwesome.SAVE);

        hlayoutControls.with(cancelBtn, saveBtn).alignAll(Alignment.MIDDLE_CENTER);
        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

        this.setModal(true);
        this.setContent(mainLayout);
    }

    private void changeUserInfo() {
        txtLastName.removeStyleName("errorField");
        txtEmail.removeStyleName("errorField");

        if (txtLastName.getValue().equals("")) {
            NotificationUtil.showErrorNotification("The last name must be not null!");
            txtLastName.addStyleName("errorField");
            return;
        }

        if (txtEmail.getValue().equals("")) {
            NotificationUtil.showErrorNotification("The email must be not null!");
            txtLastName.addStyleName("errorField");
            return;
        }

        user.setFirstname(txtFirstName.getValue());
        user.setLastname(txtLastName.getValue());
        user.setEmail(txtEmail.getValue());
        user.setDateofbirth(birthdayField.getDate());
        user.setLanguage(languageBox.getValue());
        user.setTimezone(timeZoneField.getValue());

        final UserService userService = AppContextUtil.getSpringBean(UserService.class);
        userService.updateWithSession(user, AppContext.getUsername());
        close();
        Page.getCurrent().getJavaScript().execute("window.location.reload();");
    }
}
