/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.vaadin.UserUIContext
import com.vaadin.server.Page
import com.vaadin.ui.Notification
import com.vaadin.ui.Notification.Type
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object NotificationUtil {
    private val LOG = LoggerFactory.getLogger(NotificationUtil::class.java)

    @JvmStatic
    fun showWarningNotification(description: String) {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE), description, Type.WARNING_MESSAGE)
    }

    @JvmStatic
    fun showErrorNotification(description: String) {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_ERROR_TITLE), description, Type.ERROR_MESSAGE)
    }

    @JvmOverloads
    @JvmStatic
    fun showNotification(caption: String, description: String, type: Type = Type.HUMANIZED_MESSAGE) {
        val notification = Notification(caption, description, type)
        notification.isHtmlContentAllowed = true
        notification.delayMsec = 3000

        when {
            Page.getCurrent() != null -> notification.show(Page.getCurrent())
            else -> LOG.error("Current page is null")
        }

    }

    @JvmStatic
    fun showGotoLastRecordNotification() {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_GOTO_LAST_RECORD),
                Type.HUMANIZED_MESSAGE)
    }

    @JvmStatic
    fun showGotoFirstRecordNotification() {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_GOTO_FIRST_RECORD),
                Type.HUMANIZED_MESSAGE)
    }

    @JvmStatic
    fun showRecordNotExistNotification() {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_RECORD_IS_NOT_EXISTED),
                Type.HUMANIZED_MESSAGE)
    }

    @JvmStatic
    fun showMessagePermissionAlert() {
        showNotification(
                UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_NO_PERMISSION_DO_TASK),
                Type.WARNING_MESSAGE)
    }

    @JvmStatic
    fun showFeatureNotPresentInSubscription() {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_FEATURE_NOT_AVAILABLE_IN_SUBSCRIPTION),
                Type.WARNING_MESSAGE)
    }
}
