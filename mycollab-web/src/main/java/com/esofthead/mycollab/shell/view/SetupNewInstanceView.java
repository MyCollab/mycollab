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
package com.esofthead.mycollab.shell.view;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.TimeZoneSelectionField;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.web.DesktopApplication;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.TimeZone;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class SetupNewInstanceView extends MVerticalLayout {
    public SetupNewInstanceView() {
        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        MVerticalLayout content = new MVerticalLayout().withWidth("600px");
        this.with(content);
        content.with(ELabel.h2("Last step, you are almost there!").withWidthUndefined());
        content.with(ELabel.h3("All fields are required *").withStyleName("overdue").withWidthUndefined());
        GridFormLayoutHelper formLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, 4);
        formLayoutHelper.getLayout().setWidth("600px");
        final TextField adminField = formLayoutHelper.addComponent(new TextField(), "Admin email", 0, 0);
        final PasswordField passwordField = formLayoutHelper.addComponent(new PasswordField(), "Admin password", 0, 1);
        final PasswordField retypePasswordField = formLayoutHelper.addComponent(new PasswordField(), "Retype Admin " +
                "password", 0, 2);
        final TimeZoneSelectionField timeZoneSelectionField = formLayoutHelper.addComponent(new TimeZoneSelectionField(false)
                , "Timezone", 0, 3);
        content.with(formLayoutHelper.getLayout());

        Button installBtn = new Button("Setup", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String adminName = adminField.getValue();
                String password = passwordField.getValue();
                String retypePassword = retypePasswordField.getValue();
                if (!StringUtils.isValidEmail(adminName)) {
                    NotificationUtil.showErrorNotification("Invalid email value");
                    return;
                }

                if (!password.equals(retypePassword)) {
                    NotificationUtil.showErrorNotification("Password is not match");
                    return;
                }
                BillingAccountService billingAccountService = ApplicationContextUtil.getSpringBean
                        (BillingAccountService.class);
                String timezoneDbId = timeZoneSelectionField.getValue();
                billingAccountService.createDefaultAccountData(adminName, password, timezoneDbId, true, true,
                        AppContext.getAccountId());
                ((DesktopApplication) UI.getCurrent()).doLogin(adminName, password, false);

            }
        });
        installBtn.addStyleName(UIConstants.BUTTON_ACTION);
        content.with(installBtn).withAlign(installBtn, Alignment.TOP_RIGHT);
    }
}
