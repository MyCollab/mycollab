package com.mycollab.shell.view

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.ShellI18nEnum
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.shell.event.ShellEvent
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.NotificationUtil
import com.mycollab.vaadin.web.ui.ConfirmDialogExt
import com.vaadin.ui.UI

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
object SystemUIChecker {
    /**
     * @return true if the system has the valid smtp account, false if otherwise
     */
    @JvmStatic fun hasValidSmtpAccount(): Boolean {
        if (!SiteConfiguration.isDemandEdition()) {
            val extMailService = AppContextUtil.getSpringBean(ExtMailService::class.java)
            when {
                !extMailService.isMailSetupValid -> {
                    when {
                        UserUIContext.isAdmin() -> ConfirmDialogExt.show(UI.getCurrent(),
                                UserUIContext.getMessage(ShellI18nEnum.WINDOW_STMP_NOT_SETUP),
                                UserUIContext.getMessage(ShellI18nEnum.WINDOW_SMTP_CONFIRM_SETUP_FOR_ADMIN),
                                UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                                UserUIContext.getMessage(GenericI18Enum.BUTTON_NO)
                        ) { confirmDialog ->
                            if (confirmDialog.isConfirmed) {
                                EventBusFactory.getInstance().post(ShellEvent.GotoUserAccountModule(UI.getCurrent(), arrayOf("setup")))
                            }
                        }
                        else -> NotificationUtil.showErrorNotification(UserUIContext.getMessage(ShellI18nEnum.WINDOW_SMTP_CONFIRM_SETUP_FOR_USER))
                    }
                    return false
                }
                else -> return true
            }
        }
        return true
    }
}
