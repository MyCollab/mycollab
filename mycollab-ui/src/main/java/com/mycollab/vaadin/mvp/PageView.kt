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

import com.mycollab.vaadin.event.ViewEvent
import com.vaadin.ui.HasComponents
import com.vaadin.util.ReflectTools
import java.io.Serializable
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface PageView : HasComponents, CacheableComponent {

    fun <E> addViewListener(listener: ViewListener<E>)

    interface ViewListener<E> : EventListener, Serializable {

        fun receiveEvent(event: ViewEvent<E>)

        companion object {
            val viewInitMethod = ReflectTools.findMethod(ViewListener::class.java, "receiveEvent", ViewEvent::class.java)
        }
    }
}
