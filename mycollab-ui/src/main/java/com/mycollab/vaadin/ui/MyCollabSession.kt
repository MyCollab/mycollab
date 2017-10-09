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

    @JvmField val USER_VAL = "userVal"

    @JvmField val EVENT_BUS_VAL = "eventBusVal"

    @JvmField val CURRENT_PROJECT = "project"

    @JvmField val PROJECT_MEMBER = "project_member"

    @JvmField val CURRENT_MODULE = "currentModule"

    @JvmField val CONTROLLER_REGISTRY = "CONTROLLER_REGISTRY"

    @JvmField val PRESENTER_VAL = "presenterMap"

    @JvmField val VIEW_MANAGER_VAL = "viewMap"

    @JvmStatic fun putSessionVariable(key: String, value: Any) {
        try {
            VaadinSession.getCurrent().setAttribute(key, value)
        } catch (e: Exception) {
            throw SessionExpireException("Expire Exception")
        }

    }

    @JvmStatic fun getSessionVariable(key: String): Any? {
        try {
            return VaadinSession.getCurrent().getAttribute(key)
        } catch (e: Exception) {
            throw SessionExpireException("Expire Exception")
        }

    }

    @JvmStatic fun removeSessionVariable(key: String) {
        try {
            VaadinSession.getCurrent().setAttribute(key, null)
        } catch (e: Exception) {
            throw SessionExpireException("Expire Exception")
        }

    }

    /**
     * @param key
     * @param value
     */
    @JvmStatic fun putCurrentUIVariable(key: String, value: Any?) {
        try {
            (UI.getCurrent() as AppUI).setAttribute(key, value)
        } catch (e: Exception) {
            throw SessionExpireException("Expire Exception")
        }

    }

    /**
     * @param key
     */
    @JvmStatic fun removeCurrentUIVariable(key: String) {
        try {
            (UI.getCurrent() as AppUI).setAttribute(key, null)
        } catch (e: Exception) {
            throw SessionExpireException("Expire Exception")
        }

    }

    /**
     * @param key
     * @return
     */
    @JvmStatic fun getCurrentUIVariable(key: String): Any? {
        try {
            return (UI.getCurrent() as AppUI).getAttribute(key)
        } catch (e: Exception) {
            throw SessionExpireException("Expire Exception")
        }

    }
}