package com.mycollab.common.service

import com.mycollab.common.domain.CustomViewStore
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.ICrudService

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
interface CustomViewStoreService : ICrudService<Int, CustomViewStore> {
    @Cacheable
    fun getViewLayoutDef(@CacheKey accountId: Int?, username: String, viewId: String): CustomViewStore

    @CacheEvict
    fun saveOrUpdateViewLayoutDef(@CacheKey viewStore: CustomViewStore)
}
