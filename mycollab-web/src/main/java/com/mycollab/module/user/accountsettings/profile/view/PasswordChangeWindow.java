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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.configuration.EnDecryptHelper;
import com.mycollab.core.InvalidPasswordException;
import com.mycollab.core.utils.PasswordCheckerUtil;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PasswordChangeWindow extends MWindow {
    private PasswordField txtNewPassword;
    private PasswordField txtConfirmPassword;

    private final User user;

    public PasswordChangeWindow(final User user) {
        super(UserUIContext.getMessage(UserI18nEnum.WINDOW_CHANGE_PASSWORD_TITLE));
        this.withWidth("600px").withCenter().withResizable(false).withModal(true);
        this.initUI();
        this.user = user;
    }

    private void initUI() {
        final MVerticalLayout mainLayout = new MVerticalLayout().withFullWidth();

        Label lbInstruct1 = new ELabel(UserUIContext.getMessage(UserI18nEnum.MSG_PASSWORD_INSTRUCT_LABEL_1)).withFullWidth();
        mainLayout.addComponent(lbInstruct1);
        mainLayout.setComponentAlignment(lbInstruct1, Alignment.MIDDLE_LEFT);

        final Label lbInstruct2 = new ELabel(UserUIContext.getMessage(UserI18nEnum.MSG_PASSWORD_INSTRUCT_LABEL_2)).withFullWidth();
        mainLayout.addComponent(lbInstruct2);
        mainLayout.setComponentAlignment(lbInstruct2, Alignment.MIDDLE_LEFT);

        GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);

        txtNewPassword = new PasswordField();
        passInfo.addComponent(txtNewPassword, UserUIContext.getMessage(ShellI18nEnum.OPT_NEW_PASSWORD), 0, 0);

        txtConfirmPassword = new PasswordField();
        passInfo.addComponent(txtConfirmPassword, UserUIContext.getMessage(ShellI18nEnum.OPT_CONFIRMED_PASSWORD), 0, 1);

        mainLayout.addComponent(passInfo.getLayout());
        mainLayout.setComponentAlignment(passInfo.getLayout(), Alignment.MIDDLE_CENTER);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> changePassword())
                .withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);

        MHorizontalLayout hlayoutControls = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(false);
        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

        this.setContent(mainLayout);
    }

    private void changePassword() {
        txtNewPassword.removeStyleName("errorField");
        txtConfirmPassword.removeStyleName("errorField");

        if (!txtNewPassword.getValue().equals(txtConfirmPassword.getValue())) {
            NotificationUtil.showErrorNotification(UserUIContext.getMessage(UserI18nEnum.ERROR_PASSWORDS_ARE_NOT_MATCH));
            txtNewPassword.addStyleName("errorField");
            txtConfirmPassword.addStyleName("errorField");
            return;
        }

        try {
            PasswordCheckerUtil.checkValidPassword(txtNewPassword.getValue());
        } catch (InvalidPasswordException e) {
            NotificationUtil.showErrorNotification(e.getMessage());
        }

        user.setPassword(EnDecryptHelper.encryptSaltPassword(txtNewPassword.getValue()));

        final UserService userService = AppContextUtil.getSpringBean(UserService.class);
        userService.updateWithSession(user, UserUIContext.getUsername());
        close();
    }
}
