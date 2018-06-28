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
package com.mycollab.module.esb

import com.google.common.eventbus.AsyncEventBus
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class GenericCommand {
    @Autowired protected lateinit var asyncEventBus: AsyncEventBus

    @PostConstruct
    fun registerHandler() {
        asyncEventBus.register(this)
    }

    @PreDestroy
    fun unregisterHandler() {
        asyncEventBus.unregister(this)
    }
}