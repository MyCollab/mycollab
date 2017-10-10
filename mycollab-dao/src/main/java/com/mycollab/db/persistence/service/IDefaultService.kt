/**
 * mycollab-dao - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.persistence.service

import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey

import java.io.Serializable

/**
 * @param <K>
 * @param <T>
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
</S></T></K> */
interface IDefaultService<K : Serializable, T, S : SearchCriteria> : ICrudService<K, T>, ISearchableService<S> {

    /**
     * @param record
     * @param searchCriteria
     */
    @CacheEvict
    fun updateBySearchCriteria(record: T, @CacheKey searchCriteria: S)
}
