package com.mycollab.db.persistence.service

import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey

import java.io.Serializable

/**
 * @param <K>
 * @param <T>
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
</S></T></K> */
interface IDefaultService<K : Serializable, T, S : SearchCriteria> : ICrudService<K, T>, ISearchableService<S> {

    /**
     * @param record
     * @param searchCriteria
     */
    @CacheEvict
    fun updateBySearchCriteria(record: T, @CacheKey searchCriteria: S)
}
