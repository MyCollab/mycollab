package com.mycollab.form.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IService
import com.mycollab.form.view.builder.type.DynaForm

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface MasterFormService : IService {
    @Cacheable
    fun findCustomForm(@CacheKey sAccountId: Int?, moduleName: String): DynaForm

    @CacheEvict
    fun saveCustomForm(@CacheKey sAccountId: Int?, moduleName: String, form: DynaForm)
}
