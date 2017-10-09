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
