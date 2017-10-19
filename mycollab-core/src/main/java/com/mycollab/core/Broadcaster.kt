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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.core

import java.io.Serializable
import java.util.*
import java.util.concurrent.Executors

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
class Broadcaster : Serializable {
    companion object {
        private val executorService = Executors.newSingleThreadExecutor()

        private val listeners = LinkedList<BroadcastListener>()

        @Synchronized
        @JvmStatic
        fun register(listener: BroadcastListener) {
            listeners.add(listener)
        }

        @Synchronized
        @JvmStatic
        fun unregister(listener: BroadcastListener) {
            listeners.remove(listener)
        }

        @Synchronized
        @JvmStatic
        fun broadcast(notification: BroadcastMessage) {
            listeners.forEach { listener -> executorService.execute { listener.broadcast(notification) } }
        }
    }
}
