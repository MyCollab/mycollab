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
package com.esofthead.mycollab.module.user.view;

import com.esofthead.mycollab.common.dao.RelayEmailMapper;
import com.esofthead.mycollab.common.domain.RelayEmailWithBLOBs;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.user.SendingRecoveryPasswordEmailAction;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ForgotPasswordViewImpl extends AbstractPageView implements
        ForgotPasswordView {
    private static final long serialVersionUID = 1L;

    public ForgotPasswordViewImpl() {
        this.addComponent(new ForgotPwdForm());
    }

    private class ForgotPwdForm extends CustomComponent {
        private static final long serialVersionUID = 1L;
        private final TextField nameOrEmailField;

        ForgotPwdForm() {
            CustomLayout customLayout = CustomLayoutExt.createLayout("forgotPassword");
            customLayout.setStyleName("forgotPwdForm");

            nameOrEmailField = new TextField(AppContext.getMessage(ShellI18nEnum.FORM_EMAIL));
            customLayout.addComponent(nameOrEmailField, "nameoremail");

            Button sendEmail = new Button(AppContext.getMessage(ShellI18nEnum.BUTTON_RESET_PASSWORD), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    String username = nameOrEmailField.getValue();
                    if (StringUtils.isValidEmail(username)) {
                        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
                        User user = userService.findUserByUserName(username);

                        if (user == null) {
                            NotificationUtil.showErrorNotification(AppContext
                                    .getMessage(GenericI18Enum.ERROR_USER_IS_NOT_EXISTED, username));
                        } else {
                            RelayEmailWithBLOBs relayEmail = new RelayEmailWithBLOBs();
                            relayEmail.setRecipients(username);
                            relayEmail.setEmailhandlerbean(SendingRecoveryPasswordEmailAction.class.getName());
                            relayEmail.setSaccountid(1);
                            relayEmail.setSubject("");
                            relayEmail.setBodycontent("");
                            relayEmail.setFromemail("");
                            relayEmail.setFromname("");

                            RelayEmailMapper relayEmailMapper = ApplicationContextUtil.getSpringBean(RelayEmailMapper.class);
                            relayEmailMapper.insert(relayEmail);

                            NotificationUtil.showNotification("Success", AppContext.getMessage(ShellI18nEnum.OPT_EMAIL_SENDER_NOTIFICATION));
                        }
                    } else {
                        NotificationUtil.showErrorNotification(String.format("%s is not a valid email", username));
                    }
                }
            });
            sendEmail.setStyleName(UIConstants.THEME_GREEN_LINK);
            customLayout.addComponent(sendEmail, "loginButton");

            Button memoBackBtn = new Button(AppContext.getMessage(ShellI18nEnum.BUTTON_IGNORE_RESET_PASSWORD));
            memoBackBtn.setStyleName(UIConstants.THEME_LINK);
            memoBackBtn.addClickListener(new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null));
                }
            });
            customLayout.addComponent(memoBackBtn, "forgotLink");

            this.setCompositionRoot(customLayout);
        }
    }
}
