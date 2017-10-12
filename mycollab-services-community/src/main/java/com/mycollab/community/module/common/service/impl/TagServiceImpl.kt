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
package com.mycollab.community.module.common.service.impl

import com.mycollab.common.domain.AggregateTag
import com.mycollab.common.domain.Tag
import com.mycollab.common.domain.TagExample
import com.mycollab.common.service.TagService
import com.mycollab.core.cache.CacheKey
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
@Service
class TagServiceImpl : TagService {
    override fun findTags(type: String, typeId: String, @CacheKey accountId: Int?): List<Tag>? {
        return null
    }

    override fun findTagsInAccount(name: String, types: Array<String>, @CacheKey accountId: Int?): List<Tag>? {
        return null
    }

    override fun findTagsInProject(projectId: Int, accountId: Int): List<AggregateTag>? {
        return null
    }

    override fun deleteByExample(example: TagExample): Int? {
        return 0
    }

    override fun saveWithSession(@CacheKey record: Tag, username: String?): Int {
        return -1
    }

    override fun updateWithSession(@CacheKey record: Tag, username: String?): Int {
        return -1
    }

    override fun updateSelectiveWithSession(@CacheKey record: Tag, username: String?): Int? {
        return null
    }

    override fun massUpdateWithSession(record: Tag, primaryKeys: List<Int>, @CacheKey accountId: Int?) {

    }

    override fun findByPrimaryKey(primaryKey: Int, sAccountId: Int): Tag? {
        return null;
    }

    override fun removeWithSession(item: Tag, username: String?, sAccountId: Int) {

    }

    override fun massRemoveWithSession(items: List<Tag>, username: String?, sAccountId: Int) {

    }
}
