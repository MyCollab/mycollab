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
package com.mycollab.module.user.accountsettings.setup.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.configuration.ApplicationProperties;
import com.mycollab.configuration.EmailConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.mycollab.servlet.InstallUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.io.File;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
@ViewComponent
public class SetupViewImpl extends AbstractVerticalPageView implements SetupView {
    private static Logger LOG = LoggerFactory.getLogger(SetupViewImpl.class);

    private SmtpEditForm editForm;
    private EmailConfiguration emailConf;

    public SetupViewImpl() {
        withMargin(new MarginInfo(false, true, false, true));
    }

    @Override
    public void initContent() {
        this.removeAllComponents();
        editForm = new SmtpEditForm();
        addComponent(editForm);
    }

    @Override
    public void displaySetup() {
        emailConf = SiteConfiguration.getEmailConfiguration().clone();
        editForm.display(emailConf);
    }

    private class SmtpEditForm extends AdvancedEditBeanForm<EmailConfiguration> {
        public void display(EmailConfiguration newDataSource) {
            this.setFormLayoutFactory(new BasicFormLayoutFactory());
            this.setBeanFormFieldFactory(new BasicEditFormFieldFactory(editForm));
            super.setBean(newDataSource);
        }
    }

    private class BasicFormLayoutFactory extends AbstractFormLayoutFactory {
        private GridFormLayoutHelper informationLayout;

        @Override
        public AbstractComponent getLayout() {
            AddViewLayout formAddLayout = new AddViewLayout(UserUIContext.getMessage(ShellI18nEnum.OPT_SMTP_SETTING), FontAwesome.WRENCH);
            FormContainer layout = new FormContainer();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            layout.addSection(UserUIContext.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION), informationLayout.getLayout());

            formAddLayout.addHeaderRight(createButtonControls());
            formAddLayout.addBody(layout);
            return formAddLayout;
        }

        private Layout createButtonControls() {
            final MButton closeBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLOSE),
                    clickEvent -> EventBusFactory.getInstance().post(new ProfileEvent.GotoProfileView(this)))
                    .withStyleName(WebThemes.BUTTON_OPTION);

            final MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                if (editForm.validateForm()) {
                    try {
                        InstallUtils.checkSMTPConfig(emailConf.getHost(), emailConf.getPort(), emailConf.getUser(),
                                emailConf.getPassword(), true, emailConf.getIsStartTls(), emailConf.getIsSsl());
                        saveEmailConfiguration();
                    } catch (UserInvalidInputException e) {
                        ConfirmDialogExt.show(UI.getCurrent(),
                                UserUIContext.getMessage(ShellI18nEnum.OPT_INVALID_SMTP_ACCOUNT),
                                UserUIContext.getMessage(ShellI18nEnum.OPT_CAN_NOT_ACCESS_SMTP_ACCOUNT, e.getMessage()),
                                UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                                UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                                confirmDialog -> {
                                    if (confirmDialog.isConfirmed()) {
                                        saveEmailConfiguration();
                                    }
                                });
                    }
                }
            }).withIcon(FontAwesome.SAVE).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);

            return new MHorizontalLayout(closeBtn, saveBtn);
        }

        private void saveEmailConfiguration() {
            SiteConfiguration.setEmailConfiguration(emailConf);
            File configFile = ApplicationProperties.getAppConfigFile();
            if (configFile != null) {
                try {
                    PropertiesConfiguration p = new PropertiesConfiguration(ApplicationProperties.getAppConfigFile());
                    p.setProperty(ApplicationProperties.MAIL_SMTPHOST, emailConf.getHost());
                    p.setProperty(ApplicationProperties.MAIL_USERNAME, emailConf.getUser());
                    p.setProperty(ApplicationProperties.MAIL_PASSWORD, emailConf.getPassword());
                    p.setProperty(ApplicationProperties.MAIL_PORT, emailConf.getPort());
                    p.setProperty(ApplicationProperties.MAIL_IS_TLS, emailConf.getIsStartTls());
                    p.setProperty(ApplicationProperties.MAIL_IS_SSL, emailConf.getIsSsl());
                    p.setProperty(ApplicationProperties.MAIL_NOTIFY, emailConf.getUser());
                    p.save();
                    NotificationUtil.showNotification(UserUIContext.getMessage(GenericI18Enum.OPT_CONGRATS),
                            UserUIContext.getMessage(ShellI18nEnum.OPT_SETUP_SMTP_SUCCESSFULLY));
                } catch (Exception e) {
                    LOG.error("Can not save email props", e);
                    throw new UserInvalidInputException("Can not save properties file successfully");
                }
            }
        }

        @Override
        protected Component onAttachField(Object propertyId, Field<?> field) {
            if (propertyId.equals("host")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(ShellI18nEnum.FORM_HOST),
                        UserUIContext.getMessage(ShellI18nEnum.FORM_HOST_HELP), 0, 0);
            } else if (propertyId.equals("user")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(ShellI18nEnum.FORM_MAIL_USER_NAME),
                        UserUIContext.getMessage(ShellI18nEnum.FORM_MAIL_USER_NAME_HELP), 0, 1);
            } else if (propertyId.equals("password")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(ShellI18nEnum.FORM_PASSWORD),
                        UserUIContext.getMessage(ShellI18nEnum.FORM_MAIL_PASSWORD_HELP), 0, 2);
            } else if (propertyId.equals("port")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(ShellI18nEnum.FORM_PORT),
                        UserUIContext.getMessage(ShellI18nEnum.FORM_PORT_HELP), 0, 3);
            } else if (propertyId.equals("isStartTls")) {
                return informationLayout.addComponent(field, "StartTls",
                        UserUIContext.getMessage(ShellI18nEnum.FORM_MAIL_SECURITY_HELP), 0, 4);
            } else if (propertyId.equals("isSsl")) {
                return informationLayout.addComponent(field, "Tls/Ssl",
                        UserUIContext.getMessage(ShellI18nEnum.FORM_MAIL_SECURITY_HELP), 0, 5);
            }
            return null;
        }
    }

    private static class BasicEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<EmailConfiguration> {
        private static final long serialVersionUID = 1L;

        BasicEditFormFieldFactory(GenericBeanForm<EmailConfiguration> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("isStartTls")) {
                return new CheckBox("", false);
            } else if (propertyId.equals("isSsl")) {
                return new CheckBox("", false);
            } else if (propertyId.equals("password")) {
                return new PasswordField();
            } else if (propertyId.equals("port")) {
                return new IntegerField();
            }
            return null;
        }
    }
}
