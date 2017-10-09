package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.CaseWithBLOBs
import com.mycollab.module.crm.domain.SimpleCase
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface CaseService : IDefaultService<Int, CaseWithBLOBs, CaseSearchCriteria> {

    @Cacheable
    fun findById(caseId: Int?, @CacheKey sAccountId: Int?): SimpleCase
}
