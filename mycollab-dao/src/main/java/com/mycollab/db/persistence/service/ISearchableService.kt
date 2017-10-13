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
package com.mycollab.db.persistence.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SearchCriteria

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
</S> */
interface ISearchableService<S : SearchCriteria> : IService {
    /**
     * @param criteria
     * @return
     */
    @Cacheable
    fun getTotalCount(@CacheKey criteria: S): Int

    /**
     * @param searchRequest
     * @return
     */
    @Cacheable
    fun findPageableListByCriteria(@CacheKey searchRequest: BasicSearchRequest<S>): List<*>

    /**
     * @param searchCriteria
     * @param firstIndex
     * @param numberOfItems
     * @return
     */
    @Cacheable
    fun findAbsoluteListByCriteria(@CacheKey searchCriteria: S, firstIndex: Int, numberOfItems: Int): List<*>

    /**
     * @param criteria
     * @param sAccountId
     */
    @CacheEvict
    fun removeByCriteria(criteria: S, @CacheKey sAccountId: Int)

    /**
     * @param criteria
     * @return
     */
    @Cacheable
    fun getNextItemKey(@CacheKey criteria: S): Int?

    /**
     * @param criteria
     * @return
     */
    @Cacheable
    fun getPreviousItemKey(@CacheKey criteria: S): Int?
}
