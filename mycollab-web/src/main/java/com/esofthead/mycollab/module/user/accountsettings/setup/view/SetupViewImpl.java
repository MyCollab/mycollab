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
package com.esofthead.mycollab.module.user.accountsettings.setup.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.ApplicationProperties;
import com.esofthead.mycollab.configuration.EmailConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.servlet.InstallUtils;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.io.File;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
@ViewComponent
public class SetupViewImpl extends AbstractPageView implements SetupView {
    private static Logger LOG = LoggerFactory.getLogger(SetupViewImpl.class);

    private SmtpEditForm editForm;
    private EmailConfiguration emailConf;

    public SetupViewImpl() {
        withMargin(new MarginInfo(false, true, false, true));
        editForm = new SmtpEditForm();
        addComponent(editForm);
    }

    @Override
    public void displaySetup() {
        this.removeAllComponents();
        this.addComponent(editForm);
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

    private class BasicFormLayoutFactory implements IFormLayoutFactory {
        private GridFormLayoutHelper informationLayout;

        @Override
        public ComponentContainer getLayout() {
            AddViewLayout formAddLayout = new AddViewLayout("SMTP Settings", FontAwesome.WRENCH);
            VerticalLayout layout = new VerticalLayout();
            Label organizationHeader = new Label(AppContext.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION));
            organizationHeader.setStyleName("h2");
            layout.addComponent(organizationHeader);
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 5);
            layout.addComponent(informationLayout.getLayout());

            formAddLayout.addHeaderRight(createButtonControls());
            formAddLayout.addBody(layout);
            return formAddLayout;
        }

        private Layout createButtonControls() {
            final MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(true).withStyleName("addNewControl");

            final Button closeBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CLOSE),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            EventBusFactory.getInstance().post(
                                    new ShellEvent.GotoUserAccountModule(this,
                                            new String[]{"preview"}));
                        }

                    });
            closeBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            buttonControls.with(closeBtn).withAlign(closeBtn, Alignment.MIDDLE_RIGHT);

            final Button saveBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            if (editForm.validateForm()) {
                                String isTLS = (emailConf.getIsTls()) ? "TLS" : "";
                                boolean isSetupValid = InstallUtils.checkSMTPConfig(emailConf.getHost(), emailConf.getPort(), emailConf.getUser(), emailConf.getPassword(), true, isTLS);
                                if (!isSetupValid) {
                                    ConfirmDialogExt.show(
                                            UI.getCurrent(),
                                            "Invalid SMTP account?",
                                            "We can not connect to the SMTP server. Save the configuration anyway?",
                                            AppContext
                                                    .getMessage(GenericI18Enum.BUTTON_YES),
                                            AppContext
                                                    .getMessage(GenericI18Enum.BUTTON_NO),
                                            new ConfirmDialog.Listener() {
                                                private static final long serialVersionUID = 1L;

                                                @Override
                                                public void onClose(ConfirmDialog dialog) {
                                                    if (dialog.isConfirmed()) {
                                                        saveEmailConfiguration();
                                                    }
                                                }
                                            });
                                } else {
                                    saveEmailConfiguration();
                                }
                            }
                        }
                    });
            saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            saveBtn.setIcon(FontAwesome.SAVE);
            saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            buttonControls.with(saveBtn).withAlign(saveBtn, Alignment.MIDDLE_RIGHT);
            return buttonControls;
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
                    p.setProperty(ApplicationProperties.MAIL_IS_TLS, emailConf.getIsTls());
                    p.save();
                    NotificationUtil.showNotification("Congrats", "Set up SMTP account successfully");
                } catch (Exception e) {
                    LOG.error("Can not save email props", e);
                    throw new UserInvalidInputException("Can not save properties file successfully");
                }
            }
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {
            if (propertyId.equals("host")) {
                this.informationLayout.addComponent(field, "Host", 0, 0);
            } else if (propertyId.equals("user")) {
                this.informationLayout.addComponent(field, "User Name", 0, 1);
            } else if (propertyId.equals("password")) {
                this.informationLayout.addComponent(field, "Password", 0, 2);
            } else if (propertyId.equals("port")) {
                this.informationLayout.addComponent(field, "Port", 0, 3);
            } else if (propertyId.equals("isTls")) {
                this.informationLayout.addComponent(field, "SSL/TLS", 0, 4);
            }
        }
    }

    private static class BasicEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<EmailConfiguration> {
        private static final long serialVersionUID = 1L;

        BasicEditFormFieldFactory(GenericBeanForm<EmailConfiguration> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("isTls")) {
                return new CheckBox("", false);
            }
            return null;
        }
    }
}
