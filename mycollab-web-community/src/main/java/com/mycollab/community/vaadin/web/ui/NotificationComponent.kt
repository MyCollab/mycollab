package com.mycollab.community.vaadin.web.ui

import com.mycollab.common.EntryUpdateNotification
import com.mycollab.core.AbstractNotification
import com.mycollab.vaadin.ui.ELabel
import com.mycollab.vaadin.web.ui.AbstractNotificationComponent
import com.mycollab.vaadin.web.ui.WebThemes
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import org.slf4j.LoggerFactory
import org.vaadin.viritin.button.MButton

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

    inner class ProjectNotificationComponent(notification: EntryUpdateNotification) : CssLayout() {
        init {
            setWidth("100%")
            val noLabel = ELabel.html(notification.message).withStyleName(WebThemes.LABEL_WORD_WRAP)
            addComponent(noLabel)
            val readBtn = MButton("Read").withStyleName(WebThemes.BUTTON_ACTION).withListener {
                this@NotificationComponent.removeNotification(notification)
                this@NotificationComponent.notificationContainer.removeComponent(this)
            }
            addComponent(readBtn)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(NotificationComponent::class.java)
    }
}