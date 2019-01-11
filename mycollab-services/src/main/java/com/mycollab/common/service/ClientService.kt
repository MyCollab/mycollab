package com.mycollab.common.service

import com.mycollab.common.domain.Client
import com.mycollab.common.domain.SimpleClient
import com.mycollab.common.domain.criteria.ClientSearchCriteria
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
interface ClientService : IDefaultService<Int, Client, ClientSearchCriteria> {

    @Cacheable
    fun findById(id: Int, @CacheKey sAccountId: Int): SimpleClient?
}