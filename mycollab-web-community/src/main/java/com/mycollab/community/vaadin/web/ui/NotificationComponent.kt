package com.mycollab.community.vaadin.web.ui

import com.mycollab.common.EntryUpdateNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.service.NotificationItemService
import com.mycollab.core.AbstractNotification
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.AsyncInvoker
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.ELabel
import com.mycollab.vaadin.web.ui.AbstractNotificationComponent
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.Notification
import com.vaadin.ui.UI
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class NotificationComponent : AbstractNotificationComponent() {
    init {
        AsyncInvoker.access(UI.getCurrent(), object : AsyncInvoker.PageCommand() {
            override fun run() {
                val notificationItemService = AppContextUtil.getSpringBean(NotificationItemService::class.java)
                val notifications = notificationItemService.findUnreadNotificationItemsByUser(UserUIContext.getUsername(), AppUI.accountId)
                notifications.forEach {
                    val notification = EntryUpdateNotification(it.notificationuser, it.module, it.type, it.typeid, it.message)
                    addNotification(notification)
                }
            }
        })
    }

    override fun buildComponentFromNotificationExclusive(item: AbstractNotification): Component? {
        return when (item) {
            is EntryUpdateNotification -> ProjectNotificationComponent(item)
            else -> {
                LOG.error("Do not support notification type $item")
                null
            }
        }
    }

    override fun displayTrayNotificationExclusive(item: AbstractNotification) {
        if (item is EntryUpdateNotification) {
            val no = Notification(UserUIContext.getMessage(GenericI18Enum.WINDOW_INFORMATION_TITLE), item.message,
                    Notification.Type.TRAY_NOTIFICATION)
            no.isHtmlContentAllowed = true
            no.delayMsec = 3000

            AsyncInvoker.access(ui, object : AsyncInvoker.PageCommand() {
                override fun run() {
                    no.show(ui.page)
                }
            })
        }
    }

    inner class ProjectNotificationComponent(notification: EntryUpdateNotification) : CssLayout() {
        init {
            val noLabel = ELabel.html(notification.message).withFullWidth()
            addComponent(noLabel)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(NotificationComponent::class.java)
    }
}