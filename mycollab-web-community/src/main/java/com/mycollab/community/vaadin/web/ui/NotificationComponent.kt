package com.mycollab.community.vaadin.web.ui

import com.mycollab.core.AbstractNotification
import com.mycollab.common.EntryUpdateNotification
import com.mycollab.vaadin.ui.ELabel
import com.mycollab.vaadin.web.ui.AbstractNotificationComponent
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class NotificationComponent : AbstractNotificationComponent() {
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

    }

    class ProjectNotificationComponent(notification: EntryUpdateNotification) : CssLayout() {
        init {
            val noLabel = ELabel.html(notification.message)
            addComponent(noLabel)
            addLayoutClickListener { _ -> println(notification.message) }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(NotificationComponent::class.java)
    }
}