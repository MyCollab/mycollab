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
        private const val serialVersionUID = 1L

        internal const val MOBILE_APP = "com.mycollab.mobile.MobileApplication"
        internal const val DESKTOP_APP = "com.mycollab.web.DesktopApplication"
    }
}
