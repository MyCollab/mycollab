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

import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ForgotPasswordPresenter extends AbstractPresenter<ForgotPasswordView> {
    private static final long serialVersionUID = 1L;

    public ForgotPasswordPresenter() {
        super(ForgotPasswordView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        MainWindowContainer windowContainer = (MainWindowContainer) container;
        windowContainer.removeAllComponents();

        windowContainer.addComponent(view.getWidget());

        ExtMailService extMailService = ApplicationContextUtil.getSpringBean(ExtMailService.class);
        if (!extMailService.isMailSetupValid()) {
            NotificationUtil.showErrorNotification(AppContext.getMessage(ShellI18nEnum.WINDOW_SMTP_CONFIRM_SETUP_FOR_USER));
        }

        AppContext.addFragment("user/forgotpassword", AppContext.getMessage(ShellI18nEnum.OPT_FORGOT_PASSWORD_VIEW_TITLE));
    }
}
