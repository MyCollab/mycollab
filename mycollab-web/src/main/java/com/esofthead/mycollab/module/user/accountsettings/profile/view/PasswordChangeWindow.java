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
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.InvalidPasswordException;
import com.esofthead.mycollab.core.utils.PasswordCheckerUtil;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PasswordChangeWindow extends Window {
    private PasswordField txtNewPassword;
    private PasswordField txtConfirmPassword;

    private final User user;

    public PasswordChangeWindow(final User user) {
        this.setWidth("600px");
        this.initUI();
        this.center();
        this.setResizable(false);
        this.setModal(true);
        this.user = user;
        this.setCaption(AppContext.getMessage(UserI18nEnum.WINDOW_CHANGE_PASSWORD_TITLE));
    }

    private void initUI() {
        final MVerticalLayout mainLayout = new MVerticalLayout().withWidth("100%");

        final Label lbInstruct1 = new Label(AppContext.getMessage(UserI18nEnum.MSG_PASSWORD_INSTRUCT_LABEL_1));
        mainLayout.addComponent(lbInstruct1);
        mainLayout.setComponentAlignment(lbInstruct1, Alignment.MIDDLE_LEFT);

        final Label lbInstruct2 = new Label(AppContext.getMessage(UserI18nEnum.MSG_PASSWORD_INSTRUCT_LABEL_2));
        mainLayout.addComponent(lbInstruct2);
        mainLayout.setComponentAlignment(lbInstruct2, Alignment.MIDDLE_LEFT);

        GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

        txtNewPassword = new PasswordField();
        passInfo.addComponent(txtNewPassword, "New Password", 0, 0);

        txtConfirmPassword = new PasswordField();
        passInfo.addComponent(txtConfirmPassword, "Confirmed Password", 0, 1);

        passInfo.getLayout().setSpacing(true);
        mainLayout.addComponent(passInfo.getLayout());
        mainLayout.setComponentAlignment(passInfo.getLayout(), Alignment.MIDDLE_CENTER);

        final MHorizontalLayout hlayoutControls = new MHorizontalLayout().withMargin(true);

        final Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                PasswordChangeWindow.this.close();
            }
        });
        cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);

        final Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                PasswordChangeWindow.this.changePassword();
            }
        });
        saveBtn.setStyleName(UIConstants.BUTTON_ACTION);
        saveBtn.setIcon(FontAwesome.SAVE);

        hlayoutControls.with(saveBtn, cancelBtn);

        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

        this.setModal(true);
        this.setContent(mainLayout);
    }

    private void changePassword() {
        txtNewPassword.removeStyleName("errorField");
        txtConfirmPassword.removeStyleName("errorField");

        if (!txtNewPassword.getValue().equals(txtConfirmPassword.getValue())) {
            NotificationUtil.showErrorNotification(AppContext.getMessage(UserI18nEnum.ERROR_PASSWORDS_ARE_NOT_MATCH));
            txtNewPassword.addStyleName("errorField");
            txtConfirmPassword.addStyleName("errorField");
            return;
        }

        try {
            PasswordCheckerUtil.checkValidPassword(txtNewPassword.getValue());
        } catch (InvalidPasswordException e) {
            NotificationUtil.showErrorNotification(e.getMessage());
        }

        user.setPassword(PasswordEncryptHelper.encryptSaltPassword(txtNewPassword.getValue()));

        final UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        userService.updateWithSession(user, AppContext.getUsername());

        EventBusFactory.getInstance().post(new ProfileEvent.GotoProfileView(PasswordChangeWindow.this, null));
        PasswordChangeWindow.this.close();
    }
}
