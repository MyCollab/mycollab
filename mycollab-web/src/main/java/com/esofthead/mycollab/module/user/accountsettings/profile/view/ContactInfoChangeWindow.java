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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
class ContactInfoChangeWindow extends Window {

    private TextField txtWorkPhone = new TextField();
    private TextField txtHomePhone = new TextField();
    private TextField txtFaceBook = new TextField();
    private TextField txtTwitter = new TextField();
    private TextField txtSkype = new TextField();
    private final Validator validation;

    private final User user;

    public ContactInfoChangeWindow(final User user) {
        this.user = user;
        this.setWidth("450px");
        this.setResizable(false);
        this.setModal(true);
        this.validation = ApplicationContextUtil
                .getSpringBean(LocalValidatorFactoryBean.class);
        this.initUI();
        this.center();
        this.setCaption(AppContext
                .getMessage(UserI18nEnum.WINDOW_CHANGE_CONTACT_INFO_TITLE));
    }

    private void initUI() {
        final MVerticalLayout mainLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false)).withWidth("100%");

        final GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(1, 6);

        passInfo.addComponent(txtWorkPhone,
                AppContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0, 0);
        passInfo.addComponent(txtHomePhone,
                AppContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0, 1);
        passInfo.addComponent(txtFaceBook, "Facebook", 0, 2);
        passInfo.addComponent(txtTwitter, "Twitter", 0, 3);
        passInfo.addComponent(txtSkype, "Skype", 0, 4);

        this.txtWorkPhone.setValue(this.user.getWorkphone() == null ? ""
                : this.user.getWorkphone());
        this.txtHomePhone.setValue(this.user.getHomephone() == null ? ""
                : this.user.getHomephone());
        this.txtFaceBook.setValue(this.user.getFacebookaccount() == null ? ""
                : this.user.getFacebookaccount());
        this.txtTwitter.setValue(this.user.getTwitteraccount() == null ? ""
                : this.user.getTwitteraccount());
        this.txtSkype.setValue(this.user.getSkypecontact() == null ? ""
                : this.user.getSkypecontact());
        mainLayout.addComponent(passInfo.getLayout());
        mainLayout.setComponentAlignment(passInfo.getLayout(),
                Alignment.TOP_LEFT);

        final MHorizontalLayout hlayoutControls = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true));

        final Button cancelBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        ContactInfoChangeWindow.this.close();
                    }
                });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        final Button saveBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        changeUserInfo();
                    }
                });
        saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        saveBtn.setIcon(FontAwesome.SAVE);

        hlayoutControls.with(cancelBtn, saveBtn).alignAll(Alignment.MIDDLE_CENTER);

        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

        this.setModal(true);
        this.setContent(mainLayout);
    }

    public boolean validateForm(final Object data) {
        final Set<ConstraintViolation<Object>> violations = this.validation
                .validate(data);
        if (violations.size() > 0) {
            final StringBuilder errorMsg = new StringBuilder();

            for (@SuppressWarnings("rawtypes")
            final ConstraintViolation violation : violations) {
                errorMsg.append(violation.getMessage()).append("<br/>");

                if (violation.getPropertyPath() != null
                        && !violation.getPropertyPath().toString().equals("")) {
                    if (violation.getPropertyPath().toString()
                            .equals("workphone")) {
                        this.txtWorkPhone.addStyleName("errorField");
                    }

                    if (violation.getPropertyPath().toString()
                            .equals("homephone")) {
                        this.txtHomePhone.addStyleName("errorField");
                    }
                }
            }

            NotificationUtil.showErrorNotification(errorMsg.toString());
            return false;
        }

        return true;
    }

    private void changeUserInfo() {
        this.txtWorkPhone.removeStyleName("errorField");
        this.txtHomePhone.removeStyleName("errorField");

        this.user.setWorkphone(this.txtWorkPhone.getValue());
        this.user.setHomephone(this.txtHomePhone.getValue());
        this.user.setFacebookaccount(this.txtFaceBook.getValue());
        this.user.setTwitteraccount(this.txtTwitter.getValue());
        this.user.setSkypecontact(this.txtSkype.getValue());

        if (this.validateForm(this.user)) {
            final UserService userService = ApplicationContextUtil
                    .getSpringBean(UserService.class);
            userService.updateWithSession(this.user, AppContext.getUsername());

            EventBusFactory.getInstance().post(
                    new ProfileEvent.GotoProfileView(
                            ContactInfoChangeWindow.this, null));
            ContactInfoChangeWindow.this.close();
            Page.getCurrent().getJavaScript()
                    .execute("window.location.reload();");
        }

    }
}
