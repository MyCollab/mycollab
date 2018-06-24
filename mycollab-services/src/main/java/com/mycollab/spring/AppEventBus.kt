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
package com.mycollab.spring

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.SubscriberExceptionHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.util.concurrent.Executors

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
@Configuration
@Profile("program", "test")
class AppEventBus {

    @Bean
    fun asyncEventBus(): AsyncEventBus = AsyncEventBus(Executors.newCachedThreadPool(),
            SubscriberExceptionHandler { throwable, _ -> LOG.error("Error in event bus execution", throwable) })

    companion object {
        private val LOG = LoggerFactory.getLogger(AppEventBus::class.java)

        @JvmStatic
        fun getInstance() = AppContextUtil.getSpringBean(AsyncEventBus::class.java)
    }
}
