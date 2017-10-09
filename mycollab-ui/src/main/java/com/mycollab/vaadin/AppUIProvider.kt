package com.mycollab.vaadin

import com.mycollab.core.MyCollabException
import com.vaadin.server.UIClassSelectionEvent
import com.vaadin.server.UIProvider
import com.vaadin.ui.UI

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class AppUIProvider : UIProvider() {

    override fun getUIClass(event: UIClassSelectionEvent): Class<out UI>? {
        val request = event.request
        val uiClass: String
        val userAgent: String

        try {
            userAgent = request.getHeader("user-agent").toLowerCase()
        } catch (e: Exception) {
            return null
        }

        uiClass = if (userAgent.contains("mobile")) MOBILE_APP else DESKTOP_APP

        try {
            return Class.forName(uiClass) as Class<out UI>
        } catch (e: ClassNotFoundException) {
            throw MyCollabException(e)
        }

    }

    companion object {
        private val serialVersionUID = 1L

        @JvmField internal val MOBILE_APP = "com.mycollab.mobile.MobileApplication"
        @JvmField internal val DESKTOP_APP = "com.mycollab.web.DesktopApplication"
    }
}
