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
package com.mycollab.module.user.accountsettings.profile.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.module.user.ui.components.LanguageSelectionField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.DateSelectionField;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.TimeZoneSelectionField;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.google.common.base.MoreObjects;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BasicInfoChangeWindow extends MWindow {
    private TextField txtFirstName = new TextField();
    private TextField txtLastName = new TextField();
    private TextField txtEmail = new TextField();
    private DateSelectionField birthdayField = new DateSelectionField();
    private TimeZoneSelectionField timeZoneField = new TimeZoneSelectionField(false);
    private LanguageSelectionField languageBox = new LanguageSelectionField();

    private final User user;

    public BasicInfoChangeWindow(final User user) {
        super(AppContext.getMessage(UserI18nEnum.WINDOW_CHANGE_BASIC_INFO_TITLE));
        this.user = user;
        this.withModal(true).withResizable(false).withWidth("600px").withCenter();
        this.initUI();
    }

    private void initUI() {
        final MVerticalLayout mainLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false)).withFullWidth();

        final GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(1, 6);

        passInfo.addComponent(txtFirstName, AppContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0, 0);
        passInfo.addComponent(txtLastName, AppContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0, 1);
        txtLastName.setRequired(true);
        passInfo.addComponent(txtEmail, AppContext.getMessage(GenericI18Enum.FORM_EMAIL), 0, 2);
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

        MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebUIConstants.BUTTON_OPTION);

        MButton saveBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> changeUserInfo())
                .withStyleName(WebUIConstants.BUTTON_ACTION).withIcon(FontAwesome.SAVE);

        MHorizontalLayout hlayoutControls = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(new MarginInfo(false, true, false, true));
        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

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
