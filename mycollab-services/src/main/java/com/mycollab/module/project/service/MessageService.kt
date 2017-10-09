package com.mycollab.module.project.service

import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.project.domain.Message
import com.mycollab.module.project.domain.SimpleMessage
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface MessageService : IDefaultService<Int, Message, MessageSearchCriteria> {
    @Cacheable
    fun findById(messageId: Int, @CacheKey sAccountId: Int): SimpleMessage?
}
