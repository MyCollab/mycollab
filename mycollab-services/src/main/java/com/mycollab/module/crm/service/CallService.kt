package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.CallWithBLOBs
import com.mycollab.module.crm.domain.SimpleCall
import com.mycollab.module.crm.domain.criteria.CallSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface CallService : IDefaultService<Int, CallWithBLOBs, CallSearchCriteria> {

    @Cacheable
    fun findById(callId: Int?, @CacheKey sAccountId: Int?): SimpleCall?
}
