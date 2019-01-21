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

import com.mycollab.core.SessionExpireException
import com.mycollab.vaadin.AppUI
import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
object MyCollabSession {

    const val USER_VAL = "userVal"

    const val EVENT_BUS_VAL = "eventBusVal"

    const val CURRENT_PROJECT = "project"

    const val PROJECT_MEMBER = "project_member"

    const val CURRENT_MODULE = "currentModule"

    const val CONTROLLER_REGISTRY = "CONTROLLER_REGISTRY"

    const val PRESENTER_VAL = "presenterMap"

    const val VIEW_MANAGER_VAL = "viewMap"

    @JvmStatic fun putSessionVariable(key: String, value: Any) = try {
        VaadinSession.getCurrent().setAttribute(key, value)
    } catch (e: Exception) {
        throw SessionExpireException("Expire Exception")
    }

    @JvmStatic fun getSessionVariable(key: String): Any? = try {
        VaadinSession.getCurrent().getAttribute(key)
    } catch (e: Exception) {
        throw SessionExpireException("Expire Exception")
    }

    @JvmStatic fun removeSessionVariable(key: String) = try {
        VaadinSession.getCurrent().setAttribute(key, null)
    } catch (e: Exception) {
        throw SessionExpireException("Expire Exception")
    }

    /**
     * @param key
     * @param value
     */
    @JvmStatic fun putCurrentUIVariable(key: String, value: Any?) = try {
        (UI.getCurrent() as AppUI).setAttribute(key, value)
    } catch (e: Exception) {
        throw SessionExpireException("Expire Exception")
    }

    /**
     * @param key
     */
    @JvmStatic fun removeCurrentUIVariable(key: String) = try {
        (UI.getCurrent() as AppUI).setAttribute(key, null)
    } catch (e: Exception) {
        throw SessionExpireException("Expire Exception")
    }

    /**
     * @param key
     * @return
     */
    @JvmStatic fun getCurrentUIVariable(key: String): Any? = try {
        (UI.getCurrent() as AppUI).getAttribute(key)
    } catch (e: Exception) {
        throw SessionExpireException("Expire Exception")
    }
}