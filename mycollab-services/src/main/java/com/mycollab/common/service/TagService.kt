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
