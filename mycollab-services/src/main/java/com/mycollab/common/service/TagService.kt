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

import com.mycollab.common.domain.AggregateTag
import com.mycollab.common.domain.Tag
import com.mycollab.common.domain.TagExample
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.ICrudService

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
interface TagService : ICrudService<Int, Tag> {
    @Cacheable
    fun findTags(type: String, typeId: String, @CacheKey accountId: Int?): List<Tag>?

    @Cacheable
    fun findTagsInAccount(name: String, types: Array<String>, @CacheKey accountId: Int?): List<Tag>?

    @Cacheable
    fun findTagsInProject(projectId: Int, @CacheKey accountId: Int): List<AggregateTag>?

    fun deleteByExample(example: TagExample): Int?
}
