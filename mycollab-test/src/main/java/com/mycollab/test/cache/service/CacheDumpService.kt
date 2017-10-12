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
package com.mycollab.test.cache.service

import com.mycollab.cache.service.CacheService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Service
@Profile("test")
class CacheDumpService: CacheService {
    override fun getValue(group: String, key: String): Any?  = null

    override fun putValue(group: String, key: String, value: Any) {}

    override fun removeCacheItem(group: String, prefixKey: String) {}

    override fun removeCacheItems(group: String, vararg classes: Class<*>) {}
}