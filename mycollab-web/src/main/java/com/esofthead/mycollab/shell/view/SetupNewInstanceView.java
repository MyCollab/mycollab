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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.esofthead.mycollab.module.user.dao.BillingAccountMapper;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.BillingAccountExample;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.ui.components.LanguageSelectionField;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.TimeZoneSelectionField;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.field.DateFormatField;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.web.DesktopApplication;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;
import java.util.Locale;
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
        content.with(new ELabel(AppContext.getMessage(ShellI18nEnum.OPT_SUPPORTED_LANGUAGES_INTRO), ContentMode.HTML)
                .withStyleName(UIConstants.META_COLOR));
        GridFormLayoutHelper formLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, 8, "200px");
        formLayoutHelper.getLayout().setWidth("600px");
        final TextField adminField = formLayoutHelper.addComponent(new TextField(), "Admin email", 0, 0);
        final PasswordField passwordField = formLayoutHelper.addComponent(new PasswordField(), "Admin password", 0, 1);
        final PasswordField retypePasswordField = formLayoutHelper.addComponent(new PasswordField(), "Retype Admin password", 0, 2);
        final DateFormatField dateFormatField = formLayoutHelper.addComponent(new DateFormatField(SimpleBillingAccount.DEFAULT_DATE_FORMAT),
                AppContext.getMessage(AdminI18nEnum.FORM_DEFAULT_YYMMDD_FORMAT),
                AppContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 0, 3);

        final DateFormatField shortDateFormatField = formLayoutHelper.addComponent(new DateFormatField(SimpleBillingAccount.DEFAULT_SHORT_DATE_FORMAT),
                AppContext.getMessage(AdminI18nEnum.FORM_DEFAULT_MMDD_FORMAT),
                AppContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 0, 4);

        final DateFormatField longDateFormatField = formLayoutHelper.addComponent(new DateFormatField(SimpleBillingAccount.DEFAULT_LONG_DATE_FORMAT),
                AppContext.getMessage(AdminI18nEnum.FORM_DEFAULT_HUMAN_DATE_FORMAT),
                AppContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 0, 5);

        final TimeZoneSelectionField timeZoneSelectionField = formLayoutHelper.addComponent(new TimeZoneSelectionField(false)
                , AppContext.getMessage(AdminI18nEnum.FORM_DEFAULT_TIMEZONE), 0, 6);
        timeZoneSelectionField.setValue(TimeZone.getDefault().getID());
        final LanguageSelectionField languageBox = formLayoutHelper.addComponent(new LanguageSelectionField(),
                AppContext.getMessage(AdminI18nEnum.FORM_DEFAULT_LANGUAGE), 0, 7);
        languageBox.setValue(Locale.US.toLanguageTag());
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

                String dateFormat = dateFormatField.getValue();
                String shortDateFormat = shortDateFormatField.getValue();
                String longDateFormat = longDateFormatField.getValue();
                if (!isValidDayPattern(dateFormat) || !isValidDayPattern(shortDateFormat) || !isValidDayPattern(longDateFormat)) {
                    NotificationUtil.showErrorNotification("Invalid date format");
                    return;
                }
                String language = languageBox.getValue();
                String timezoneDbId = timeZoneSelectionField.getValue();
                BillingAccountMapper billingAccountMapper = AppContextUtil.getSpringBean(BillingAccountMapper.class);
                BillingAccountExample ex = new BillingAccountExample();
                ex.createCriteria().andIdEqualTo(AppContext.getAccountId());
                List<BillingAccount> billingAccounts = billingAccountMapper.selectByExample(ex);
                BillingAccount billingAccount = billingAccounts.get(0);
                billingAccount.setDefaultlanguagetag(language);
                billingAccount.setDefaultyymmddformat(dateFormat);
                billingAccount.setDefaultmmddformat(shortDateFormat);
                billingAccount.setDefaulthumandateformat(longDateFormat);
                billingAccount.setDefaulttimezone(timezoneDbId);
                billingAccountMapper.updateByPrimaryKey(billingAccount);

                BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);

                billingAccountService.createDefaultAccountData(adminName, password, timezoneDbId, language, true, true,
                        AppContext.getAccountId());
                ((DesktopApplication) UI.getCurrent()).doLogin(adminName, password, false);

            }
        });
        installBtn.addStyleName(UIConstants.BUTTON_ACTION);
        content.with(installBtn).withAlign(installBtn, Alignment.TOP_RIGHT);
    }

    private boolean isValidDayPattern(String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
            formatter.print(new DateTime());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
