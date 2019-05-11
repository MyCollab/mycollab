/**
 * Copyright © MyCollab
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui

import com.mycollab.configuration.SiteConfiguration
import com.vaadin.data.HasValue
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.Page
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServletRequest
import com.vaadin.ui.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.vaadin.addons.stackpanel.StackPanel

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
object UIUtils {
    private val LOG = LoggerFactory.getLogger(UIUtils::class.java)

    val browserWidth: Int
        get() = UI.getCurrent().page.browserWindowWidth

    val browserHeight: Int
        get() = UI.getCurrent().page.browserWindowHeight

    @JvmStatic fun <T> getRoot(container: Component, type: Class<T>): T? {
        var parent: HasComponents? = container.parent
        while (parent != null) {
            if (type.isAssignableFrom(parent.javaClass)) {
                return parent as T?
            } else {
                parent = parent.parent
            }
        }
        return null
    }

    @JvmStatic fun removeChildAssociate(container: Component, type: Class<*>): Boolean {
        val parent = container.parent
        if (parent != null) {
            return if (type.isAssignableFrom(parent.javaClass) && parent is ComponentContainer) {
                parent.removeComponent(container)
                true
            } else {
                removeChildAssociate(parent, type)
            }
        }
        return false
    }

    @JvmStatic fun makeStackPanel(panel: Panel) {
        val stackPanel = StackPanel.extend(panel)
        stackPanel.setToggleDownIcon(VaadinIcons.ANGLE_RIGHT)
        stackPanel.setToggleUpIcon(VaadinIcons.ANGLE_DOWN)
    }

    fun getComponent(compClassName: String): HasValue<*>? = try {
        Class.forName(compClassName).newInstance() as HasValue<*>
    } catch (e: Exception) {
        LOG.warn("Can not init component $compClassName", e)
        null
    }

    @JvmStatic fun getSubDomain(request: VaadinRequest): String {
        val servletRequest = request as VaadinServletRequest
        return when {
            SiteConfiguration.isDemandEdition() -> servletRequest.serverName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            else -> servletRequest.serverName
        }
    }

    @JvmStatic fun reloadPage(): Unit = Page.getCurrent().javaScript.execute("window.location.reload();")
}
