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
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ContactInfoChangeWindow extends MWindow {
    private TextField txtWorkPhone = new TextField();
    private TextField txtHomePhone = new TextField();
    private TextField txtFaceBook = new TextField();
    private TextField txtTwitter = new TextField();
    private TextField txtSkype = new TextField();
    private Validator validation;

    private User user;

    ContactInfoChangeWindow(User user) {
        super(UserUIContext.getMessage(UserI18nEnum.WINDOW_CHANGE_CONTACT_INFO_TITLE));
        this.user = user;
        this.withWidth("450px").withResizable(false).withModal(true).withCenter();
        this.validation = AppContextUtil.getValidator();
        this.initUI();
    }

    private void initUI() {
        MVerticalLayout mainLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false)).withFullWidth();

        GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(1, 6);

        passInfo.addComponent(txtWorkPhone, UserUIContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0, 0);
        passInfo.addComponent(txtHomePhone, UserUIContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0, 1);
        passInfo.addComponent(txtFaceBook, "Facebook", 0, 2);
        passInfo.addComponent(txtTwitter, "Twitter", 0, 3);
        passInfo.addComponent(txtSkype, "Skype", 0, 4);

        txtWorkPhone.setValue(MoreObjects.firstNonNull(user.getWorkphone(), ""));
        txtHomePhone.setValue(MoreObjects.firstNonNull(user.getHomephone(), ""));
        txtFaceBook.setValue(MoreObjects.firstNonNull(user.getFacebookaccount(), ""));
        txtTwitter.setValue(MoreObjects.firstNonNull(user.getTwitteraccount(), ""));
        txtSkype.setValue(MoreObjects.firstNonNull(user.getSkypecontact(), ""));
        mainLayout.addComponent(passInfo.getLayout());
        mainLayout.setComponentAlignment(passInfo.getLayout(), Alignment.TOP_LEFT);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> changeUserInfo())
                .withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);

        MHorizontalLayout hlayoutControls = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(new MarginInfo(false, true, false, true));
        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);
        this.setContent(mainLayout);
    }

    public boolean validateForm(final Object data) {
        Set<ConstraintViolation<Object>> violations = this.validation.validate(data);
        if (violations.size() > 0) {
            final StringBuilder errorMsg = new StringBuilder();

            for (ConstraintViolation violation : violations) {
                errorMsg.append(violation.getMessage()).append("<br/>");

                if (violation.getPropertyPath() != null && !violation.getPropertyPath().toString().equals("")) {
                    if (violation.getPropertyPath().toString().equals("workphone")) {
                        txtWorkPhone.addStyleName("errorField");
                    }

                    if (violation.getPropertyPath().toString().equals("homephone")) {
                        txtHomePhone.addStyleName("errorField");
                    }
                }
            }

            NotificationUtil.showErrorNotification(errorMsg.toString());
            return false;
        }

        return true;
    }

    private void changeUserInfo() {
        txtWorkPhone.removeStyleName("errorField");
        txtHomePhone.removeStyleName("errorField");

        user.setWorkphone(txtWorkPhone.getValue());
        user.setHomephone(txtHomePhone.getValue());
        user.setFacebookaccount(txtFaceBook.getValue());
        user.setTwitteraccount(txtTwitter.getValue());
        user.setSkypecontact(txtSkype.getValue());

        if (validateForm(user)) {
            UserService userService = AppContextUtil.getSpringBean(UserService.class);
            userService.updateWithSession(user, UserUIContext.getUsername());
            close();
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }
    }
}
