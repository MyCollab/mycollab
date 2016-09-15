/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class NotificationUtil {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationUtil.class);

    public static void showNotification(String caption, String description) {
        showNotification(caption, description, Type.HUMANIZED_MESSAGE);
    }

    public static void showWarningNotification(String description) {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE), description, Type.WARNING_MESSAGE);
    }

    public static void showErrorNotification(String description) {
        showNotification(UserUIContext.getMessage(GenericI18Enum.WINDOW_ERROR_TITLE), description, Type.ERROR_MESSAGE);
    }

    public static void showNotification(String caption, String description, Type type) {
        Notification notification = new Notification(caption, description, type);
        notification.setHtmlContentAllowed(true);
        notification.setDelayMsec(3000);

        if (Page.getCurrent() != null) {
            notification.show(Page.getCurrent());
        } else {
            LOG.error("Current page is null");
        }

    }

    public static void showGotoLastRecordNotification() {
        showNotification(
                UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_GOTO_LAST_RECORD),
                Type.HUMANIZED_MESSAGE);
    }

    public static void showGotoFirstRecordNotification() {
        showNotification(
                UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_GOTO_FIRST_RECORD),
                Type.HUMANIZED_MESSAGE);
    }

    public static void showRecordNotExistNotification() {
        showNotification(
                UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_RECORD_IS_NOT_EXISTED),
                Type.HUMANIZED_MESSAGE);
    }

    public static void showMessagePermissionAlert() {
        showNotification(
                UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_NO_PERMISSION_DO_TASK),
                Type.WARNING_MESSAGE);
    }

    public static void showFeatureNotPresentInSubscription() {
        showNotification(
                UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE),
                UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_FEATURE_NOT_AVAILABLE_IN_SUBSCRIPTION),
                Type.WARNING_MESSAGE);
    }
}
