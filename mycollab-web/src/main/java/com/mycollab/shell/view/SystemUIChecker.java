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
package com.mycollab.shell.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class SystemUIChecker {
    /**
     * @return true if the system has the valid smtp account, false if otherwise
     */
    public static boolean hasValidSmtpAccount() {
        if (!SiteConfiguration.isDemandEdition()) {
            ExtMailService extMailService = AppContextUtil.getSpringBean(ExtMailService.class);
            if (!extMailService.isMailSetupValid()) {
                if (UserUIContext.isAdmin()) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(ShellI18nEnum.WINDOW_STMP_NOT_SETUP),
                            UserUIContext.getMessage(ShellI18nEnum.WINDOW_SMTP_CONFIRM_SETUP_FOR_ADMIN),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(null, new String[]{"setup"}));
                                }
                            });

                } else {
                    NotificationUtil.showErrorNotification(UserUIContext.getMessage(ShellI18nEnum.WINDOW_SMTP_CONFIRM_SETUP_FOR_USER));
                }
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}
