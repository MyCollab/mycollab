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
package com.mycollab.vaadin.mvp

import com.mycollab.core.MyCollabException
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.mvp.service.ComponentScannerService
import com.mycollab.vaadin.ui.MyCollabSession

import java.util.HashMap

import com.mycollab.vaadin.ui.MyCollabSession.VIEW_MANAGER_VAL

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object ViewManager {

    @JvmStatic fun <T : CacheableComponent> getCacheComponent(viewClass: Class<T>): T? {
        var viewMap = MyCollabSession.getCurrentUIVariable(MyCollabSession.VIEW_MANAGER_VAL) as MutableMap<Class<*>, Any>?
        if (viewMap == null) {
            viewMap = mutableMapOf()
            MyCollabSession.putCurrentUIVariable(MyCollabSession.VIEW_MANAGER_VAL, viewMap)
        }

        try {
            val policy = viewClass.getAnnotation(LoadPolicy::class.java)
            if (policy != null && policy.scope == ViewScope.PROTOTYPE) {
                return createInstanceFromCls(viewClass)
            }
            var value = viewMap[viewClass] as? T
            return when (value) {
                null -> {
                    value = createInstanceFromCls(viewClass)
                    viewMap.put(viewClass, value)
                    value
                }
                else -> value
            }
        } catch (e: Exception) {
            throw MyCollabException("Can not create view instance of class: " + viewClass, e)
        }

    }

    @Throws(IllegalAccessException::class, InstantiationException::class)
    private fun <T> createInstanceFromCls(viewClass: Class<T>): T {
        val componentScannerService = AppContextUtil.getSpringBean(ComponentScannerService::class.java)
        val implCls = componentScannerService.getViewImplCls(viewClass)
        if (implCls != null) {
            return implCls.newInstance() as T
        }
        throw MyCollabException("Can not find the implementation class for view " + viewClass)
    }
}
