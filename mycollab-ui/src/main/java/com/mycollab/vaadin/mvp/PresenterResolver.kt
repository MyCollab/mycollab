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

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object PresenterResolver {

     @JvmStatic fun <P : IPresenter<*>> getPresenter(presenterClass: Class<P>): P {
        var presenterMap = MyCollabSession.getCurrentUIVariable(MyCollabSession.PRESENTER_VAL) as MutableMap<Class<*>, Any>?
        if (presenterMap == null) {
            presenterMap = mutableMapOf()
            MyCollabSession.putCurrentUIVariable(MyCollabSession.PRESENTER_VAL, presenterMap)
        }

        var value: P? = presenterMap[presenterClass] as? P
         return when (value) {
             null -> {
                 value = initPresenter(presenterClass)
                 presenterMap.put(presenterClass, value)
                 value
             }
             else -> {
                 val policy = presenterClass.getAnnotation(LoadPolicy::class.java)
                 if (policy != null && policy.scope == ViewScope.PROTOTYPE) {
                     value = initPresenter(presenterClass)
                     presenterMap.put(presenterClass, value)
                 }
                 value
             }
         }
    }

    @JvmStatic fun <P : IPresenter<*>> getPresenterAndInitView(presenterClass: Class<P>): P {
        val presenter = getPresenter(presenterClass)
        presenter.getView()
        return presenter
    }

    private fun <P : IPresenter<*>> initPresenter(presenterClass: Class<P>): P {
        var value: P? = null
        try {
            if (!presenterClass.isInterface) {
                value = presenterClass.newInstance()
            } else {
                val componentScannerService = AppContextUtil.getSpringBean(ComponentScannerService::class.java)
                val presenterClassImpl = componentScannerService.getPresenterImplCls(presenterClass)
                if (presenterClassImpl != null) {
                    value = presenterClassImpl.newInstance() as P
                }
            }
        } catch (e: Exception) {
            throw MyCollabException(e)
        }

        return when {
            value != null -> value
            else -> throw PresenterNotFoundException("Can not find instance of $presenterClass")
        }
    }
}
