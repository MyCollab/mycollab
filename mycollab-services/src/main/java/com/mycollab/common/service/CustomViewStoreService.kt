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
package com.mycollab.common.service

import com.mycollab.common.domain.CustomViewStore
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.ICrudService

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
interface CustomViewStoreService : ICrudService<Int, CustomViewStore> {
    @Cacheable
    fun getViewLayoutDef(@CacheKey accountId: Int?, username: String, viewId: String): CustomViewStore

    @CacheEvict
    fun saveOrUpdateViewLayoutDef(@CacheKey viewStore: CustomViewStore)
}
