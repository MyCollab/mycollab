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
package com.mycollab.shell.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.dao.BillingAccountMapper;
import com.mycollab.module.user.domain.BillingAccount;
import com.mycollab.module.user.domain.BillingAccountExample;
import com.mycollab.module.user.domain.SimpleBillingAccount;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.module.user.ui.components.LanguageSelectionField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.field.DateFormatField;
import com.mycollab.vaadin.web.ui.TimeZoneSelectionField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.mycollab.web.DesktopApplication;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
class SetupNewInstanceView extends MHorizontalLayout {
    SetupNewInstanceView() {
        this.withFullSize().withStyleName(WebThemes.SCROLLABLE_CONTAINER);
        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        this.with(new MHorizontalLayout(ELabel.html(UserUIContext.getMessage(ShellI18nEnum.OPT_SUPPORTED_LANGUAGES_INTRO))
                .withStyleName(WebThemes.META_COLOR).withFullWidth()).withMargin(true).withWidth("400px").withStyleName(WebThemes.ALIGN_RIGHT, "separator"));
        MVerticalLayout formLayout = new MVerticalLayout().withWidth("600px");
        this.with(formLayout).withAlign(formLayout, Alignment.TOP_LEFT);
        formLayout.with(ELabel.h2("Last step, you are almost there!").withUndefinedWidth());
        formLayout.with(ELabel.h3("All fields are required *").withStyleName("overdue").withUndefinedWidth());

        GridFormLayoutHelper formLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.TWO_COLUMN);
        formLayoutHelper.getLayout().setWidth("600px");
        final TextField adminField = formLayoutHelper.addComponent(new TextField(), "Admin email", 0, 0);
        final PasswordField passwordField = formLayoutHelper.addComponent(new PasswordField(), "Admin password", 0, 1);
        final PasswordField retypePasswordField = formLayoutHelper.addComponent(new PasswordField(), "Retype Admin password", 0, 2);
        final DateFormatField dateFormatField = formLayoutHelper.addComponent(new DateFormatField(SimpleBillingAccount.DEFAULT_DATE_FORMAT),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_YYMMDD_FORMAT),
                UserUIContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 0, 3);

        final DateFormatField shortDateFormatField = formLayoutHelper.addComponent(new DateFormatField(SimpleBillingAccount.DEFAULT_SHORT_DATE_FORMAT),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_MMDD_FORMAT),
                UserUIContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 0, 4);

        final DateFormatField longDateFormatField = formLayoutHelper.addComponent(new DateFormatField(SimpleBillingAccount.DEFAULT_LONG_DATE_FORMAT),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_HUMAN_DATE_FORMAT),
                UserUIContext.getMessage(GenericI18Enum.FORM_DATE_FORMAT_HELP), 0, 5);

        final TimeZoneSelectionField timeZoneSelectionField = formLayoutHelper.addComponent(new TimeZoneSelectionField(false)
                , UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_TIMEZONE), 0, 6);
        timeZoneSelectionField.setValue(TimeZone.getDefault().getID());
        final LanguageSelectionField languageBox = formLayoutHelper.addComponent(new LanguageSelectionField(),
                UserUIContext.getMessage(AdminI18nEnum.FORM_DEFAULT_LANGUAGE), 0, 7);
        languageBox.setValue(Locale.US.toLanguageTag());
        formLayout.with(formLayoutHelper.getLayout());

        CheckBox createSampleDataSelection = new CheckBox("Create sample data", true);

        MButton installBtn = new MButton("Setup", clickEvent -> {
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
            ex.createCriteria().andIdEqualTo(AppUI.getAccountId());
            List<BillingAccount> billingAccounts = billingAccountMapper.selectByExample(ex);
            BillingAccount billingAccount = billingAccounts.get(0);
            billingAccount.setDefaultlanguagetag(language);
            billingAccount.setDefaultyymmddformat(dateFormat);
            billingAccount.setDefaultmmddformat(shortDateFormat);
            billingAccount.setDefaulthumandateformat(longDateFormat);
            billingAccount.setDefaulttimezone(timezoneDbId);
            billingAccountMapper.updateByPrimaryKey(billingAccount);

            BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);
            billingAccountService.createDefaultAccountData(adminName, password, timezoneDbId, language, true,
                    createSampleDataSelection.getValue(), AppUI.getAccountId());

            ((DesktopApplication) UI.getCurrent()).doLogin(adminName, password, false);
        }).withStyleName(WebThemes.BUTTON_ACTION);

        MHorizontalLayout buttonControls = new MHorizontalLayout(createSampleDataSelection, installBtn).alignAll(Alignment.MIDDLE_RIGHT);
        formLayout.with(buttonControls).withAlign(buttonControls, Alignment.MIDDLE_RIGHT);
    }

    private boolean isValidDayPattern(String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            formatter.format(LocalDateTime.now());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
