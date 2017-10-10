/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.common.service

import com.mycollab.common.domain.OptionVal
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.ICrudService

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
interface OptionValService : ICrudService<Int, OptionVal> {

    fun isExistedOptionVal(type: String, typeVal: String, fieldGroup: String, projectId: Int?, sAccountId: Int?): Boolean

    fun createDefaultOptions(sAccountId: Int?)

    @Cacheable
    fun findOptionVals(type: String, projectId: Int?, @CacheKey sAccountId: Int?): List<OptionVal>

    @Cacheable
    fun findOptionValsExcludeClosed(type: String, projectId: Int?, @CacheKey sAccountId: Int?): List<OptionVal>

    @CacheEvict
    fun massUpdateOptionIndexes(mapIndexes: List<Map<String, Int>>, @CacheKey sAccountId: Int?)
}
