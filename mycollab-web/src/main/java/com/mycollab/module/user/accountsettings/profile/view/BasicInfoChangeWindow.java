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
package com.mycollab.module.user.accountsettings.profile.view;

import com.google.common.base.MoreObjects;
import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.module.user.ui.components.LanguageSelectionField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.Utils;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.TimeZoneSelectionField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
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
    private DateField birthdayField = new DateField();
    private TimeZoneSelectionField timeZoneField = new TimeZoneSelectionField(false);
    private LanguageSelectionField languageBox = new LanguageSelectionField();

    private final User user;

    BasicInfoChangeWindow(final User user) {
        super(UserUIContext.getMessage(UserI18nEnum.WINDOW_CHANGE_BASIC_INFO_TITLE));
        this.user = user;
        this.withModal(true).withResizable(false).withWidth("600px").withCenter();
        this.initUI();
    }

    private void initUI() {
        MVerticalLayout mainLayout = new MVerticalLayout().withMargin(true).withFullWidth();

        GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);

        passInfo.addComponent(txtFirstName, UserUIContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0, 0);
        passInfo.addComponent(txtLastName, UserUIContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0, 1);
        txtLastName.setRequiredIndicatorVisible(true);
        passInfo.addComponent(txtEmail, UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL), 0, 2);
        txtEmail.setRequiredIndicatorVisible(true);
        passInfo.addComponent(birthdayField, UserUIContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 0, 3);
        birthdayField.setValue(user.getBirthday());

        passInfo.addComponent(timeZoneField, UserUIContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 4);
        timeZoneField.setValue(user.getTimezone());

        passInfo.addComponent(languageBox, UserUIContext.getMessage(UserI18nEnum.FORM_LANGUAGE),
                UserUIContext.getMessage(ShellI18nEnum.OPT_SUPPORTED_LANGUAGES_INTRO), 0, 5);
        languageBox.setValue(user.getLanguage());

        txtFirstName.setValue(MoreObjects.firstNonNull(user.getFirstname(), ""));
        txtLastName.setValue(MoreObjects.firstNonNull(user.getLastname(), ""));
        txtEmail.setValue(MoreObjects.firstNonNull(user.getEmail(), ""));

        mainLayout.addComponent(passInfo.getLayout());
        mainLayout.setComponentAlignment(passInfo.getLayout(), Alignment.TOP_LEFT);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> changeUserInfo())
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.CLIPBOARD).withClickShortcut(ShortcutAction.KeyCode.ENTER);

        MHorizontalLayout hlayoutControls = new MHorizontalLayout(cancelBtn, saveBtn);
        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

        this.setContent(mainLayout);
    }

    private void changeUserInfo() {
        txtLastName.removeStyleName("errorField");
        txtEmail.removeStyleName("errorField");

        if (txtLastName.getValue().equals("")) {
            NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                    UserUIContext.getMessage(GenericI18Enum.FORM_LASTNAME)));
            txtLastName.addStyleName("errorField");
            return;
        }

        if (txtEmail.getValue().equals("")) {
            NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                    UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL)));
            txtLastName.addStyleName("errorField");
            return;
        }

        user.setFirstname(txtFirstName.getValue());
        user.setLastname(txtLastName.getValue());
        user.setEmail(txtEmail.getValue());
        user.setBirthday(birthdayField.getValue());
        user.setLanguage(languageBox.getValue());
        user.setTimezone(timeZoneField.getValue());

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        userService.updateWithSession(user, UserUIContext.getUsername());
        close();
        Utils.reloadPage();
    }
}
