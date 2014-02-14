package com.esofthead.mycollab.mobile.utils;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class NotificationUtil {

	public static void showNotification(String caption) {
		showNotification(caption, null, Type.HUMANIZED_MESSAGE);
	}

	public static void showWarningNotification(String description) {
		showNotification(
				LocalizationHelper
						.getMessage(GenericI18Enum.WARNING_WINDOW_TITLE),
				description, Type.WARNING_MESSAGE);
	}

	public static void showErrorNotification(String description) {
		showNotification(
				LocalizationHelper
						.getMessage(GenericI18Enum.ERROR_WINDOW_TITLE),
				description, Type.ERROR_MESSAGE);
	}

	public static void showNotification(String caption, String description,
			Type type) {
		Notification warnNotif = new Notification(caption, description, type);
		warnNotif.setHtmlContentAllowed(true);
		warnNotif.setDelayMsec(3000);

		if (Page.getCurrent() != null) {
			warnNotif.show(Page.getCurrent());
		} else {
			warnNotif.show(MobileApplication.getInstance().getPage());
		}

	}

	public static void showGotoLastRecordNotification() {
		showNotification(
				LocalizationHelper
						.getMessage(GenericI18Enum.INFORMATION_WINDOW_TITLE),
				LocalizationHelper
						.getMessage(GenericI18Enum.INFORMATION_GOTO_LAST_RECORD),
				Type.HUMANIZED_MESSAGE);
	}

	public static void showGotoFirstRecordNotification() {
		showNotification(
				LocalizationHelper
						.getMessage(GenericI18Enum.INFORMATION_WINDOW_TITLE),
				LocalizationHelper
						.getMessage(GenericI18Enum.INFORMATION_GOTO_FIRST_RECORD),
				Type.HUMANIZED_MESSAGE);
	}

	public static void showRecordNotExistNotification() {
		showNotification(
				LocalizationHelper
						.getMessage(GenericI18Enum.INFORMATION_WINDOW_TITLE),
				LocalizationHelper
						.getMessage(GenericI18Enum.INFORMATION_RECORD_IS_NOT_EXISTED_MESSAGE),
				Type.HUMANIZED_MESSAGE);
	}

	public static void showMessagePermissionAlert() {
		showNotification(
				LocalizationHelper
						.getMessage(GenericI18Enum.WARNING_WINDOW_TITLE),
				"Sorry! You do not have permission to do this task.",
				Type.WARNING_MESSAGE);
	}
}
